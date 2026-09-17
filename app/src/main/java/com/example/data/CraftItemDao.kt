package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CraftItemDao {
  @Query("SELECT * FROM craft_items ORDER BY createdAt DESC")
  fun getAllItems(): Flow<List<CraftItem>>

  @Query("SELECT * FROM craft_items WHERE id = :id")
  fun getItemById(id: Long): Flow<CraftItem?>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(item: CraftItem): Long

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(items: List<CraftItem>)

  @Update
  suspend fun update(item: CraftItem)

  @Delete
  suspend fun delete(item: CraftItem)

  @Query("DELETE FROM craft_items WHERE id = :id")
  suspend fun deleteById(id: Long)

  @Query("UPDATE craft_items SET isUpvoted = :isUpvoted, upvotes = upvotes + :delta WHERE id = :id")
  suspend fun updateUpvote(id: Long, isUpvoted: Boolean, delta: Int)

  @Query("UPDATE craft_items SET isSaved = :isSaved WHERE id = :id")
  suspend fun updateSaved(id: Long, isSaved: Boolean)

  @Query("SELECT COUNT(*) FROM craft_items")
  suspend fun getCount(): Int
}
