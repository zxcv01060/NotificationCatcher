package tw.idv.louisli.notificationcatcher.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class NotificationApplication(
    @PrimaryKey
    val id: String,
    val createDate: Date,
    val isEnable: Boolean = true,
    var order: Int,
)
