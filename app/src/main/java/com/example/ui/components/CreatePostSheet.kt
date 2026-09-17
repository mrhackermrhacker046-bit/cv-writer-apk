package com.example.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.R
import com.example.ui.theme.DiamondCyan
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldAccent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostSheet(
  onDismiss: () -> Unit,
  onPublish: (
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
  ) -> Unit,
  modifier: Modifier = Modifier
) {
  var title by remember { mutableStateOf("") }
  var category by remember { mutableStateOf("Builds") }
  var description by remember { mutableStateOf("") }
  var author by remember { mutableStateOf("") }
  var edition by remember { mutableStateOf("Both") }
  var mcVersion by remember { mutableStateOf("1.21+") }
  var seed by remember { mutableStateOf("") }
  var coordinates by remember { mutableStateOf("") }
  var serverIp by remember { mutableStateOf("") }
  var tags by remember { mutableStateOf("") }
  var materials by remember { mutableStateOf("") }
  var difficulty by remember { mutableStateOf("Medium") }

  var selectedPresetImage by remember { mutableStateOf<String?>("img_build_castle") }
  var selectedCustomUri by remember { mutableStateOf<Uri?>(null) }
  var isCategoryExpanded by remember { mutableStateOf(false) }

  val photoPickerLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.PickVisualMedia()
  ) { uri: Uri? ->
    if (uri != null) {
      selectedCustomUri = uri
      selectedPresetImage = null
    }
  }

  val categoryOptions = listOf("Builds", "Seeds", "Redstone", "Servers", "Guides", "Textures")
  val editionOptions = listOf("Both", "Java", "Bedrock")
  val difficultyOptions = listOf("Easy", "Medium", "Master")

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = modifier
        .fillMaxSize()
        .padding(horizontal = 10.dp, vertical = 20.dp)
        .clip(RoundedCornerShape(24.dp))
        .border(
          width = 1.dp,
          color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
          shape = RoundedCornerShape(24.dp)
        )
        .testTag("create_post_dialog"),
      color = MaterialTheme.colorScheme.surface
    ) {
      Column(modifier = Modifier.fillMaxSize()) {
        // Sheet Header
        Surface(
          color = MaterialTheme.colorScheme.surfaceVariant,
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "Share Minecraft Creation",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = "Publish to the community hub",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }

            IconButton(
              onClick = onDismiss,
              modifier = Modifier.testTag("close_create_post_button")
            ) {
              Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Cancel",
                tint = MaterialTheme.colorScheme.onSurface
              )
            }
          }
        }

        // Form fields
        Column(
          modifier = Modifier
            .weight(1f)
            .verticalScroll(rememberScrollState())
            .padding(18.dp),
          verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
          // Visual / Screenshot selector
          Text(
            text = "Screenshot / Visual Banner",
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
            color = EmeraldPrimary
          )

          // Preview of chosen visual
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(150.dp)
              .clip(RoundedCornerShape(12.dp))
              .background(MaterialTheme.colorScheme.surfaceVariant)
              .border(
                1.dp,
                EmeraldPrimary.copy(alpha = 0.3f),
                RoundedCornerShape(12.dp)
              )
          ) {
            if (selectedCustomUri != null) {
              AsyncImage(
                model = selectedCustomUri,
                contentDescription = "Selected screenshot",
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop
              )
            } else if (selectedPresetImage != null) {
              val resId = when (selectedPresetImage) {
                "img_build_castle" -> R.drawable.img_build_castle
                "img_seed_cherry" -> R.drawable.img_seed_cherry
                "img_redstone_machine" -> R.drawable.img_redstone_machine
                "img_mc_hero" -> R.drawable.img_mc_hero
                else -> R.drawable.img_mc_hero
              }
              Image(
                painter = painterResource(id = resId),
                contentDescription = "Preset banner",
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop
              )
            }
          }

          // Pick preset or gallery button
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Button(
              onClick = {
                photoPickerLauncher.launch(
                  PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
              },
              colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
              modifier = Modifier
                .weight(1f)
                .testTag("pick_device_photo_btn")
            ) {
              Icon(
                imageVector = Icons.Default.AddPhotoAlternate,
                contentDescription = null,
                tint = EmeraldPrimary,
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text("Upload Image", color = MaterialTheme.colorScheme.onSurface, fontSize = 13.sp)
            }
          }

          // Quick preset thumbs
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            val presets = listOf(
              "img_build_castle" to "Castle",
              "img_seed_cherry" to "Cherry Biome",
              "img_redstone_machine" to "Redstone Lab",
              "img_mc_hero" to "Fortress"
            )
            presets.forEach { (presetKey, label) ->
              val isSelected = selectedPresetImage == presetKey && selectedCustomUri == null
              Surface(
                shape = RoundedCornerShape(8.dp),
                color = if (isSelected) EmeraldPrimary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier
                  .clip(RoundedCornerShape(8.dp))
                  .clickable {
                    selectedPresetImage = presetKey
                    selectedCustomUri = null
                  }
                  .border(
                    width = if (isSelected) 2.dp else 1.dp,
                    color = if (isSelected) EmeraldPrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(8.dp)
                  )
              ) {
                Text(
                  text = label,
                  style = MaterialTheme.typography.labelSmall,
                  color = if (isSelected) EmeraldPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                  modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                )
              }
            }
          }

          // Title
          OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title *") },
            placeholder = { Text("e.g. Modern Cliffside Villa or Quad Witch Hut Seed") },
            modifier = Modifier
              .fillMaxWidth()
              .testTag("input_title"),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = EmeraldPrimary,
              focusedLabelColor = EmeraldPrimary
            )
          )

          // Category Dropdown
          ExposedDropdownMenuBox(
            expanded = isCategoryExpanded,
            onExpandedChange = { isCategoryExpanded = !isCategoryExpanded }
          ) {
            OutlinedTextField(
              value = category,
              onValueChange = {},
              readOnly = true,
              label = { Text("Category") },
              trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCategoryExpanded) },
              modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
                .testTag("input_category"),
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = EmeraldPrimary,
                focusedLabelColor = EmeraldPrimary
              )
            )
            ExposedDropdownMenu(
              expanded = isCategoryExpanded,
              onDismissRequest = { isCategoryExpanded = false }
            ) {
              categoryOptions.forEach { opt ->
                DropdownMenuItem(
                  text = { Text(opt) },
                  onClick = {
                    category = opt
                    isCategoryExpanded = false
                    // Auto-adjust default preset based on category
                    if (selectedCustomUri == null) {
                      when (opt) {
                        "Builds" -> selectedPresetImage = "img_build_castle"
                        "Seeds" -> selectedPresetImage = "img_seed_cherry"
                        "Redstone" -> selectedPresetImage = "img_redstone_machine"
                        else -> selectedPresetImage = "img_mc_hero"
                      }
                    }
                  }
                )
              }
            }
          }

          // Author Gamertag
          OutlinedTextField(
            value = author,
            onValueChange = { author = it },
            label = { Text("Your Gamertag / Name") },
            placeholder = { Text("e.g. Steve_Master") },
            modifier = Modifier
              .fillMaxWidth()
              .testTag("input_author"),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = EmeraldPrimary,
              focusedLabelColor = EmeraldPrimary
            )
          )

          // Edition selection
          Text(
            text = "Target Minecraft Edition",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            editionOptions.forEach { ed ->
              FilterChip(
                selected = edition == ed,
                onClick = { edition = ed },
                label = { Text(ed) },
                colors = FilterChipDefaults.filterChipColors(
                  selectedContainerColor = if (ed == "Java") DiamondCyan else if (ed == "Bedrock") GoldAccent else EmeraldPrimary,
                  selectedLabelColor = Color.Black
                ),
                modifier = Modifier.testTag("create_edition_$ed")
              )
            }
          }

          // Version
          OutlinedTextField(
            value = mcVersion,
            onValueChange = { mcVersion = it },
            label = { Text("Game Version") },
            placeholder = { Text("1.21 Tricky Trials") },
            modifier = Modifier
              .fillMaxWidth()
              .testTag("input_version"),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = EmeraldPrimary,
              focusedLabelColor = EmeraldPrimary
            )
          )

          // Category-specific inputs
          if (category == "Seeds") {
            OutlinedTextField(
              value = seed,
              onValueChange = { seed = it },
              label = { Text("World Seed Number *") },
              placeholder = { Text("-82910481940182") },
              modifier = Modifier
                .fillMaxWidth()
                .testTag("input_seed"),
              singleLine = true,
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = EmeraldPrimary,
                focusedLabelColor = EmeraldPrimary
              )
            )

            OutlinedTextField(
              value = coordinates,
              onValueChange = { coordinates = it },
              label = { Text("Key Coordinates") },
              placeholder = { Text("X: 120, Y: 75, Z: -300") },
              modifier = Modifier
                .fillMaxWidth()
                .testTag("input_coords"),
              singleLine = true,
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = EmeraldPrimary,
                focusedLabelColor = EmeraldPrimary
              )
            )
          }

          if (category == "Servers") {
            OutlinedTextField(
              value = serverIp,
              onValueChange = { serverIp = it },
              label = { Text("Server IP Address / Port *") },
              placeholder = { Text("play.mycommunity.com:25565") },
              modifier = Modifier
                .fillMaxWidth()
                .testTag("input_server_ip"),
              singleLine = true,
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = EmeraldPrimary,
                focusedLabelColor = EmeraldPrimary
              )
            )
          }

          if (category in listOf("Builds", "Redstone", "Guides")) {
            OutlinedTextField(
              value = materials,
              onValueChange = { materials = it },
              label = { Text("Key Materials / Requirements") },
              placeholder = { Text("Copper Bulbs, Observers, Crafters, Chests") },
              modifier = Modifier
                .fillMaxWidth()
                .testTag("input_materials"),
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = EmeraldPrimary,
                focusedLabelColor = EmeraldPrimary
              )
            )

            Text(
              text = "Difficulty Level",
              style = MaterialTheme.typography.labelMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              difficultyOptions.forEach { diff ->
                FilterChip(
                  selected = difficulty == diff,
                  onClick = { difficulty = diff },
                  label = { Text(diff) },
                  colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = GoldAccent,
                    selectedLabelColor = Color.Black
                  )
                )
              }
            }
          }

          // Description
          OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description & Instructions *") },
            placeholder = { Text("Share instructions, build tips, lore, or how to reach this place...") },
            modifier = Modifier
              .fillMaxWidth()
              .height(110.dp)
              .testTag("input_description"),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = EmeraldPrimary,
              focusedLabelColor = EmeraldPrimary
            )
          )

          // Tags
          OutlinedTextField(
            value = tags,
            onValueChange = { tags = it },
            label = { Text("Tags") },
            placeholder = { Text("#castle, #survival, #starter") },
            modifier = Modifier
              .fillMaxWidth()
              .testTag("input_tags"),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = EmeraldPrimary,
              focusedLabelColor = EmeraldPrimary
            )
          )
        }

        // Bottom publish button
        Surface(
          tonalElevation = 8.dp,
          color = MaterialTheme.colorScheme.surfaceVariant,
          modifier = Modifier.fillMaxWidth()
        ) {
          Button(
            onClick = {
              if (title.isNotBlank() && description.isNotBlank()) {
                onPublish(
                  title,
                  category,
                  description,
                  author,
                  edition,
                  mcVersion,
                  selectedPresetImage,
                  selectedCustomUri?.toString(),
                  seed.ifBlank { null },
                  coordinates.ifBlank { null },
                  serverIp.ifBlank { null },
                  tags,
                  materials.ifBlank { null },
                  difficulty
                )
              }
            },
            enabled = title.isNotBlank() && description.isNotBlank(),
            colors = ButtonDefaults.buttonColors(
              containerColor = EmeraldPrimary,
              disabledContainerColor = EmeraldPrimary.copy(alpha = 0.3f)
            ),
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp)
              .testTag("publish_post_btn")
          ) {
            Icon(
              imageVector = Icons.Default.Upload,
              contentDescription = null,
              tint = Color.Black
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Publish to MineShare",
              color = Color.Black,
              fontWeight = FontWeight.Bold,
              fontSize = 16.sp
            )
          }
        }
      }
    }
  }
}
