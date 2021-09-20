package tw.idv.louisli.notificationcatcher.view.adapter

import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

abstract class FlowAdapter<I : Any, VH : RecyclerView.ViewHolder>(
    scope: CoroutineScope,
    private val flow: Flow<List<I>>
) : AbstractAdapter<VH>() {
    var itemList: List<I> = listOf()
        set(value) {
            val originValueLastIndex = field.lastIndex
            field = value
            notifyItemRangeInserted(
                originValueLastIndex + 1,
                value.size - originValueLastIndex + 1
            )
        }

    init {
        scope.launch {
            flow.distinctUntilChanged().collect { itemList = it }
        }
    }

    override fun getItemCount(): Int = itemList.size
}