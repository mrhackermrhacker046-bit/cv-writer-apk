package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.EmeraldPrimary

/**
 * Dialog component allowing users to add new Minecraft project entries
 * with fields for title and description to be saved in Room.
 */
@Composable
fun AddProjectDialog(
  onDismiss: () -> Unit,
  onConfirm: (title: String, description: String) -> Unit,
  modifier: Modifier = Modifier,
  onConfirmWithCategory: ((title: String, description: String, category: String) -> Unit)? = null
) {
  var title by remember { mutableStateOf("") }
  var description by remember { mutableStateOf("") }
  var category by remember { mutableStateOf("Builds") }
  var isError by remember { mutableStateOf(false) }

  val categories = listOf("Builds", "Redstone", "Seeds", "Servers", "Guides")

  AlertDialog(
    onDismissRequest = onDismiss,
    modifier = modifier.testTag("add_project_dialog"),
    icon = {
      Box(
        modifier = Modifier
          .size(48.dp)
          .clip(CircleShape)
          .background(EmeraldPrimary.copy(alpha = 0.15f)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.Add,
          contentDescription = null,
          tint = EmeraldPrimary,
          modifier = Modifier.size(28.dp)
        )
      }
    },
    title = {
      Text(
        text = "Add Minecraft Project",
        style = MaterialTheme.typography.titleLarge.copy(
          fontWeight = FontWeight.Bold,
          letterSpacing = 0.5.sp
        ),
        color = MaterialTheme.colorScheme.onSurface
      )
    },
    text = {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        Text(
          text = "Enter a title and description for your new Minecraft project entry to save it locally in Room.",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Title Field
        OutlinedTextField(
          value = title,
          onValueChange = {
            title = it
            if (isError && it.isNotBlank()) isError = false
          },
          label = { Text("Title *") },
          placeholder = { Text("e.g., Nether Hub or Medieval Castle") },
          singleLine = true,
          isError = isError && title.isBlank(),
          supportingText = {
            if (isError && title.isBlank()) {
              Text("Title is required", color = MaterialTheme.colorScheme.error)
            }
          },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("title_field"),
          shape = RoundedCornerShape(12.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = EmeraldPrimary,
            focusedLabelColor = EmeraldPrimary
          )
        )

        // Category selection
        Text(
          text = "Category",
          style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          categories.forEach { cat ->
            FilterChip(
              selected = category == cat,
              onClick = { category = cat },
              label = { Text(cat, fontSize = 12.sp) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = EmeraldPrimary,
                selectedLabelColor = Color.Black
              ),
              modifier = Modifier.testTag("chip_$cat")
            )
          }
        }

        // Description Field
        OutlinedTextField(
          value = description,
          onValueChange = { description = it },
          label = { Text("Description") },
          placeholder = { Text("Describe your build, redstone mechanism, or seed coordinates...") },
          minLines = 3,
          maxLines = 6,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("description_field"),
          shape = RoundedCornerShape(12.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = EmeraldPrimary,
            focusedLabelColor = EmeraldPrimary
          )
        )
      }
    },
    confirmButton = {
      Button(
        onClick = {
          if (title.isNotBlank()) {
            if (onConfirmWithCategory != null) {
              onConfirmWithCategory(title.trim(), description.trim(), category)
            } else {
              onConfirm(title.trim(), description.trim())
            }
          } else {
            isError = true
          }
        },
        enabled = title.isNotBlank(),
        colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.testTag("save_project_button")
      ) {
        Text("Save Project", color = Color.Black, fontWeight = FontWeight.Bold)
      }
    },
    dismissButton = {
      TextButton(
        onClick = onDismiss,
        modifier = Modifier.testTag("cancel_button")
      ) {
        Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant)
      }
    }
  )
}

/**
 * Alias component for AddProjectDialog
 */
@Composable
fun AddMinecraftProjectDialog(
  onDismiss: () -> Unit,
  onConfirm: (title: String, description: String) -> Unit,
  modifier: Modifier = Modifier,
  onConfirmWithCategory: ((title: String, description: String, category: String) -> Unit)? = null
) = AddProjectDialog(
  onDismiss = onDismiss,
  onConfirm = onConfirm,
  modifier = modifier,
  onConfirmWithCategory = onConfirmWithCategory
)
