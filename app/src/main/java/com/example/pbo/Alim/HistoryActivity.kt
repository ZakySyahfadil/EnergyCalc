package com.example.pbo.Alim

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pbo.R
import com.example.pbo.data.AppDatabase
import com.example.pbo.data.HistoryAdapter
// Pastikan import ini sesuai dengan nama folder kamu (util atau utils)
import com.example.pbo.utils.DialogUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

                            // Refresh list setelah hapus
                            withContext(Dispatchers.Main) { loadHistory() }
                        }
                    }
                )
            },
            onItemClick = { item ->
                // Pastikan package tujuan benar (sesuaikan dengan struktur projectmu)
                val intent = Intent(this, com.example.pbo.zaky.HistoryResults::class.java)
                intent.putExtra("history_id", item.id)
                startActivity(intent)
            }
        )

        recyclerHistory.adapter = adapter

        btnBack.setOnClickListener { finish() }

        loadHistory()
    }

    private fun loadHistory() {
        // 1. AMBIL ID USER YANG SEDANG LOGIN
        val prefs = getSharedPreferences("USER_PREFS", MODE_PRIVATE)
        val currentUser = prefs.getString("LOGIN_KEY", null)

        // Jika tidak ada user login (error/logout), tampilkan list kosong
        if (currentUser == null) {
            adapter.submitList(emptyList())
            emptyView.visibility = View.VISIBLE
            return
        }

        lifecycleScope.launch(Dispatchers.IO) {
            // 2. MINTA DATA KHUSUS MILIK USER TERSEBUT
            val items = AppDatabase.getDatabase(this@HistoryActivity)
                .historyDao()
                // Pastikan di HistoryDao fungsinya sudah: getAllHistory(owner: String)
                .getAllHistory(currentUser)

            withContext(Dispatchers.Main) {
                adapter.submitList(items)
                // Tampilkan teks "Empty" jika list kosong
                emptyView.visibility = if (items.isEmpty()) View.VISIBLE else View.GONE
            }
        }
    }
}