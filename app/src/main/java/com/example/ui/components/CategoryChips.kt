package com.example.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Fort
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Terrain
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.ui.theme.DiamondCyan
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldAccent

data class CategoryItem(
  val label: String,
  val icon: ImageVector
)

val CATEGORIES = listOf(
  CategoryItem("All", Icons.Default.AutoAwesome),
  CategoryItem("Builds", Icons.Default.Fort),
  CategoryItem("Seeds", Icons.Default.Terrain),
  CategoryItem("Redstone", Icons.Default.Bolt),
  CategoryItem("Servers", Icons.Default.Dns),
  CategoryItem("Guides", Icons.Default.MenuBook),
)

@Composable
fun CategoryFilterRow(
  selectedCategory: String,
  onSelectCategory: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .horizontalScroll(rememberScrollState())
      .padding(horizontal = 16.dp, vertical = 6.dp),
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    CATEGORIES.forEach { category ->
      val isSelected = selectedCategory.equals(category.label, ignoreCase = true)
      FilterChip(
        selected = isSelected,
        onClick = { onSelectCategory(category.label) },
        label = { Text(category.label) },
        leadingIcon = {
          Icon(
            imageVector = category.icon,
            contentDescription = null,
            modifier = Modifier.size(18.dp)
          )
        },
        colors = FilterChipDefaults.filterChipColors(
          selectedContainerColor = EmeraldPrimary,
          selectedLabelColor = MaterialTheme.colorScheme.background,
          selectedLeadingIconColor = MaterialTheme.colorScheme.background
        ),
        modifier = Modifier.testTag("filter_chip_${category.label.lowercase()}")
      )
    }
  }
}

@Composable
fun EditionFilterRow(
  selectedEdition: String,
  onSelectEdition: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val editions = listOf("All", "Java", "Bedrock")
  Row(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 2.dp),
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(
      text = "Edition:",
      style = MaterialTheme.typography.labelMedium,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    editions.forEach { edition ->
      val isSelected = selectedEdition.equals(edition, ignoreCase = true)
      FilterChip(
        selected = isSelected,
        onClick = { onSelectEdition(edition) },
        label = { Text(edition) },
        colors = FilterChipDefaults.filterChipColors(
          selectedContainerColor = if (edition == "Java") DiamondCyan else if (edition == "Bedrock") GoldAccent else MaterialTheme.colorScheme.secondary,
          selectedLabelColor = MaterialTheme.colorScheme.background
        ),
        modifier = Modifier.testTag("edition_chip_${edition.lowercase()}")
      )
    }
  }
}
