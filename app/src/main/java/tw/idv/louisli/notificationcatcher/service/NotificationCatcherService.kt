package tw.idv.louisli.notificationcatcher.service

import android.app.Notification
import android.content.Intent
import android.os.Bundle
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

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

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
        if (sbn.notification.flags and Notification.FLAG_GROUP_SUMMARY != 0) {
            return
        }

        val extras = sbn.notification.extras
        val title = obtainTitle(extras)
        val content = obtainContent(extras)
        scope.launch {
            notificationApplicationDAO.save(
                NotificationApplication(
                    id = sbn.packageName,
                    createDate = Date()
                )
            )

            if (title.isNullOrBlank() && content.isNullOrBlank()) {
                return@launch
            }
            notificationHistoryDAO.save(
                NotificationHistory(
                    appPackageName = sbn.packageName,
                    title = title,
                    content = content,
                    receiveDate = Date(sbn.postTime)
                )
            )
        }
    }

    private fun obtainTitle(extras: Bundle): String? = extras.getString(
        Notification.EXTRA_TITLE,
        extras.getString(Notification.EXTRA_TITLE_BIG)
    )

    private fun obtainContent(extras: Bundle): String? =
        extras.getCharSequence(Notification.EXTRA_TEXT)?.toString()
            ?: extras.getCharSequenceArray(Notification.EXTRA_TEXT_LINES)
                ?.joinToString(separator = "\n")

    override fun onListenerDisconnected() {
        IS_STARTED = false
        job.cancel()
    }
}