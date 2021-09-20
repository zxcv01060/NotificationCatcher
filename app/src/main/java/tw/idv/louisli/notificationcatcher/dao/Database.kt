package tw.idv.louisli.notificationcatcher.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import tw.idv.louisli.notificationcatcher.data.NotificationApplication
import tw.idv.louisli.notificationcatcher.data.NotificationHistory

@Database(
    entities = [NotificationApplication::class, NotificationHistory::class],
    version = 10,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class Database : RoomDatabase() {
    abstract val notificationApplicationDAO: NotificationApplicationDAO
    abstract val notificationHistoryDAO: NotificationHistoryDAO
}