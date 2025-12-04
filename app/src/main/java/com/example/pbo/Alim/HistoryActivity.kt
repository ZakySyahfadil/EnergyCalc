// file: app/src/main/java/com/example/pbo/Alim/HistoryActivity.kt
package com.example.pbo.Alim

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pbo.R
import com.example.pbo.data.AppDatabase
import com.example.pbo.data.HistoryAdapter
import com.example.pbo.data.HistoryEntity
import com.example.pbo.zaky.HistoryResults
import com.example.pbo.utils.DialogUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.widget.ImageView
import android.widget.TextView

class HistoryActivity : AppCompatActivity() {

    private lateinit var recyclerHistory: RecyclerView
    private lateinit var adapter: HistoryAdapter
    private lateinit var emptyView: TextView
    private lateinit var btnBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        recyclerHistory = findViewById(R.id.recyclerHistory)
        emptyView = findViewById(R.id.txtEmpty)
        btnBack = findViewById(R.id.btnBack)

        recyclerHistory.layoutManager = LinearLayoutManager(this)

        adapter = HistoryAdapter(
            onDeleteClick = { item ->
                DialogUtils.showUniversalDialog(
                    context = this,
                    message = "Are you sure you want to delete \"${item.deviceName}\"?",
                    isConfirmation = true,
                    onConfirm = {
                        lifecycleScope.launch(Dispatchers.IO) {
                            AppDatabase.getDatabase(this@HistoryActivity)
                                .historyDao().deleteById(item.id)

                            withContext(Dispatchers.Main) { loadHistory() }
                        }
                    },
                    onDismiss = {}
                )
            },
            onItemClick = { item ->
                val intent = Intent(this, com.example.pbo.zaky.HistoryResults::class.java).apply {
                    putExtra("deviceName", item.deviceName)
                    putExtra("kWh", item.kWh)
                    putExtra("totalCost", item.totalCost)
                    putExtra("date", item.date)

                    putExtra("powerValue", item.powerValue)
                    putExtra("durationValue", item.durationValue)
                    putExtra("frequencyValue", item.frequencyValue)
                }
                startActivity(intent)
            }
        )
        recyclerHistory.adapter = adapter

        btnBack.setOnClickListener { finish() }

        loadHistory()
    }

    private fun loadHistory() {
        lifecycleScope.launch(Dispatchers.IO) {
            val items = AppDatabase.getDatabase(this@HistoryActivity)
                .historyDao()
                .getAllHistory()

            withContext(Dispatchers.Main) {
                adapter.submitList(items)
                emptyView.visibility = if (items.isEmpty()) View.VISIBLE else View.GONE
            }
        }
    }
}