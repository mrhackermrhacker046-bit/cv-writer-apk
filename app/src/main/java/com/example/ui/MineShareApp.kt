package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.data.CraftItem
import com.example.ui.components.AddProjectDialog
import com.example.ui.components.CategoryFilterRow
import com.example.ui.components.CraftCard
import com.example.ui.components.CreatePostSheet
import com.example.ui.components.EditionFilterRow
import com.example.ui.components.ItemDetailDialog
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldAccent
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MineShareApp(
  viewModel: CraftViewModel,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
  val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
  val selectedEdition by viewModel.selectedEdition.collectAsStateWithLifecycle()
  val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
  val selectedSort by viewModel.selectedSort.collectAsStateWithLifecycle()

  val feedItems by viewModel.filteredFeed.collectAsStateWithLifecycle()
  val savedItems by viewModel.savedItems.collectAsStateWithLifecycle()
  val myItems by viewModel.myItems.collectAsStateWithLifecycle()

  val selectedItem by viewModel.selectedItem.collectAsStateWithLifecycle()
  val isCreateSheetOpen by viewModel.isCreateSheetOpen.collectAsStateWithLifecycle()
  val isAddProjectDialogOpen by viewModel.isAddProjectDialogOpen.collectAsStateWithLifecycle()

  val snackbarHostState = remember { SnackbarHostState() }
  var isSortMenuOpen by remember { mutableStateOf(false) }
  var isPublicShareOpen by remember { mutableStateOf(false) }

  val publicAppUrl = "https://ais-pre-yjccowtv4cgt3bwcehigt7-903539021876.europe-west2.run.app"

  LaunchedEffect(Unit) {
    viewModel.toastEvent.collectLatest { message ->
      snackbarHostState.showSnackbar(message)
    }
  }

  Scaffold(
    modifier = modifier.fillMaxSize(),
    containerColor = MaterialTheme.colorScheme.background,
    topBar = {
      TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = MaterialTheme.colorScheme.surface,
          titleContentColor = MaterialTheme.colorScheme.onSurface
        ),
        title = {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Box(
              modifier = Modifier
                .size(34.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(EmeraldPrimary),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.SportsEsports,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.size(22.dp)
              )
            }
            Column {
              Text(
                text = "MineShare",
                style = MaterialTheme.typography.titleLarge.copy(
                  fontWeight = FontWeight.ExtraBold,
                  letterSpacing = 0.5.sp
                )
              )
              Text(
                text = "COMMUNITY SHOWCASE",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold,
                  letterSpacing = 1.sp
                ),
                color = EmeraldPrimary
              )
            }
          }
        },
        actions = {
          IconButton(
            onClick = { isPublicShareOpen = true },
            modifier = Modifier.testTag("share_public_link_button")
          ) {
            Icon(
              imageVector = Icons.Default.Share,
              contentDescription = "Share Public App",
              tint = EmeraldPrimary
            )
          }

          Box {
            IconButton(
              onClick = { isSortMenuOpen = true },
              modifier = Modifier.testTag("sort_menu_button")
            ) {
              Icon(
                imageVector = Icons.Default.FilterList,
                contentDescription = "Sort",
                tint = MaterialTheme.colorScheme.onSurface
              )
            }

            DropdownMenu(
              expanded = isSortMenuOpen,
              onDismissRequest = { isSortMenuOpen = false }
            ) {
              SortOption.values().forEach { option ->
                DropdownMenuItem(
                  text = {
                    Text(
                      text = option.label,
                      fontWeight = if (selectedSort == option) FontWeight.Bold else FontWeight.Normal,
                      color = if (selectedSort == option) EmeraldPrimary else MaterialTheme.colorScheme.onSurface
                    )
                  },
                  onClick = {
                    viewModel.setSort(option)
                    isSortMenuOpen = false
                  }
                )
              }
            }
          }
        }
      )
    },
    bottomBar = {
      NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.navigationBarsPadding(),
        tonalElevation = 8.dp
      ) {
        NavigationBarItem(
          selected = currentTab == AppTab.EXPLORE,
          onClick = { viewModel.setTab(AppTab.EXPLORE) },
          icon = { Icon(Icons.Default.Explore, contentDescription = "Explore") },
          label = { Text("Explore") },
          colors = NavigationBarItemDefaults.colors(
            indicatorColor = EmeraldPrimary,
            selectedIconColor = Color.Black,
            selectedTextColor = EmeraldPrimary
          ),
          modifier = Modifier.testTag("nav_explore")
        )

        NavigationBarItem(
          selected = currentTab == AppTab.SAVED,
          onClick = { viewModel.setTab(AppTab.SAVED) },
          icon = {
            Icon(
              if (currentTab == AppTab.SAVED) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
              contentDescription = "Saved"
            )
          },
          label = { Text("Saved") },
          colors = NavigationBarItemDefaults.colors(
            indicatorColor = EmeraldPrimary,
            selectedIconColor = Color.Black,
            selectedTextColor = EmeraldPrimary
          ),
          modifier = Modifier.testTag("nav_saved")
        )

        NavigationBarItem(
          selected = currentTab == AppTab.MY_POSTS,
          onClick = { viewModel.setTab(AppTab.MY_POSTS) },
          icon = { Icon(Icons.Default.Inventory2, contentDescription = "My Creations") },
          label = { Text("My Shares") },
          colors = NavigationBarItemDefaults.colors(
            indicatorColor = EmeraldPrimary,
            selectedIconColor = Color.Black,
            selectedTextColor = EmeraldPrimary
          ),
          modifier = Modifier.testTag("nav_my_posts")
        )
      }
    },
    floatingActionButton = {
      FloatingActionButton(
        onClick = { viewModel.openAddProjectDialog() },
        containerColor = EmeraldPrimary,
        contentColor = Color.Black,
        modifier = Modifier.testTag("floating_action_button")
      ) {
        Icon(
          imageVector = Icons.Default.Add,
          contentDescription = "Add Minecraft project"
        )
      }
    },
    snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      when (currentTab) {
        AppTab.EXPLORE -> {
          ExploreView(
            items = feedItems,
            selectedCategory = selectedCategory,
            selectedEdition = selectedEdition,
            searchQuery = searchQuery,
            onSelectCategory = { viewModel.selectCategory(it) },
            onSelectEdition = { viewModel.selectEdition(it) },
            onSearchQueryChange = { viewModel.updateSearchQuery(it) },
            onItemClick = { viewModel.openItemDetail(it) },
            onUpvote = { viewModel.toggleUpvote(it) },
            onSave = { viewModel.toggleSaved(it) },
            onShare = { viewModel.shareItemExternal(context, it) },
            onCopyText = { label, value -> viewModel.copyToClipboard(label, value) },
            onOpenPublicShare = { isPublicShareOpen = true }
          )
        }

        AppTab.SAVED -> {
          SavedView(
            items = savedItems,
            searchQuery = searchQuery,
            onSearchQueryChange = { viewModel.updateSearchQuery(it) },
            onItemClick = { viewModel.openItemDetail(it) },
            onUpvote = { viewModel.toggleUpvote(it) },
            onSave = { viewModel.toggleSaved(it) },
            onShare = { viewModel.shareItemExternal(context, it) },
            onCopyText = { label, value -> viewModel.copyToClipboard(label, value) }
          )
        }

        AppTab.MY_POSTS -> {
          MyPostsView(
            items = myItems,
            onItemClick = { viewModel.openItemDetail(it) },
            onUpvote = { viewModel.toggleUpvote(it) },
            onSave = { viewModel.toggleSaved(it) },
            onShare = { viewModel.shareItemExternal(context, it) },
            onCopyText = { label, value -> viewModel.copyToClipboard(label, value) },
            onNewShareClick = { viewModel.openAddProjectDialog() }
          )
        }
      }

      // Dialogs
      if (isAddProjectDialogOpen) {
        AddProjectDialog(
          onDismiss = { viewModel.closeAddProjectDialog() },
          onConfirm = { title, description ->
            viewModel.addProject(title, description)
          },
          onConfirmWithCategory = { title, description, category ->
            viewModel.addProject(title, description, category)
          }
        )
      }

      selectedItem?.let { item ->
        ItemDetailDialog(
          item = item,
          onDismiss = { viewModel.closeItemDetail() },
          onUpvote = { viewModel.toggleUpvote(item) },
          onSave = { viewModel.toggleSaved(item) },
          onShare = { viewModel.shareItemExternal(context, item) },
          onDelete = { viewModel.deleteItem(item) },
          onCopyText = { label, value -> viewModel.copyToClipboard(label, value) }
        )
      }

      if (isCreateSheetOpen) {
        CreatePostSheet(
          onDismiss = { viewModel.closeCreateSheet() },
          onPublish = { title, category, desc, author, edition, ver, resName, uri, seed, coords, ip, tags, mat, diff ->
            viewModel.createCraftItem(
              title, category, desc, author, edition, ver, resName, uri, seed, coords, ip, tags, mat, diff
            )
          }
        )
      }

      if (isPublicShareOpen) {
        PublicShareDialog(
          publicUrl = publicAppUrl,
          onDismiss = { isPublicShareOpen = false },
          onCopyLink = {
            viewModel.copyToClipboard("MineShare Public Link", publicAppUrl)
          },
          onShareExternal = {
            val shareText = "⛏️ Check out MineShare! Open and test it live right in your browser or get the Android APK here:\n$publicAppUrl"
            val intent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
              type = "text/plain"
              putExtra(android.content.Intent.EXTRA_SUBJECT, "MineShare - Minecraft Community App")
              putExtra(android.content.Intent.EXTRA_TEXT, shareText)
            }
            context.startActivity(android.content.Intent.createChooser(intent, "Share MineShare via"))
          }
        )
      }
    }
  }
}

@Composable
fun ExploreView(
  items: List<CraftItem>,
  selectedCategory: String,
  selectedEdition: String,
  searchQuery: String,
  onSelectCategory: (String) -> Unit,
  onSelectEdition: (String) -> Unit,
  onSearchQueryChange: (String) -> Unit,
  onItemClick: (CraftItem) -> Unit,
  onUpvote: (CraftItem) -> Unit,
  onSave: (CraftItem) -> Unit,
  onShare: (CraftItem) -> Unit,
  onCopyText: (label: String, value: String) -> Unit,
  onOpenPublicShare: () -> Unit,
  modifier: Modifier = Modifier
) {
  LazyColumn(
    modifier = modifier.fillMaxSize(),
    contentPadding = PaddingValues(bottom = 80.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Search Field
    item {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 8.dp)
      ) {
        OutlinedTextField(
          value = searchQuery,
          onValueChange = onSearchQueryChange,
          placeholder = { Text("Search builds, seeds, redstone, servers...") },
          leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = null, tint = EmeraldPrimary)
          },
          trailingIcon = {
            if (searchQuery.isNotEmpty()) {
              IconButton(onClick = { onSearchQueryChange("") }) {
                Icon(Icons.Default.Clear, contentDescription = "Clear")
              }
            }
          },
          singleLine = true,
          shape = RoundedCornerShape(14.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = EmeraldPrimary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
          ),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("search_text_field")
        )
      }
    }

    // Public Sharing Banner
    item {
      Surface(
        shape = RoundedCornerShape(12.dp),
        color = EmeraldPrimary.copy(alpha = 0.12f),
        border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.35f)),
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp)
          .clip(RoundedCornerShape(12.dp))
          .clickable(onClick = onOpenPublicShare)
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.weight(1f)
          ) {
            Icon(
              imageVector = Icons.Default.Share,
              contentDescription = null,
              tint = EmeraldPrimary,
              modifier = Modifier.size(20.dp)
            )
            Column {
              Text(
                text = "Public Web & Mobile Link Ready",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = EmeraldPrimary
              )
              Text(
                text = "Anyone can open & test this app instantly in their browser",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
          Surface(
            shape = RoundedCornerShape(6.dp),
            color = EmeraldPrimary
          ) {
            Text(
              text = "Share",
              style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
              color = Color.Black,
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
            )
          }
        }
      }
    }

    // Featured Spotlight Banner (shown when not searching)
    if (searchQuery.isBlank() && selectedCategory == "All") {
      item {
        FeaturedSpotlightBanner(
          onExploreBuilds = { onSelectCategory("Builds") }
        )
      }
    }

    // Category Filter Pills
    item {
      Column {
        CategoryFilterRow(
          selectedCategory = selectedCategory,
          onSelectCategory = onSelectCategory
        )
        EditionFilterRow(
          selectedEdition = selectedEdition,
          onSelectEdition = onSelectEdition
        )
      }
    }

    // Feed items or Empty state
    if (items.isEmpty()) {
      item {
        EmptyFeedPlaceholder(
          onReset = {
            onSelectCategory("All")
            onSelectEdition("All")
            onSearchQueryChange("")
          }
        )
      }
    } else {
      items(items, key = { it.id }) { craftItem ->
        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
          CraftCard(
            item = craftItem,
            onClick = { onItemClick(craftItem) },
            onUpvote = { onUpvote(craftItem) },
            onSave = { onSave(craftItem) },
            onShare = { onShare(craftItem) },
            onCopyText = onCopyText
          )
        }
      }
    }
  }
}

@Composable
fun FeaturedSpotlightBanner(
  onExploreBuilds: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp)
      .clip(RoundedCornerShape(20.dp))
      .clickable(onClick = onExploreBuilds),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(170.dp)
    ) {
      Image(
        painter = painterResource(id = R.drawable.img_mc_hero),
        contentDescription = "Featured Showcase",
        modifier = Modifier.matchParentSize(),
        contentScale = ContentScale.Crop
      )

      Box(
        modifier = Modifier
          .matchParentSize()
          .background(
            Brush.verticalGradient(
              colors = listOf(
                Color.Transparent,
                Color.Black.copy(alpha = 0.85f)
              )
            )
          )
      )

      Column(
        modifier = Modifier
          .align(Alignment.BottomStart)
          .padding(16.dp)
      ) {
        Surface(
          shape = RoundedCornerShape(6.dp),
          color = GoldAccent
        ) {
          Text(
            text = "★ FEATURED SHOWCASE",
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
            color = Color.Black,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
          )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "Grand Mountain Fortress & Shaders",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
        )
        Text(
          text = "Tap to discover top rated community castles & survival bases",
          style = MaterialTheme.typography.bodySmall,
          color = Color.LightGray
        )
      }
    }
  }
}

@Composable
fun SavedView(
  items: List<CraftItem>,
  searchQuery: String,
  onSearchQueryChange: (String) -> Unit,
  onItemClick: (CraftItem) -> Unit,
  onUpvote: (CraftItem) -> Unit,
  onSave: (CraftItem) -> Unit,
  onShare: (CraftItem) -> Unit,
  onCopyText: (label: String, value: String) -> Unit,
  modifier: Modifier = Modifier
) {
  LazyColumn(
    modifier = modifier.fillMaxSize(),
    contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 80.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Saved Bookmarks",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = "${items.size} saved builds, seeds & contraptions",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    }

    if (items.isEmpty()) {
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
          ) {
            Icon(
              imageVector = Icons.Default.BookmarkBorder,
              contentDescription = null,
              tint = EmeraldPrimary,
              modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
              text = "No saved creations yet",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "Tap the bookmark icon on any build or seed card in the Explore tab to save it for quick offline access.",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
          }
        }
      }
    } else {
      items(items, key = { it.id }) { craftItem ->
        CraftCard(
          item = craftItem,
          onClick = { onItemClick(craftItem) },
          onUpvote = { onUpvote(craftItem) },
          onSave = { onSave(craftItem) },
          onShare = { onShare(craftItem) },
          onCopyText = onCopyText
        )
      }
    }
  }
}

@Composable
fun MyPostsView(
  items: List<CraftItem>,
  onItemClick: (CraftItem) -> Unit,
  onUpvote: (CraftItem) -> Unit,
  onSave: (CraftItem) -> Unit,
  onShare: (CraftItem) -> Unit,
  onCopyText: (label: String, value: String) -> Unit,
  onNewShareClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  LazyColumn(
    modifier = modifier.fillMaxSize(),
    contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 80.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "My Published Shares",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = "Items created and published by you",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    }

    if (items.isEmpty()) {
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
          ) {
            Icon(
              imageVector = Icons.Default.Inventory2,
              contentDescription = null,
              tint = EmeraldPrimary,
              modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
              text = "You haven't shared anything yet",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "Share your coolest Minecraft world seeds, redstone blueprints, SMP servers, or castle builds with the community!",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
              onClick = onNewShareClick,
              colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
            ) {
              Text("Share Your First Item", color = Color.Black, fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    } else {
      items(items, key = { it.id }) { craftItem ->
        CraftCard(
          item = craftItem,
          onClick = { onItemClick(craftItem) },
          onUpvote = { onUpvote(craftItem) },
          onSave = { onSave(craftItem) },
          onShare = { onShare(craftItem) },
          onCopyText = onCopyText
        )
      }
    }
  }
}

@Composable
fun EmptyFeedPlaceholder(
  onReset: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(32.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Icon(
        imageVector = Icons.Default.Search,
        contentDescription = null,
        tint = EmeraldPrimary,
        modifier = Modifier.size(48.dp)
      )
      Spacer(modifier = Modifier.height(12.dp))
      Text(
        text = "No Minecraft creations match your filters",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        textAlign = androidx.compose.ui.text.style.TextAlign.Center
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = "Try searching for different keywords or reset your category and edition filters.",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = androidx.compose.ui.text.style.TextAlign.Center
      )
      Spacer(modifier = Modifier.height(16.dp))
      Button(
        onClick = onReset,
        colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
      ) {
        Text("Clear Filters", color = Color.Black, fontWeight = FontWeight.Bold)
      }
    }
  }
}

@Composable
fun PublicShareDialog(
  publicUrl: String,
  onDismiss: () -> Unit,
  onCopyLink: () -> Unit,
  onShareExternal: () -> Unit,
  modifier: Modifier = Modifier
) {
  androidx.compose.ui.window.Dialog(
    onDismissRequest = onDismiss
  ) {
    Card(
      shape = RoundedCornerShape(24.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      border = androidx.compose.foundation.BorderStroke(
        width = 1.dp,
        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
      ),
      modifier = modifier
        .fillMaxWidth()
        .padding(16.dp)
        .testTag("public_share_dialog")
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Box(
          modifier = Modifier
            .size(54.dp)
            .clip(CircleShape)
            .background(EmeraldPrimary.copy(alpha = 0.15f)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Share,
            contentDescription = null,
            tint = EmeraldPrimary,
            modifier = Modifier.size(28.dp)
          )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
          text = "Share MineShare Publicly",
          style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
          color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
          text = "Anyone with this public link can open and test this app instantly in their mobile or desktop browser without installing anything!",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // URL container
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = MaterialTheme.colorScheme.surfaceVariant,
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(
              text = publicUrl,
              style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
              color = EmeraldPrimary,
              maxLines = 1,
              overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
              modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
              onClick = onCopyLink,
              modifier = Modifier.size(28.dp)
            ) {
              Icon(
                imageVector = androidx.compose.material.icons.Icons.Default.BookmarkBorder,
                contentDescription = "Copy Link",
                tint = EmeraldPrimary,
                modifier = Modifier.size(18.dp)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          androidx.compose.material3.OutlinedButton(
            onClick = onCopyLink,
            modifier = Modifier
              .weight(1f)
              .testTag("copy_public_link_btn")
          ) {
            Text("Copy Link")
          }

          Button(
            onClick = onShareExternal,
            colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
            modifier = Modifier
              .weight(1f)
              .testTag("send_public_link_btn")
          ) {
            Text("Send Link", color = Color.Black, fontWeight = FontWeight.Bold)
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        androidx.compose.material3.TextButton(
          onClick = onDismiss,
          modifier = Modifier.testTag("close_public_dialog_btn")
        ) {
          Text("Close", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
      }
    }
  }
}

