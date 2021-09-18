package tw.idv.louisli.notificationcatcher.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import tw.idv.louisli.notificationcatcher.data.NotificationHistory

@Dao
interface NotificationHistoryDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(history: NotificationHistory)
}