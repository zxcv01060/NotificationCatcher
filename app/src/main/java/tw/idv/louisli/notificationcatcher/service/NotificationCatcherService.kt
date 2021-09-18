package tw.idv.louisli.notificationcatcher.service

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import tw.idv.louisli.notificationcatcher.NotificationCatcherApplication
import tw.idv.louisli.notificationcatcher.dao.NotificationApplicationDAO
import tw.idv.louisli.notificationcatcher.dao.NotificationHistoryDAO
import tw.idv.louisli.notificationcatcher.data.NotificationApplication
import tw.idv.louisli.notificationcatcher.data.NotificationHistory
import java.util.*

class NotificationCatcherService : NotificationListenerService() {
    companion object {
        var IS_STARTED = false
    }

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)
    private lateinit var notificationApplicationDAO: NotificationApplicationDAO
    private lateinit var notificationHistoryDAO: NotificationHistoryDAO

    override fun onListenerConnected() {
        IS_STARTED = true
        this.notificationApplicationDAO =
            NotificationCatcherApplication.database.notificationApplicationDAO
        this.notificationHistoryDAO =
            NotificationCatcherApplication.database.notificationHistoryDAO
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        if (sbn == null || sbn.notification.extras.containsKey("mParcelledData.dataSize")) {
            return
        }

        val appInfo = packageManager.getApplicationInfo(sbn.packageName, 0)
        val extras = sbn.notification.extras
        scope.launch {
            notificationApplicationDAO.save(NotificationApplication(appInfo.uid, sbn.packageName))

            val title = extras.getString(
                Notification.EXTRA_TITLE,
                extras.getString(Notification.EXTRA_TITLE_BIG)
            )
            val content = extras.getCharSequence(Notification.EXTRA_TEXT)?.toString()
                ?: extras.getCharSequenceArray(Notification.EXTRA_TEXT_LINES)
                    ?.joinToString(separator = "\n")
            if (title.isNullOrBlank() && content.isNullOrBlank()) {
                return@launch
            }
            notificationHistoryDAO.save(
                NotificationHistory(
                    appId = appInfo.uid,
                    title = title,
                    content = content,
                    receiveDate = Date(sbn.postTime)
                )
            )
        }
    }

    override fun onListenerDisconnected() {
        IS_STARTED = false
        job.cancel()
    }
}