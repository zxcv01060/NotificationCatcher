package tw.idv.louisli.notificationcatcher.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import tw.idv.louisli.notificationcatcher.data.NotificationApplication

@Database(entities = [NotificationApplication::class], version = 1)
@TypeConverters(Converters::class)
abstract class Database : RoomDatabase() {
    abstract val notificationApplicationDAO: NotificationApplicationDAO
}