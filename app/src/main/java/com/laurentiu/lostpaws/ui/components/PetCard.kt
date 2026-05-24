package com.laurentiu.lostpaws.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.laurentiu.lostpaws.data.local.entity.PetEntity
import com.laurentiu.lostpaws.data.repository.PetRepository
import com.laurentiu.lostpaws.ui.theme.Green
import com.laurentiu.lostpaws.ui.theme.Red

@Composable
fun PetCard(
    pet: PetEntity,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AnimalImage(
                imageUrl = pet.imageUrl,
                contentDescription = pet.name,
                modifier = Modifier.size(92.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = pet.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (pet.isResolved) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = "Rezolvat",
                            tint = Green,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
                Text(
                    text = "${pet.type} • ${pet.city}, ${pet.area}",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = pet.breed,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
                StatusChip(status = pet.status)
            }
            IconButton(onClick = onFavoriteClick) {
                Icon(
                    imageVector = if (pet.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Favorit",
                    tint = if (pet.isFavorite) Red else MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

@Composable
fun StatusChip(status: String) {
    val isLost = status == PetRepository.STATUS_LOST
    AssistChip(
        onClick = {},
        label = {
            Text(text = if (isLost) "Pierdut" else "Gasit")
        },
        leadingIcon = {
            Spacer(modifier = Modifier.height(1.dp))
        },
        modifier = Modifier.height(32.dp)
    )
}
