package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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
import com.example.data.CraftItem
import com.example.ui.theme.DiamondCyan
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.RedstoneAccent

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ItemDetailDialog(
  item: CraftItem,
  onDismiss: () -> Unit,
  onUpvote: () -> Unit,
  onSave: () -> Unit,
  onShare: () -> Unit,
  onDelete: () -> Unit,
  onCopyText: (label: String, value: String) -> Unit,
  modifier: Modifier = Modifier
) {
  val upvoteColor by animateColorAsState(
    targetValue = if (item.isUpvoted) RedstoneAccent else MaterialTheme.colorScheme.onSurfaceVariant,
    label = "detail_upvote_color"
  )
  val saveColor by animateColorAsState(
    targetValue = if (item.isSaved) EmeraldPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
    label = "detail_save_color"
  )

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = modifier
        .fillMaxSize()
        .padding(horizontal = 12.dp, vertical = 24.dp)
        .clip(RoundedCornerShape(24.dp))
        .border(
          width = 1.dp,
          color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
          shape = RoundedCornerShape(24.dp)
        )
        .testTag("detail_dialog"),
      color = MaterialTheme.colorScheme.surface
    ) {
      Column(modifier = Modifier.fillMaxSize()) {
        // Visual Header with Close Button
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(230.dp)
        ) {
          val drawableResId = when (item.imageResName) {
            "img_mc_hero" -> R.drawable.img_mc_hero
            "img_build_castle" -> R.drawable.img_build_castle
            "img_redstone_machine" -> R.drawable.img_redstone_machine
            "img_seed_cherry" -> R.drawable.img_seed_cherry
            else -> null
          }

          if (item.imageUri != null) {
            AsyncImage(
              model = item.imageUri,
              contentDescription = item.title,
              modifier = Modifier.matchParentSize(),
              contentScale = ContentScale.Crop
            )
          } else if (drawableResId != null) {
            Image(
              painter = painterResource(id = drawableResId),
              contentDescription = item.title,
              modifier = Modifier.matchParentSize(),
              contentScale = ContentScale.Crop
            )
          } else {
            Box(
              modifier = Modifier
                .matchParentSize()
                .background(
                  Brush.linearGradient(
                    listOf(
                      Color(0xFF0F172A),
                      Color(0xFF064E3B),
                      Color(0xFF022C22)
                    )
                  )
                )
            )
          }

          // Top gradient shadow
          Box(
            modifier = Modifier
              .matchParentSize()
              .background(
                Brush.verticalGradient(
                  colors = listOf(
                    Color.Black.copy(alpha = 0.6f),
                    Color.Transparent,
                    Color.Black.copy(alpha = 0.8f)
                  )
                )
              )
          )

          // Close button
          IconButton(
            onClick = onDismiss,
            modifier = Modifier
              .align(Alignment.TopEnd)
              .padding(12.dp)
              .background(Color.Black.copy(alpha = 0.6f), CircleShape)
              .testTag("close_detail_button")
          ) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "Close",
              tint = Color.White
            )
          }

          // Category & Edition badges
          Row(
            modifier = Modifier
              .align(Alignment.BottomStart)
              .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = EmeraldPrimary
            ) {
              Text(
                text = item.category.uppercase(),
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
              )
            }

            Surface(
              shape = RoundedCornerShape(8.dp),
              color = DiamondCyan
            ) {
              Text(
                text = "${item.edition} • ${item.mcVersion}",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
              )
            }

            if (item.difficulty != null) {
              Surface(
                shape = RoundedCornerShape(8.dp),
                color = GoldAccent
              ) {
                Text(
                  text = item.difficulty,
                  style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                  color = Color.Black,
                  modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                )
              }
            }
          }
        }

        // Scrollable Body Content
        Column(
          modifier = Modifier
            .weight(1f)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
        ) {
          Text(
            text = item.title,
            style = MaterialTheme.typography.headlineSmall.copy(
              fontWeight = FontWeight.Bold,
              lineHeight = 30.sp
            ),
            color = MaterialTheme.colorScheme.onSurface
          )

          Spacer(modifier = Modifier.height(8.dp))

          // Author row
          Row(
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(EmeraldPrimary),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.size(18.dp)
              )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Shared by ${item.author}",
              style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }

          Spacer(modifier = Modifier.height(16.dp))

          // Copyable quick items
          if (item.seed != null) {
            DetailCopyCard(
              title = "World Seed",
              value = item.seed,
              icon = Icons.Default.ContentCopy,
              onCopy = { onCopyText("Seed", item.seed) },
              modifier = Modifier.testTag("detail_copy_seed")
            )
            Spacer(modifier = Modifier.height(10.dp))
          }

          if (item.coordinates != null) {
            DetailCopyCard(
              title = "Spawn / Feature Coordinates",
              value = item.coordinates,
              icon = Icons.Default.ContentCopy,
              onCopy = { onCopyText("Coordinates", item.coordinates) },
              modifier = Modifier.testTag("detail_copy_coords")
            )
            Spacer(modifier = Modifier.height(10.dp))
          }

          if (item.serverIp != null) {
            DetailCopyCard(
              title = "Server Connection IP",
              value = item.serverIp,
              icon = Icons.Default.ContentCopy,
              onCopy = { onCopyText("Server IP", item.serverIp) },
              modifier = Modifier.testTag("detail_copy_ip")
            )
            Spacer(modifier = Modifier.height(10.dp))
          }

          // Description
          Text(
            text = "Description & Details",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = EmeraldPrimary
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = item.description,
            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
            color = MaterialTheme.colorScheme.onSurface
          )

          // Materials list if present
          if (!item.materials.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
              text = "Key Materials & Blocks",
              style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
              color = EmeraldPrimary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = item.materials,
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }

          // Tags
          if (item.tags.isNotBlank()) {
            Spacer(modifier = Modifier.height(16.dp))
            FlowRow(
              horizontalArrangement = Arrangement.spacedBy(6.dp),
              verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              item.tags.split(",", " ").filter { it.isNotBlank() }.forEach { tag ->
                val cleanTag = if (tag.startsWith("#")) tag else "#$tag"
                Surface(
                  shape = RoundedCornerShape(6.dp),
                  color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                  Text(
                    text = cleanTag,
                    style = MaterialTheme.typography.labelSmall,
                    color = EmeraldPrimary,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                  )
                }
              }
            }
          }
        }

        // Bottom Action Bar
        Surface(
          tonalElevation = 6.dp,
          color = MaterialTheme.colorScheme.surfaceVariant,
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            // Upvote & Bookmark
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
              OutlinedButton(
                onClick = onUpvote,
                modifier = Modifier.testTag("detail_upvote_btn")
              ) {
                Icon(
                  imageVector = if (item.isUpvoted) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                  contentDescription = "Upvote",
                  tint = upvoteColor,
                  modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("${item.upvotes}", color = upvoteColor)
              }

              OutlinedButton(
                onClick = onSave,
                modifier = Modifier.testTag("detail_save_btn")
              ) {
                Icon(
                  imageVector = if (item.isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                  contentDescription = "Save",
                  tint = saveColor,
                  modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(if (item.isSaved) "Saved" else "Save", color = saveColor)
              }
            }

            // Share & Delete
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
              if (item.isUserCreated) {
                IconButton(
                  onClick = onDelete,
                  modifier = Modifier.testTag("detail_delete_btn")
                ) {
                  Icon(
                    imageVector = Icons.Default.DeleteOutline,
                    contentDescription = "Delete",
                    tint = RedstoneAccent
                  )
                }
              }

              Button(
                onClick = onShare,
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                modifier = Modifier.testTag("detail_share_btn")
              ) {
                Icon(
                  imageVector = Icons.Default.Share,
                  contentDescription = null,
                  tint = Color.Black,
                  modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("Share", color = Color.Black, fontWeight = FontWeight.Bold)
              }
            }
          }
        }
      }
    }
  }
}

@Composable
fun DetailCopyCard(
  title: String,
  value: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  onCopy: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surfaceVariant
    ),
    border = androidx.compose.foundation.BorderStroke(
      1.dp,
      EmeraldPrimary.copy(alpha = 0.4f)
    ),
    modifier = modifier.fillMaxWidth()
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = title,
          style = MaterialTheme.typography.labelSmall,
          color = EmeraldPrimary,
          fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = value,
          style = MaterialTheme.typography.bodyMedium.copy(
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp
          ),
          color = MaterialTheme.colorScheme.onSurface
        )
      }

      Button(
        onClick = onCopy,
        colors = ButtonDefaults.buttonColors(
          containerColor = EmeraldPrimary.copy(alpha = 0.15f),
          contentColor = EmeraldPrimary
        ),
        shape = RoundedCornerShape(8.dp)
      ) {
        Icon(
          imageVector = icon,
          contentDescription = "Copy",
          modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text("Copy", fontSize = 12.sp, fontWeight = FontWeight.Bold)
      }
    }
  }
}
