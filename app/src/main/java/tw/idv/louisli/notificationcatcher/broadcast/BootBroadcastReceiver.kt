package tw.idv.louisli.notificationcatcher.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import tw.idv.louisli.notificationcatcher.service.NotificationCatcherService

class BootBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action != "android.intent.action.BOOT_COMPLETED") {
            return
        }

        val serviceIntent = Intent(context, NotificationCatcherService::class.java)
        context?.startService(serviceIntent)
    }
}