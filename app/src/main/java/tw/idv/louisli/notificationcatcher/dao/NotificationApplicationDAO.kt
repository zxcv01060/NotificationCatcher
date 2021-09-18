package tw.idv.louisli.notificationcatcher.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import tw.idv.louisli.notificationcatcher.data.NotificationApplication

@Dao
interface NotificationApplicationDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(application: NotificationApplication)

    @Query("SELECT * FROM NotificationApplication WHERE id = :id")
    suspend fun getById(id: Int): NotificationApplication
}