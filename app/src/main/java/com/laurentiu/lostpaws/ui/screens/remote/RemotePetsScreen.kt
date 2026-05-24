package com.laurentiu.lostpaws.ui.screens.remote

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.laurentiu.lostpaws.data.repository.PetRepository
import com.laurentiu.lostpaws.data.repository.RemotePetImage
import com.laurentiu.lostpaws.ui.components.AnimalImage
import com.laurentiu.lostpaws.ui.components.EmptyState
import com.laurentiu.lostpaws.ui.components.LostPawsTopBar
import com.laurentiu.lostpaws.ui.viewmodel.RemotePetsViewModel

@Composable
fun RemotePetsScreen(
    viewModel: RemotePetsViewModel,
    onBackClick: () -> Unit
) {
    val state = viewModel.uiState

    LaunchedEffect(Unit) {
        if (state.images.isEmpty() && !state.isLoading) {
            viewModel.loadRemotePets()
        }
    }

    Scaffold(
        topBar = {
            LostPawsTopBar(
                title = "Imagini online",
                canNavigateBack = true,
                onBackClick = onBackClick
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        when {
            state.isLoading -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                    Text(
                        text = "Se incarca imagini cu animale...",
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }
            }

            state.errorMessage != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = state.errorMessage,
                        color = MaterialTheme.colorScheme.error
                    )
                    Button(
                        onClick = viewModel::loadRemotePets,
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        Text("Reincearca")
                    }
                }
            }

            state.images.isEmpty() -> {
                EmptyState(
                    title = "Nu exista imagini",
                    message = "API-urile nu au returnat rezultate.",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                )
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.images) { image ->
                        RemotePetCard(image = image)
                    }
                }
            }
        }
    }
}

@Composable
private fun RemotePetCard(image: RemotePetImage) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            AnimalImage(
                imageUrl = image.imageUrl,
                contentDescription = image.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            )
            Text(
                text = if (image.type == PetRepository.TYPE_DOG) "Caine" else "Pisica",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
            Text(
                text = image.imageUrl,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 12.dp)
            )
        }
    }
}
