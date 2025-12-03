// file: app/src/main/java/com/example/pbo/Alim/HistoryActivity.kt
package com.example.pbo.Alim

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pbo.R
import com.example.pbo.data.AppDatabase
import com.example.pbo.data.HistoryAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoryActivity : AppCompatActivity() {
    private lateinit var recycler: RecyclerView
    private lateinit var emptyView: TextView
    private lateinit var btnBack: ImageView
    private val adapter = HistoryAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        btnBack = findViewById(R.id.btnBack)
        emptyView = findViewById(R.id.emptyHistory)
        recycler = findViewById(R.id.recyclerHistory)

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        btnBack.setOnClickListener { finish() }

        loadHistory()
    }

    private fun loadHistory() {
        lifecycleScope.launch {
            val list = withContext(Dispatchers.IO) {
                AppDatabase.getDatabase(applicationContext).historyDao().getAllHistory()
            }

            if (list.isNullOrEmpty()) {
                emptyView.visibility = TextView.VISIBLE
                recycler.visibility = RecyclerView.GONE
            } else {
                emptyView.visibility = TextView.GONE
                recycler.visibility = RecyclerView.VISIBLE
                adapter.submitList(list)
            }
        }
    }
}