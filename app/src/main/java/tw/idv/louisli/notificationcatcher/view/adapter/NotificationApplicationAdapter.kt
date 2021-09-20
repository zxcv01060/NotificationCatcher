package tw.idv.louisli.notificationcatcher.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tw.idv.louisli.notificationcatcher.R
import tw.idv.louisli.notificationcatcher.data.NotificationApplication

class NotificationApplicationAdapter :
    RecyclerView.Adapter<NotificationApplicationAdapter.ViewHolder>() {
    var itemList: List<NotificationApplication> = listOf()
        set(value) {
            notifyItemRangeInserted(
                itemList.lastIndex + 1,
                value.size - itemList.size
            )
            field = value
        }
    private lateinit var context: Context
    private lateinit var inflater: LayoutInflater

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
        val info = context.packageManager.getApplicationInfo(itemList[position].appPackageName, 0)
        holder.imageIcon.setImageDrawable(context.packageManager.getApplicationIcon(info))
        holder.textName.text = context.packageManager.getApplicationLabel(info)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageIcon: ImageView = itemView.findViewById(R.id.image_application_icon)
        val textName: TextView = itemView.findViewById(R.id.text_application_name)
    }
}