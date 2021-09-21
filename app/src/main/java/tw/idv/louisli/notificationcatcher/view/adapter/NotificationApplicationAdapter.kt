package tw.idv.louisli.notificationcatcher.view.adapter

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.ViewSwitcher
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import tw.idv.louisli.notificationcatcher.R
import tw.idv.louisli.notificationcatcher.data.NotificationApplication

class NotificationApplicationAdapter(
    private val scope: CoroutineScope,
    flow: Flow<List<NotificationApplication>>,
    private val newsCountSupplier: (appPackageName: String) -> Flow<Long>,
    private val reorderListener: (holder: ViewHolder) -> Unit
) : FlowAdapter<NotificationApplicationAdapter.Item, NotificationApplicationAdapter.ViewHolder>(
    scope,
    flow.map { applications -> applications.map { Item(it, countFlow = newsCountSupplier(it.id)) } }
) {
    companion object {
        const val STATE_NEWS_COUNT = 0
        const val STATE_REORDER = 1
    }

    lateinit var onItemClickListener: (item: NotificationApplication) -> Unit
    var state: Int = STATE_NEWS_COUNT

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            inflater.inflate(R.layout.recycler_item_application, parent, false),
            reorderListener
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val info = context.packageManager.getApplicationInfo(itemList[position].application.id, 0)
        holder.imageIcon.setImageDrawable(context.packageManager.getApplicationIcon(info))
        holder.textName.text = context.packageManager.getApplicationLabel(info)
        scope.launch {
            itemList[position].countFlow.collectLatest {
                if (it == 0L) {
                    holder.textCount.visibility = View.GONE
                    return@collectLatest
                }

                holder.textCount.visibility = View.VISIBLE
                holder.textCount.text = if (it > 999) {
                    "999+"
                } else {
                    it.toString()
                }
            }
        }
        holder.itemView.setOnClickListener {
            if (state == STATE_REORDER) {
                return@setOnClickListener
            }

            onItemClickListener.invoke(itemList[position].application)
        }
        if (holder.switcher.displayedChild > state) {
            holder.switcher.showPrevious()
        } else if (holder.switcher.displayedChild < state) {
            holder.switcher.showNext()
        }
    }

    data class Item(
        val application: NotificationApplication,
        var countFlow: Flow<Long>
    )

    @SuppressLint("ClickableViewAccessibility")
    class ViewHolder(
        itemView: View,
        private val reorderListener: (holder: ViewHolder) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        val imageIcon: ImageView = itemView.findViewById(R.id.image_application_icon)
        val textName: TextView = itemView.findViewById(R.id.text_application_name)
        val switcher: ViewSwitcher =
            itemView.findViewById(R.id.switcher_application_count_and_reorder)
        val textCount: TextView = itemView.findViewById(R.id.text_application_news_count)
        private val imageReorder: ImageView = itemView.findViewById(R.id.image_application_reorder)

        init {
            itemView.isLongClickable = true
            imageReorder.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    reorderListener(this)
                }
                return@setOnTouchListener false
            }
        }
    }
}