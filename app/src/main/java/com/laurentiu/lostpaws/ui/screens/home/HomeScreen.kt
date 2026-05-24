package com.laurentiu.lostpaws.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.laurentiu.lostpaws.data.repository.PetRepository
import com.laurentiu.lostpaws.ui.components.EmptyState
import com.laurentiu.lostpaws.ui.components.LostPawsTopBar
import com.laurentiu.lostpaws.ui.components.PetCard
import com.laurentiu.lostpaws.ui.viewmodel.PetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    petViewModel: PetViewModel,
    onPetClick: (Long) -> Unit,
    onAddClick: () -> Unit,
    onRemoteClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    val state = petViewModel.uiState

    Scaffold(
        topBar = {
            LostPawsTopBar(
                title = "LostPaws",
                onProfileClick = onProfileClick,
                onRemoteClick = onRemoteClick
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Filled.Add, contentDescription = "Adauga anunt")
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = petViewModel::updateSearch,
                label = { Text("Cauta dupa nume, oras sau rasa") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            FilterRow(
                selected = state.selectedFilter,
                onSelected = petViewModel::updateFilter
            )
            if (state.pets.isEmpty() && !state.isLoading) {
                EmptyState(
                    title = "Nu exista anunturi",
                    message = "Adauga primul anunt sau schimba filtrul curent.",
                    modifier = Modifier.weight(1f)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 96.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.pets, key = { it.id }) { pet ->
                        PetCard(
                            pet = pet,
                            onClick = { onPetClick(pet.id) },
                            onFavoriteClick = { petViewModel.updateFavorite(pet) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FilterRow(
    selected: String,
    onSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            selected = selected == PetRepository.FILTER_ALL,
            onClick = { onSelected(PetRepository.FILTER_ALL) },
            label = { Text("Toate") }
        )
        FilterChip(
            selected = selected == PetRepository.STATUS_LOST,
            onClick = { onSelected(PetRepository.STATUS_LOST) },
            label = { Text("Pierdute") }
        )
        FilterChip(
            selected = selected == PetRepository.STATUS_FOUND,
            onClick = { onSelected(PetRepository.STATUS_FOUND) },
            label = { Text("Gasite") }
        )
    }
}
