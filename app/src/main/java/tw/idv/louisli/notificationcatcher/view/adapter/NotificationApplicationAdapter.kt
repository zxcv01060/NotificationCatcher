package tw.idv.louisli.notificationcatcher.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import tw.idv.louisli.notificationcatcher.R
import tw.idv.louisli.notificationcatcher.data.NotificationApplication

class NotificationApplicationAdapter(
    private val scope: CoroutineScope,
    private val newsCountSupplier: (appPackageName: String) -> Flow<Long>
) : RecyclerView.Adapter<NotificationApplicationAdapter.ViewHolder>() {
    var itemList: List<NotificationApplication> = listOf()
        set(value) {
            val originValueLastIndex = field.lastIndex
            field = value
            notifyItemRangeInserted(
                originValueLastIndex + 1,
                value.size - originValueLastIndex + 1
            )
        }
    lateinit var onItemClickListener: (item: NotificationApplication) -> Unit
    private lateinit var context: Context
    private lateinit var inflater: LayoutInflater
    private val suppliedAppPackageNameSet: MutableSet<String> = mutableSetOf()

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.context = recyclerView.context
        this.inflater = LayoutInflater.from(this.context)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.recycler_item_application, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val info = context.packageManager.getApplicationInfo(itemList[position].id, 0)
        holder.imageIcon.setImageDrawable(context.packageManager.getApplicationIcon(info))
        holder.textName.text = context.packageManager.getApplicationLabel(info)
        if (suppliedAppPackageNameSet.add(itemList[position].id)) {
            scope.launch {
                newsCountSupplier(itemList[position].id)
                    .collect {
                        if (it == 0L) {
                            holder.textCount.visibility = View.GONE
                            return@collect
                        }
                        holder.textCount.text = if (it > 999) {
                            "999+"
                        } else {
                            it.toString()
                        }
                    }
            }
        }
        holder.itemView.setOnClickListener { onItemClickListener.invoke(itemList[position]) }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageIcon: ImageView = itemView.findViewById(R.id.image_application_icon)
        val textName: TextView = itemView.findViewById(R.id.text_application_name)
        val textCount: TextView = itemView.findViewById(R.id.text_application_news_count)
    }
}