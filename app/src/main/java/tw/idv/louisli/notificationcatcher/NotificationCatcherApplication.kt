package tw.idv.louisli.notificationcatcher

import android.app.Application
import androidx.room.Room
import tw.idv.louisli.notificationcatcher.dao.Database

class NotificationCatcherApplication : Application() {
    companion object {
        lateinit var database: Database
    }

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(
            this,
            Database::class.java,
            "notification-catcher.db"
        ).fallbackToDestructiveMigration()
            .build()
    }
}