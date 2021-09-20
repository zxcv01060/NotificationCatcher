package tw.idv.louisli.notificationcatcher.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import tw.idv.louisli.notificationcatcher.NotificationCatcherApplication
import tw.idv.louisli.notificationcatcher.R
import tw.idv.louisli.notificationcatcher.dao.NotificationHistoryDAO
import tw.idv.louisli.notificationcatcher.view.adapter.NotificationHistoryAdapter

class NotificationHistoryActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_APP_PACKAGE_NAME =
            "tw.idv.louisli.notificationcatcher.NotificationHistoryActivity.extra.AppPackageName"
    }

    private val historyDAO: NotificationHistoryDAO =
        NotificationCatcherApplication.database.notificationHistoryDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_history)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_notification_history)
        recyclerView.layoutManager = LinearLayoutManager(this).apply {
            stackFromEnd = true
        }
        recyclerView.adapter = NotificationHistoryAdapter().apply {
            lifecycleScope.launch {
                historyDAO.searchByAppPackageName(intent.getStringExtra(EXTRA_APP_PACKAGE_NAME)!!)
                    .collect { this@apply.itemList = it }
            }
        }
    }
}