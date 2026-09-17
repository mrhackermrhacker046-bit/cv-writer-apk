package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "craft_items")
data class CraftItem(
  @PrimaryKey(autoGenerate = true)
  val id: Long = 0,
  val title: String,
  val category: String, // "Builds", "Seeds", "Redstone", "Servers", "Guides", "Textures"
  val description: String,
  val author: String,
  val edition: String = "Both", // "Java", "Bedrock", "Both"
  val mcVersion: String = "1.21+",
  val imageResName: String? = null,
  val imageUri: String? = null,
  val seed: String? = null,
  val coordinates: String? = null,
  val serverIp: String? = null,
  val tags: String = "",
  val materials: String? = null,
  val difficulty: String? = null, // "Easy", "Medium", "Master"
  val upvotes: Int = 0,
  val isUpvoted: Boolean = false,
  val isSaved: Boolean = false,
  val isUserCreated: Boolean = false,
  val createdAt: Long = System.currentTimeMillis(),
)
