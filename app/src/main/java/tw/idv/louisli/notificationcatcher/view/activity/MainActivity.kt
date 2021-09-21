package tw.idv.louisli.notificationcatcher.view.activity

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch
import tw.idv.louisli.notificationcatcher.NotificationCatcherApplication
import tw.idv.louisli.notificationcatcher.R
import tw.idv.louisli.notificationcatcher.broadcast.ServiceRestartBroadcastReceiver
import tw.idv.louisli.notificationcatcher.dao.NotificationApplicationDAO
import tw.idv.louisli.notificationcatcher.dao.NotificationHistoryDAO
import tw.idv.louisli.notificationcatcher.extension.RecyclerViewExtension.setDivider
import tw.idv.louisli.notificationcatcher.service.NotificationCatcherService
import tw.idv.louisli.notificationcatcher.view.ReorderItemTouchHelperCallback
import tw.idv.louisli.notificationcatcher.view.adapter.NotificationApplicationAdapter
import tw.idv.louisli.notificationcatcher.view.menuinfo.RecyclerViewMenuInfo

class MainActivity : AppCompatActivity() {
    private val notificationApplicationDAO: NotificationApplicationDAO =
        NotificationCatcherApplication.database.notificationApplicationDAO
    private val notificationHistoryDAO: NotificationHistoryDAO =
        NotificationCatcherApplication.database.notificationHistoryDAO
    private var isReorderState = false
    private val recyclerViewItemTouchHelper by lazy {
        ItemTouchHelper(ReorderItemTouchHelperCallback(adapter))
    }
    private val adapter: NotificationApplicationAdapter = NotificationApplicationAdapter(
        scope = lifecycleScope,
        flow = notificationApplicationDAO.searchAll(),
        newsCountSupplier = notificationHistoryDAO::getNewsCount,
        reorderListener = { recyclerViewItemTouchHelper.startDrag(it) }
    )

    private val recyclerView: RecyclerView by lazy { findViewById(R.id.recycler_main) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startNotificationListenerService()
        setupRecyclerView()
        setupReorderButton()
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
        AlertDialog.Builder(this)
            .setCancelable(false)
            .setMessage("檢測到尚未授予此App通知存取權，點下確定後會自動開啟設定頁面，請找到本App後授予通知存取權，然後再透過返回鍵返回此App即可")
            .setPositiveButton("確定") { _, _ ->
                launcher.launch(Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"))
            }
            .show()
    }

    private fun startListener() {
        startService(Intent(this, NotificationCatcherService::class.java))
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setDivider(this, R.attr.colorOnBackground, 2)
        recyclerView.adapter = adapter.apply {
            onItemClickListener = {
                startActivity(
                    Intent(this@MainActivity, NotificationHistoryActivity::class.java)
                        .apply {
                            putExtra(NotificationHistoryActivity.EXTRA_APP_PACKAGE_NAME, it.id)
                        }
                )
            }
        }
        recyclerViewItemTouchHelper.attachToRecyclerView(recyclerView)
        registerForContextMenu(recyclerView)
    }

    private fun setupReorderButton() {
        val buttonReorder = findViewById<FloatingActionButton>(R.id.button_main_reorder)
        buttonReorder.setOnClickListener {
            isReorderState = if (isReorderState) {
                buttonReorder.setImageResource(R.drawable.ic_baseline_reorder_24)
                saveOrder()
                false
            } else {
                buttonReorder.setImageResource(R.drawable.ic_baseline_check_24)
                startReorder()
                true
            }
            adapter.notifyItemRangeChanged(0, adapter.itemCount)
        }
    }

    private fun saveOrder() = lifecycleScope.launch {
        adapter.state = NotificationApplicationAdapter.STATE_NEWS_COUNT
        adapter.itemList.forEachIndexed { index, item -> item.application.order = index }
        notificationApplicationDAO.update(adapter.itemList.map { it.application })
    }

    private fun startReorder() {
        adapter.state = NotificationApplicationAdapter.STATE_REORDER
    }

    override fun onDestroy() {
        val intent = Intent(ServiceRestartBroadcastReceiver.ACTION_RESTART)
        intent.setClass(this, ServiceRestartBroadcastReceiver::class.java)
        sendBroadcast(intent)
        super.onDestroy()
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        val recyclerViewMenuInfo = menuInfo as RecyclerViewMenuInfo
        val holder = recyclerViewMenuInfo.holder as NotificationApplicationAdapter.ViewHolder
        menu?.setHeaderTitle(holder.textName.text)
        menuInflater.inflate(R.menu.notification_application, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.menu_item_notification_application_delete) {
            val menuInfo = item.menuInfo as RecyclerViewMenuInfo
            disableApplication(menuInfo.position)
            true
        } else {
            super.onContextItemSelected(item)
        }
    }

    private fun disableApplication(position: Int) {
        lifecycleScope.launch {
            val adapter = recyclerView.adapter as NotificationApplicationAdapter
            notificationApplicationDAO.disable(adapter.itemList[position].application.id)
            adapter.notifyItemRemoved(position)
        }
    }
}