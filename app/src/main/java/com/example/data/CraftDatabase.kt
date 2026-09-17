package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CraftItem::class], version = 1, exportSchema = false)
abstract class CraftDatabase : RoomDatabase() {
  abstract fun craftItemDao(): CraftItemDao

  companion object {
    @Volatile
    private var INSTANCE: CraftDatabase? = null

    fun getDatabase(context: Context): CraftDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          CraftDatabase::class.java,
          "craft_database"
        ).build()
        INSTANCE = instance
        instance
      }
    }
  }
}
