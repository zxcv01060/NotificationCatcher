package tw.idv.louisli.notificationcatcher.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import tw.idv.louisli.notificationcatcher.data.NotificationApplication

@Dao
interface NotificationApplicationDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun save(application: NotificationApplication)

    @Query("SELECT * FROM NotificationApplication WHERE isEnable = 1 ORDER BY `order`")
    fun searchAll(): Flow<List<NotificationApplication>>

    @Query("SELECT * FROM NotificationApplication WHERE id = :id")
    suspend fun getById(id: String): NotificationApplication

    @Query("UPDATE NotificationApplication SET isEnable = 0 WHERE id = :id")
    suspend fun disable(id: String)

    @Update
    suspend fun update(applications: List<NotificationApplication>)

    @Query("SELECT COUNT(*) FROM NotificationApplication WHERE isEnable = 1")
    suspend fun countByEnableIsTrue(): Int
}