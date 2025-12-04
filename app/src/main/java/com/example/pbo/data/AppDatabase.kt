package com.example.pbo.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [Account::class, HistoryEntity::class],
    version = 6, // naikkan dari 5 ke 6
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun accountDao(): AccountDao
    abstract fun historyDao(): HistoryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // --- Migration 5 -> 6 (Tambah 3 kolom baru di HistoryEntity)
        private val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(database: SupportSQLiteDatabase) {

                // Tambahkan kolom baru dengan default kosong
                database.execSQL(
                    "ALTER TABLE history_table ADD COLUMN powerValue TEXT NOT NULL DEFAULT ''"
                )

                database.execSQL(
                    "ALTER TABLE history_table ADD COLUMN durationValue TEXT NOT NULL DEFAULT ''"
                )

                database.execSQL(
                    "ALTER TABLE history_table ADD COLUMN frequencyValue TEXT NOT NULL DEFAULT ''"
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
                    .addMigrations(MIGRATION_5_6)  // gunakan migrasi, jangan destroy database
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}