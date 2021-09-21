package tw.idv.louisli.notificationcatcher.view

import android.content.Context
import android.util.AttributeSet
import android.view.ContextMenu
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import tw.idv.louisli.notificationcatcher.view.menuinfo.RecyclerViewMenuInfo

class ContextMenuRecyclerView : RecyclerView {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr)

    private lateinit var latestContextMenuInfo: RecyclerViewMenuInfo

    override fun getContextMenuInfo(): ContextMenu.ContextMenuInfo = latestContextMenuInfo

    override fun showContextMenuForChild(originalView: View?): Boolean {
        saveContextMenuInfo(originalView)
        return super.showContextMenuForChild(originalView)
    }

    private fun saveContextMenuInfo(child: View?) {
        this.latestContextMenuInfo = RecyclerViewMenuInfo(
            getChildAdapterPosition(child ?: throw IllegalArgumentException("child不能為null")),
            getChildViewHolder(child)
        )
    }

    override fun showContextMenuForChild(originalView: View?, x: Float, y: Float): Boolean {
        saveContextMenuInfo(originalView)
        return super.showContextMenuForChild(originalView, x, y)
    }
}