package tw.idv.louisli.notificationcatcher

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import tw.idv.louisli.notificationcatcher.service.NotificationCatcherService

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (isListenerStarted()) {
            return
        }

        if (isShouldRequestPermission()) {
            requestPermission()
        } else {
            startListener()
        }
    }

    private fun isListenerStarted(): Boolean = NotificationCatcherService.IS_STARTED

    private fun isShouldRequestPermission(): Boolean {
        val componentName = ComponentName(this, NotificationCatcherService::class.java)
        val enabledServiceNames =
            Settings.Secure.getString(contentResolver, "enabled_notification_listeners")
        return componentName.flattenToString() !in enabledServiceNames
    }

    private fun requestPermission() {
        val contract = ActivityResultContracts.StartActivityForResult()
        val launcher = registerForActivityResult(contract) { startListener() }
        launcher.launch(Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"))
    }

    private fun startListener() {
        startService(Intent(this, NotificationCatcherService::class.java))
    }
}