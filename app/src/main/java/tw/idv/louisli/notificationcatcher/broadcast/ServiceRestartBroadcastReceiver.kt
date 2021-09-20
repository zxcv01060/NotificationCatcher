package tw.idv.louisli.notificationcatcher.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import tw.idv.louisli.notificationcatcher.service.NotificationCatcherService

class ServiceRestartBroadcastReceiver : BroadcastReceiver() {
    companion object {
        const val ACTION_BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED"
        const val ACTION_RESTART = "tw.idv.louisli.notificationcatcher.broadcast.restart"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action != ACTION_BOOT_COMPLETED && intent?.action != ACTION_RESTART) {
            return
        }

        val serviceIntent = Intent(context, NotificationCatcherService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context?.startForegroundService(serviceIntent)
        } else {
            context?.startService(serviceIntent)
        }
    }
}