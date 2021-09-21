package tw.idv.louisli.notificationcatcher.view.adapter

interface ItemTouchHelperAdapter {
    fun onItemRemoved(position: Int)

    fun onItemMove(from: Int, to: Int)
}