package com.example.ui

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.CraftDatabase
import com.example.data.CraftItem
import com.example.data.CraftRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppTab {
  EXPLORE,
  SAVED,
  MY_POSTS
}

enum class SortOption(val label: String) {
  TRENDING("Trending"),
  NEWEST("Newest"),
  TOP_RATED("Top Rated")
}

class CraftViewModel(
  application: Application,
  private val repository: CraftRepository
) : AndroidViewModel(application) {

  val currentTab = MutableStateFlow(AppTab.EXPLORE)
  val selectedCategory = MutableStateFlow("All")
  val selectedEdition = MutableStateFlow("All")
  val searchQuery = MutableStateFlow("")
  val selectedSort = MutableStateFlow(SortOption.TRENDING)

  val selectedItem = MutableStateFlow<CraftItem?>(null)
  val isCreateSheetOpen = MutableStateFlow(false)
  val isAddProjectDialogOpen = MutableStateFlow(false)

  fun openAddProjectDialog() {
    isAddProjectDialogOpen.value = true
  }

  fun closeAddProjectDialog() {
    isAddProjectDialogOpen.value = false
  }

  // Message events for SnackBar/Toasts
  private val _toastEvent = MutableSharedFlow<String>()
  val toastEvent: SharedFlow<String> = _toastEvent

  init {
    viewModelScope.launch {
      repository.prepopulateIfNeeded()
    }
  }

  val allItems: StateFlow<List<CraftItem>> = repository.allItems
    .stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = emptyList()
    )

  // Filtered and sorted feed for Explore tab
  val filteredFeed: StateFlow<List<CraftItem>> = combine(
    allItems,
    selectedCategory,
    selectedEdition,
    searchQuery,
    selectedSort
  ) { items, category, edition, query, sort ->
    var filtered = items

    if (category != "All") {
      filtered = filtered.filter { it.category.equals(category, ignoreCase = true) }
    }

    if (edition != "All") {
      filtered = filtered.filter {
        it.edition.equals(edition, ignoreCase = true) || it.edition.equals("Both", ignoreCase = true)
      }
    }

    if (query.isNotBlank()) {
      val trimmed = query.trim()
      filtered = filtered.filter {
        it.title.contains(trimmed, ignoreCase = true) ||
          it.description.contains(trimmed, ignoreCase = true) ||
          it.author.contains(trimmed, ignoreCase = true) ||
          it.tags.contains(trimmed, ignoreCase = true) ||
          (it.seed?.contains(trimmed, ignoreCase = true) == true) ||
          (it.serverIp?.contains(trimmed, ignoreCase = true) == true)
      }
    }

    when (sort) {
      SortOption.TRENDING -> filtered.sortedByDescending { it.upvotes }
      SortOption.NEWEST -> filtered.sortedByDescending { it.createdAt }
      SortOption.TOP_RATED -> filtered.sortedByDescending { it.upvotes }
    }
  }.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = emptyList()
  )

  val savedItems: StateFlow<List<CraftItem>> = allItems
    .combine(searchQuery) { items, query ->
      val saved = items.filter { it.isSaved }
      if (query.isBlank()) saved
      else saved.filter {
        it.title.contains(query, ignoreCase = true) ||
          it.tags.contains(query, ignoreCase = true)
      }
    }.stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = emptyList()
    )

  val myItems: StateFlow<List<CraftItem>> = allItems
    .combine(searchQuery) { items, query ->
      val mine = items.filter { it.isUserCreated }
      if (query.isBlank()) mine
      else mine.filter {
        it.title.contains(query, ignoreCase = true) ||
          it.tags.contains(query, ignoreCase = true)
      }
    }.stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = emptyList()
    )

  fun setTab(tab: AppTab) {
    currentTab.value = tab
  }

  fun selectCategory(category: String) {
    selectedCategory.value = category
  }

  fun selectEdition(edition: String) {
    selectedEdition.value = edition
  }

  fun setSort(sort: SortOption) {
    selectedSort.value = sort
  }

  fun updateSearchQuery(query: String) {
    searchQuery.value = query
  }

  fun openItemDetail(item: CraftItem) {
    selectedItem.value = item
  }

  fun closeItemDetail() {
    selectedItem.value = null
  }

  fun openCreateSheet() {
    isCreateSheetOpen.value = true
  }

  fun closeCreateSheet() {
    isCreateSheetOpen.value = false
  }

  fun toggleUpvote(item: CraftItem) {
    viewModelScope.launch {
      repository.toggleUpvote(item)
      // Update selectedItem if open
      if (selectedItem.value?.id == item.id) {
        val newUpvoted = !item.isUpvoted
        val delta = if (newUpvoted) 1 else -1
        selectedItem.value = item.copy(
          isUpvoted = newUpvoted,
          upvotes = item.upvotes + delta
        )
      }
    }
  }

  fun toggleSaved(item: CraftItem) {
    viewModelScope.launch {
      repository.toggleSaved(item)
      val newSaved = !item.isSaved
      val msg = if (newSaved) "Saved to your bookmarks!" else "Removed from bookmarks"
      _toastEvent.emit(msg)
      if (selectedItem.value?.id == item.id) {
        selectedItem.value = item.copy(isSaved = newSaved)
      }
    }
  }

  fun deleteItem(item: CraftItem) {
    viewModelScope.launch {
      repository.delete(item)
      if (selectedItem.value?.id == item.id) {
        selectedItem.value = null
      }
      _toastEvent.emit("Post deleted successfully")
    }
  }

  fun addProject(
    title: String,
    description: String,
    category: String = "Builds"
  ) {
    viewModelScope.launch {
      val trimmedTitle = title.trim()
      val trimmedDesc = description.trim()
      val chosenCategory = category.ifBlank { "Builds" }
      val newItem = CraftItem(
        title = trimmedTitle,
        category = chosenCategory,
        description = trimmedDesc,
        author = "You",
        edition = "Both",
        mcVersion = "1.21+",
        imageResName = when (chosenCategory) {
          "Seeds" -> "img_seed_cherry"
          "Redstone" -> "img_redstone_machine"
          else -> "img_build_castle"
        },
        tags = "#project, #${chosenCategory.lowercase()}",
        upvotes = 1,
        isUpvoted = true,
        isSaved = true,
        isUserCreated = true,
        createdAt = System.currentTimeMillis()
      )
      repository.insert(newItem)
      closeAddProjectDialog()
      _toastEvent.emit("Saved project '$trimmedTitle' to MineShare!")
    }
  }

  fun createCraftItem(
    title: String,
    category: String,
    description: String,
    author: String,
    edition: String,
    mcVersion: String,
    imageResName: String?,
    imageUri: String?,
    seed: String?,
    coordinates: String?,
    serverIp: String?,
    tags: String,
    materials: String?,
    difficulty: String?
  ) {
    viewModelScope.launch {
      val newItem = CraftItem(
        title = title.trim(),
        category = category,
        description = description.trim(),
        author = if (author.isBlank()) "You" else author.trim(),
        edition = edition,
        mcVersion = if (mcVersion.isBlank()) "1.21+" else mcVersion.trim(),
        imageResName = imageResName,
        imageUri = imageUri,
        seed = seed?.trim()?.ifEmpty { null },
        coordinates = coordinates?.trim()?.ifEmpty { null },
        serverIp = serverIp?.trim()?.ifEmpty { null },
        tags = tags.trim(),
        materials = materials?.trim()?.ifEmpty { null },
        difficulty = difficulty,
        upvotes = 1,
        isUpvoted = true,
        isSaved = true,
        isUserCreated = true,
        createdAt = System.currentTimeMillis()
      )
      repository.insert(newItem)
      isCreateSheetOpen.value = false
      _toastEvent.emit("Published '${newItem.title}' to MineShare!")
    }
  }

  fun copyToClipboard(label: String, text: String) {
    val clipboard = getApplication<Application>().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText(label, text)
    clipboard.setPrimaryClip(clip)
    viewModelScope.launch {
      _toastEvent.emit("Copied $label to clipboard: $text")
    }
  }

  fun shareItemExternal(context: Context, item: CraftItem) {
    val shareBody = buildString {
      append("⛏️ Check out this Minecraft ${item.category}: ${item.title}\n")
      append("By: ${item.author} | Edition: ${item.edition} (${item.mcVersion})\n\n")
      append("${item.description}\n\n")
      item.seed?.let { append("🌱 Seed: $it\n") }
      item.coordinates?.let { append("📍 Coordinates: $it\n") }
      item.serverIp?.let { append("🖥️ Server IP: $it\n") }
      item.materials?.let { append("📦 Materials: $it\n") }
      if (item.tags.isNotBlank()) append("🏷️ Tags: ${item.tags}\n")
      append("\nShared via MineShare App")
    }

    val intent = Intent(Intent.ACTION_SEND).apply {
      type = "text/plain"
      putExtra(Intent.EXTRA_SUBJECT, item.title)
      putExtra(Intent.EXTRA_TEXT, shareBody)
    }
    context.startActivity(Intent.createChooser(intent, "Share via"))
  }

  companion object {
    fun provideFactory(application: Application): ViewModelProvider.Factory =
      object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
          val db = CraftDatabase.getDatabase(application)
          val repository = CraftRepository(db.craftItemDao())
          return CraftViewModel(application, repository) as T
        }
      }
  }
}
