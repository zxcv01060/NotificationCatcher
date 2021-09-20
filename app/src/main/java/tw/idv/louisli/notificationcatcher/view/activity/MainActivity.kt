package tw.idv.louisli.notificationcatcher.view.activity

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import tw.idv.louisli.notificationcatcher.NotificationCatcherApplication
import tw.idv.louisli.notificationcatcher.R
import tw.idv.louisli.notificationcatcher.dao.NotificationApplicationDAO
import tw.idv.louisli.notificationcatcher.service.NotificationCatcherService
import tw.idv.louisli.notificationcatcher.view.adapter.NotificationApplicationAdapter

class MainActivity : AppCompatActivity() {
    private val notificationApplicationDAO: NotificationApplicationDAO =
        NotificationCatcherApplication.database.notificationApplicationDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startNotificationListenerService()
        setupRecyclerView()
    }

    private fun startNotificationListenerService() {
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

    private fun setupRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_main)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = NotificationApplicationAdapter().apply {
            lifecycleScope.launch {
                notificationApplicationDAO.searchAll().collect {
                    this@apply.itemList = it
                }
            }
        }
    }
}