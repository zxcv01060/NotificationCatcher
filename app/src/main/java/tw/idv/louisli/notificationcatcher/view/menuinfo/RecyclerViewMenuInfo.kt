package tw.idv.louisli.notificationcatcher.view.menuinfo

import android.view.ContextMenu
import androidx.recyclerview.widget.RecyclerView

data class RecyclerViewMenuInfo(
    val position: Int,
    val holder: RecyclerView.ViewHolder
) : ContextMenu.ContextMenuInfo
