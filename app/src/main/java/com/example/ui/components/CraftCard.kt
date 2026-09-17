package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.IosShare
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.data.CraftItem
import com.example.ui.theme.DiamondCyan
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.RedstoneAccent

@Composable
fun CraftCard(
  item: CraftItem,
  onClick: () -> Unit,
  onUpvote: () -> Unit,
  onSave: () -> Unit,
  onShare: () -> Unit,
  onCopyText: (label: String, value: String) -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val upvoteColor by animateColorAsState(
    targetValue = if (item.isUpvoted) RedstoneAccent else MaterialTheme.colorScheme.onSurfaceVariant,
    label = "upvote_color"
  )
  val saveColor by animateColorAsState(
    targetValue = if (item.isSaved) EmeraldPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
    label = "save_color"
  )

  Card(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(16.dp))
      .clickable(onClick = onClick)
      .testTag("card_${item.id}"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surfaceVariant
    ),
    border = androidx.compose.foundation.BorderStroke(
      width = 1.dp,
      color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
    )
  ) {
    Column(modifier = Modifier.fillMaxWidth()) {
      // Card Visual Banner / Screenshot
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(180.dp)
          .background(MaterialTheme.colorScheme.surface)
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
          // Decorative Minecraft Obsidian & Emerald placeholder gradient
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
          ) {
            Text(
              text = item.category.uppercase(),
              style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Black,
                color = EmeraldPrimary.copy(alpha = 0.35f)
              ),
              modifier = Modifier.align(Alignment.Center)
            )
          }
        }

        // Gradient overlay for smooth readability
        Box(
          modifier = Modifier
            .matchParentSize()
            .background(
              Brush.verticalGradient(
                colors = listOf(
                  Color.Black.copy(alpha = 0.4f),
                  Color.Transparent,
                  Color.Black.copy(alpha = 0.7f)
                )
              )
            )
        )

        // Top Badges (Category & Edition)
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f)
          ) {
            Text(
              text = item.category.uppercase(),
              style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
              color = EmeraldPrimary,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }

          val editionColor = when (item.edition.lowercase()) {
            "java" -> DiamondCyan
            "bedrock" -> GoldAccent
            else -> EmeraldPrimary
          }
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = editionColor.copy(alpha = 0.9f)
          ) {
            Text(
              text = "${item.edition} • ${item.mcVersion}",
              style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
              color = Color.Black,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }
        }

        // Author pill at bottom of banner
        Row(
          modifier = Modifier
            .align(Alignment.BottomStart)
            .padding(12.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(24.dp)
              .clip(CircleShape)
              .background(EmeraldPrimary),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Person,
              contentDescription = null,
              tint = Color.Black,
              modifier = Modifier.size(16.dp)
            )
          }
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = item.author,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
            color = Color.White
          )
        }
      }

      // Card Content
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp)
      ) {
        Text(
          text = item.title,
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp
          ),
          color = MaterialTheme.colorScheme.onSurface,
          maxLines = 2,
          overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
          text = item.description,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          maxLines = 2,
          overflow = TextOverflow.Ellipsis
        )

        // Quick Copy Strip for Seeds / Servers / Coordinates
        if (item.seed != null || item.serverIp != null || item.coordinates != null) {
          Spacer(modifier = Modifier.height(10.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            if (item.seed != null) {
              QuickCopyPill(
                label = "Seed",
                value = item.seed,
                onCopy = { onCopyText("Seed", item.seed) },
                modifier = Modifier.weight(1f, fill = false)
              )
            }
            if (item.serverIp != null) {
              QuickCopyPill(
                label = "Server IP",
                value = item.serverIp,
                onCopy = { onCopyText("Server IP", item.serverIp) },
                modifier = Modifier.weight(1f, fill = false)
              )
            }
            if (item.coordinates != null && item.seed == null && item.serverIp == null) {
              QuickCopyPill(
                label = "XYZ",
                value = item.coordinates,
                onCopy = { onCopyText("Coordinates", item.coordinates) },
                modifier = Modifier.weight(1f, fill = false)
              )
            }
          }
        }

        // Action Row (Upvote, Bookmark, Share)
        Spacer(modifier = Modifier.height(12.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Upvote button
          Row(
            modifier = Modifier
              .clip(RoundedCornerShape(20.dp))
              .clickable(onClick = onUpvote)
              .padding(horizontal = 8.dp, vertical = 6.dp)
              .testTag("upvote_button_${item.id}"),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = if (item.isUpvoted) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
              contentDescription = "Upvote",
              tint = upvoteColor,
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = item.upvotes.toString(),
              style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
              color = upvoteColor
            )
          }

          Row(verticalAlignment = Alignment.CenterVertically) {
            // Bookmark button
            IconButton(
              onClick = onSave,
              modifier = Modifier.testTag("save_button_${item.id}")
            ) {
              Icon(
                imageVector = if (item.isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                contentDescription = "Save",
                tint = saveColor,
                modifier = Modifier.size(20.dp)
              )
            }

            // Share button
            IconButton(
              onClick = onShare,
              modifier = Modifier.testTag("share_button_${item.id}")
            ) {
              Icon(
                imageVector = Icons.Default.IosShare,
                contentDescription = "Share",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
              )
            }
          }
        }
      }
    }
  }
}

@Composable
fun QuickCopyPill(
  label: String,
  value: String,
  onCopy: () -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    shape = RoundedCornerShape(8.dp),
    color = MaterialTheme.colorScheme.surface,
    modifier = modifier
      .clip(RoundedCornerShape(8.dp))
      .clickable(onClick = onCopy)
      .border(
        width = 1.dp,
        color = EmeraldPrimary.copy(alpha = 0.35f),
        shape = RoundedCornerShape(8.dp)
      )
  ) {
    Row(
      modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "$label: $value",
        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
        color = MaterialTheme.colorScheme.onSurface,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.weight(1f, fill = false)
      )
      Spacer(modifier = Modifier.width(4.dp))
      Icon(
        imageVector = Icons.Default.ContentCopy,
        contentDescription = "Copy",
        tint = EmeraldPrimary,
        modifier = Modifier.size(14.dp)
      )
    }
  }
}
