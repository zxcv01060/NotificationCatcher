package tw.idv.louisli.notificationcatcher.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = NotificationApplication::class,
            parentColumns = ["id"],
            childColumns = ["appId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("id", unique = true), Index("appId")]
)
data class NotificationHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val appId: Int,
    val title: String?,
    val content: String?,
    val receiveDate: Date
)
