package tw.idv.louisli.notificationcatcher.view.adapter

import android.content.Context
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView

abstract class AbstractAdapter<VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {
    protected lateinit var context: Context
    protected lateinit var inflater: LayoutInflater

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.context = recyclerView.context
        this.inflater = LayoutInflater.from(this.context)
    }
}