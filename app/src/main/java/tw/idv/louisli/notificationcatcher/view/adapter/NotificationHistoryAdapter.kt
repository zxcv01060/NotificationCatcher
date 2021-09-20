package tw.idv.louisli.notificationcatcher.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tw.idv.louisli.notificationcatcher.R
import tw.idv.louisli.notificationcatcher.data.NotificationHistory
import java.text.SimpleDateFormat
import java.util.*

class NotificationHistoryAdapter : RecyclerView.Adapter<NotificationHistoryAdapter.ViewHolder>() {
    var itemList: List<NotificationHistory> = emptyList()
        set(value) {
            val originLastIndex = field.lastIndex
            field = value
            notifyItemRangeInserted(
                originLastIndex + 1,
                value.size - originLastIndex + 1
            )
        }
    private lateinit var inflater: LayoutInflater
    private val dateFormatter: SimpleDateFormat by lazy {
        SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.TAIWAN)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.inflater = LayoutInflater.from(recyclerView.context)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            inflater.inflate(
                R.layout.recycler_item_notification_history,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textTitle.text = itemList[position].title
        holder.textContent.text = itemList[position].content
        holder.textRecordTime.text = dateFormatter.format(itemList[position].receiveDate)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textTitle: TextView = itemView.findViewById(R.id.text_notification_history_title)
        val textContent: TextView = itemView.findViewById(R.id.text_notification_history_content)
        val textRecordTime: TextView =
            itemView.findViewById(R.id.text_notification_history_record_time)
    }
}