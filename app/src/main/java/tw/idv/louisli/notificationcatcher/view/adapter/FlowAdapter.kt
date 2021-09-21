package tw.idv.louisli.notificationcatcher.view.adapter

import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import tw.idv.louisli.notificationcatcher.extension.MutableListExtension.move

abstract class FlowAdapter<I : Any, VH : RecyclerView.ViewHolder>(
    scope: CoroutineScope,
    private val flow: Flow<List<I>>
) : AbstractAdapter<VH>(), ItemTouchHelperAdapter {
    var itemList: MutableList<I> = mutableListOf()
    private var isInitialized: Boolean = false

    init {
        scope.launch {
            flow.distinctUntilChanged().collectLatest {
                val newItemCount = itemList.size - it.size
                itemList = it.toMutableList()
                notifyItemRangeInserted(
                    if (isInitialized) {
                        itemList.lastIndex
                    } else {
                        isInitialized = true
                        0
                    },
                    newItemCount
                )
            }
        }
    }

    override fun getItemCount(): Int = itemList.size

    override fun onItemRemoved(position: Int) {
        itemList.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onItemMove(from: Int, to: Int) {
        itemList.move(from, to)
        notifyItemMoved(from, to)
    }
}