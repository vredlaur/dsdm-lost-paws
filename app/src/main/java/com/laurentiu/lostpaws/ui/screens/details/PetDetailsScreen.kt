package com.laurentiu.lostpaws.ui.screens.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.laurentiu.lostpaws.data.local.entity.PetEntity
import com.laurentiu.lostpaws.data.repository.PetRepository
import com.laurentiu.lostpaws.ui.components.AnimalImage
import com.laurentiu.lostpaws.ui.components.EmptyState
import com.laurentiu.lostpaws.ui.components.LostPawsTopBar
import com.laurentiu.lostpaws.ui.theme.Green
import com.laurentiu.lostpaws.ui.theme.Red
import com.laurentiu.lostpaws.ui.viewmodel.PetViewModel

@Composable
fun PetDetailsScreen(
    petId: Long,
    petViewModel: PetViewModel,
    onBackClick: () -> Unit,
    onDeleted: () -> Unit
) {
    LaunchedEffect(petId) {
        petViewModel.loadPet(petId)
    }

    val pet = petViewModel.selectedPet

    Scaffold(
        topBar = {
            LostPawsTopBar(
                title = "Detalii anunt",
                canNavigateBack = true,
                onBackClick = onBackClick
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        if (pet == null) {
            EmptyState(
                title = "Anunt indisponibil",
                message = "Anuntul nu a fost gasit sau a fost sters.",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            )
        } else {
            DetailsContent(
                pet = pet,
                canDelete = pet.ownerUserId == petViewModel.currentUserId(),
                onFavoriteClick = { petViewModel.updateFavorite(pet) },
                onResolvedClick = { petViewModel.markResolved(pet) },
                onDeleteClick = { petViewModel.deletePet(pet, onDeleted) },
                modifier = Modifier.padding(padding)
            )
        }
    }
}

@Composable
private fun DetailsContent(
    pet: PetEntity,
    canDelete: Boolean,
    onFavoriteClick: () -> Unit,
    onResolvedClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        AnimalImage(
            imageUrl = pet.imageUrl,
            contentDescription = pet.name,
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = pet.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (pet.status == PetRepository.STATUS_LOST) "Pierdut" else "Gasit",
                    color = if (pet.status == PetRepository.STATUS_LOST) Red else Green,
                    fontWeight = FontWeight.Bold
                )
            }
            IconButton(onClick = onFavoriteClick) {
                Icon(
                    imageVector = if (pet.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Favorit",
                    tint = if (pet.isFavorite) Red else MaterialTheme.colorScheme.secondary
                )
            }
        }
        if (pet.isResolved) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = Green)
                Text(" Anunt rezolvat", color = Green, fontWeight = FontWeight.Bold)
            }
        }
        InfoCard(pet)
        Button(
            onClick = onResolvedClick,
            enabled = !pet.isResolved,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (pet.isResolved) "Deja rezolvat" else "Marcheaza ca rezolvat")
        }
        if (canDelete) {
            OutlinedButton(
                onClick = onDeleteClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Filled.Delete, contentDescription = null)
                Text(" Sterge anunt")
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun InfoCard(pet: PetEntity) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            InfoRow("Tip", pet.type)
            InfoRow("Rasa", pet.breed)
            InfoRow("Culoare", pet.color)
            InfoRow("Gen", pet.gender)
            InfoRow("Oras", pet.city)
            InfoRow("Zona", pet.area)
            InfoRow("Telefon", pet.contactPhone)
            InfoRow("Recompensa", pet.reward)
            Text("Descriere", fontWeight = FontWeight.Bold)
            Text(pet.description)
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(0.35f)
        )
        Text(
            text = value,
            modifier = Modifier.weight(0.65f)
        )
    }
}
