package com.example.pbo.zaky

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.pbo.R
import com.example.pbo.data.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoryResults : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_results)

        val txtDevice = findViewById<TextView>(R.id.device)
        val txtAmount = findViewById<TextView>(R.id.amount)

        val txtPower = findViewById<TextView>(R.id.AmtPower)
        val txtDuration = findViewById<TextView>(R.id.AmtDuration)
        val txtFrequency = findViewById<TextView>(R.id.AmtFrequency)

        val btnDetails = findViewById<Button>(R.id.btndetails)
        val btnBack = findViewById<ImageView>(R.id.btnBack)

        val historyId = intent.getIntExtra("history_id", -1)
        if (historyId == -1) {
            finish()
            return
        }

        lifecycleScope.launch(Dispatchers.IO) {
            val dao = AppDatabase.getDatabase(this@HistoryResults).historyDao()
            val data = dao.getHistoryById(historyId) ?: return@launch

            withContext(Dispatchers.Main) {
                txtDevice.text = data.deviceName
                txtAmount.text = data.totalCost

                txtPower.text = data.powerValue
                txtDuration.text = data.durationValue
                txtFrequency.text = data.frequencyValue
            }
        }

        btnBack.setOnClickListener { finish() }

        btnDetails.setOnClickListener {
            val intent = Intent(this, HistoryDetails::class.java)
            intent.putExtra("history_id", historyId)  // 🔥 Pass ID
            startActivity(intent)
        }
    }
}