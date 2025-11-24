package com.example.pbo.zaky

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.pbo.Alim.settingActivity
import com.example.pbo.R
import com.example.pbo.data.AppDatabase
import kotlinx.coroutines.launch

class Main_Page : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_page)

        val tvName = findViewById<TextView>(R.id.name)
        val editProfile = findViewById<TextView>(R.id.edit)

        // Efek fade saat masuk halaman ini
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

        // Ambil LOGIN_KEY dari SharedPreferences
        val prefs = getSharedPreferences("USER_PREFS", MODE_PRIVATE)
        val loginKey = prefs.getString("LOGIN_KEY", null)

        if (loginKey != null) {
            lifecycleScope.launch {
                val db = AppDatabase.getDatabase(this@Main_Page)
                val dao = db.accountDao()

                // Cari user dari email atau phone
                val account = dao.getAccountByEmailOrPhone(loginKey)

                runOnUiThread {
                    if (account != null) {
                        tvName.text = "${account.firstName} ${account.lastName}"
                    } else {
                        tvName.text = "User"
                    }
                }
            }
        } else {
            // Jika tidak ada login, tampilkan default
            tvName.text = "User"
        }

        // Edit profile
        editProfile.setOnClickListener {
            startActivity(Intent(this, settingActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }
}