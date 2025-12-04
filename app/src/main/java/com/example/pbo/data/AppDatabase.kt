package com.example.pbo.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [Account::class, HistoryEntity::class],
    version = 7, // 🔥 NAIK DARI 6 KE 7
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun accountDao(): AccountDao
    abstract fun historyDao(): HistoryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // --- Migration 5 -> 6 (Lama, biarkan saja)
        private val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE history_table ADD COLUMN powerValue TEXT NOT NULL DEFAULT ''")
                database.execSQL("ALTER TABLE history_table ADD COLUMN durationValue TEXT NOT NULL DEFAULT ''")
                database.execSQL("ALTER TABLE history_table ADD COLUMN frequencyValue TEXT NOT NULL DEFAULT ''")
            }
        }

        // 🔥 MIGRATION 6 -> 7 (BARU: Tambah kolom userOwner)
        private val MIGRATION_6_7 = object : Migration(6, 7) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Tambahkan kolom userOwner dengan default kosong
                database.execSQL(
                    "ALTER TABLE history_table ADD COLUMN userOwner TEXT NOT NULL DEFAULT ''"
                )
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "user_database"
                )
                    // 🔥 Jangan lupa tambahkan MIGRATION_6_7 di sini
                    .addMigrations(MIGRATION_5_6, MIGRATION_6_7)
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}