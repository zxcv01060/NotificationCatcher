package tw.idv.louisli.notificationcatcher.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class NotificationApplication(
    @PrimaryKey
    val id: Int,
    val appPackageName: String,
    val createDate: Date,
    val isEnable: Boolean = true
)
