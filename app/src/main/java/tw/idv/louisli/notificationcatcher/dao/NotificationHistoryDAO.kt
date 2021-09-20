package tw.idv.louisli.notificationcatcher.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import tw.idv.louisli.notificationcatcher.data.NotificationHistory

@Dao
interface NotificationHistoryDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(history: NotificationHistory)

    @Query(
        """
        SELECT COUNT(*) FROM NotificationHistory
                WHERE appPackageName = :appPackageName AND readTime IS NULL
    """
    )
    fun getNewsCount(appPackageName: String): Flow<Long>

    @Query("SELECT * FROM NotificationHistory WHERE appPackageName = :appPackageName")
    fun searchByAppPackageName(appPackageName: String): Flow<List<NotificationHistory>>

    @Update
    suspend fun update(history: NotificationHistory)
}