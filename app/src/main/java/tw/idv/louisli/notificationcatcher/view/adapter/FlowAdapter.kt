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
            val removedItemIndexList = (field - value).map { field.indexOf(it) }

            field = value
            if (!isInitialized) {
                return
            }
            
            if (removedItemIndexList.isEmpty()) {
                notifyItemInserted(value.size)
            } else {
                notifyItemRemoved(removedItemIndexList[0])
            }
        }
    private var isInitialized: Boolean = false

    init {
        scope.launch {
            flow.distinctUntilChanged().collect {
                itemList = it
                isInitialized = true
            }
        }
    }

    override fun getItemCount(): Int = itemList.size
}