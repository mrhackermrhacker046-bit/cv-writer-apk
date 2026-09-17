package com.example

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.CraftDatabase
import com.example.data.CraftItem
import com.example.data.CraftRepository
import com.example.ui.CraftViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  private lateinit var db: CraftDatabase
  private lateinit var repository: CraftRepository

  @Before
  fun setup() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    db = Room.inMemoryDatabaseBuilder(context, CraftDatabase::class.java)
      .allowMainThreadQueries()
      .build()
    repository = CraftRepository(db.craftItemDao())
  }

  @After
  fun teardown() {
    db.close()
  }

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("MineShare", appName)
  }

  @Test
  fun `add new Minecraft project entry with title and description saves in Room`() = runBlocking {
    val newProject = CraftItem(
      title = "Automatic Nether Wart Farm",
      description = "Compact redstone clock with water flush collection system for nether wart.",
      category = "Redstone",
      author = "Crafter99"
    )

    val insertedId = repository.insert(newProject)
    assertTrue(insertedId > 0)

    val allItems = repository.allItems.first()
    val savedProject = allItems.find { it.title == "Automatic Nether Wart Farm" }

    assertNotNull(savedProject)
    assertEquals("Automatic Nether Wart Farm", savedProject?.title)
    assertEquals("Compact redstone clock with water flush collection system for nether wart.", savedProject?.description)
    assertEquals("Redstone", savedProject?.category)
  }

  @Test
  fun `CraftViewModel addProject saves entry in Room and manages dialog state`() = runBlocking {
    val context = ApplicationProvider.getApplicationContext<Application>()
    val viewModel = CraftViewModel(context, repository)

    // Open dialog
    viewModel.openAddProjectDialog()
    assertTrue(viewModel.isAddProjectDialogOpen.value)

    // Add project with title and description
    viewModel.addProject(
      title = "Skyblock Island Base",
      description = "Efficient starter platform with cobblestone generator and mob drop tower.",
      category = "Builds"
    )

    org.robolectric.shadows.ShadowLooper.idleMainLooper()

    // Dialog should be closed after saving
    assertEquals(false, viewModel.isAddProjectDialogOpen.value)

    // Entry should be persisted in Room
    val allItems = repository.allItems.first()
    val foundItem = allItems.find { it.title == "Skyblock Island Base" }

    assertNotNull(foundItem)
    assertEquals("Skyblock Island Base", foundItem?.title)
    assertEquals("Efficient starter platform with cobblestone generator and mob drop tower.", foundItem?.description)
  }
}

