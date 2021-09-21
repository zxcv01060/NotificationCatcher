package tw.idv.louisli.notificationcatcher.extension

object MutableListExtension {
    fun <E> MutableList<E>.move(from: Int, to: Int) {
        if (from == to) {
            return
        }

        val element = removeAt(from)
        add(to, element)
    }
}