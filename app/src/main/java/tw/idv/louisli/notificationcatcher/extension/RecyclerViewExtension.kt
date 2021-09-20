package tw.idv.louisli.notificationcatcher.extension

import android.content.Context
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import tw.idv.louisli.notificationcatcher.view.DividerItemDecoration

object RecyclerViewExtension {
    fun RecyclerView.setDivider(context: Context, @AttrRes colorAttrId: Int, height: Int = 1) {
        val typeValue = TypedValue()
        context.theme.resolveAttribute(colorAttrId, typeValue, true)
        setDivider(typeValue.data, height)
    }

    fun RecyclerView.setDivider(@ColorInt color: Int, height: Int = 1) {
        addItemDecoration(DividerItemDecoration(color, height))
    }
}