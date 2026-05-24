package com.laurentiu.lostpaws.ui.screens.add

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.laurentiu.lostpaws.data.repository.PetRepository
import com.laurentiu.lostpaws.ui.components.LostPawsTopBar
import com.laurentiu.lostpaws.ui.viewmodel.PetFormState
import com.laurentiu.lostpaws.ui.viewmodel.PetViewModel

@Composable
fun AddPetScreen(
    petViewModel: PetViewModel,
    onBackClick: () -> Unit,
    onSaved: () -> Unit
) {
    val form = petViewModel.formState
    val state = petViewModel.uiState

    Scaffold(
        topBar = {
            LostPawsTopBar(
                title = "Adauga anunt",
                canNavigateBack = true,
                onBackClick = onBackClick
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Tip animal", style = MaterialTheme.typography.titleSmall)
            ChoiceRow(
                options = listOf(PetRepository.TYPE_DOG, PetRepository.TYPE_CAT, PetRepository.TYPE_OTHER),
                selected = form.type,
                labelFor = { if (it == PetRepository.TYPE_DOG) "Caine" else if (it == PetRepository.TYPE_CAT) "Pisica" else "Altul" },
                onSelected = { selected ->
                    petViewModel.updateForm { copy(type = selected) }
                }
            )
            Text("Status", style = MaterialTheme.typography.titleSmall)
            ChoiceRow(
                options = listOf(PetRepository.STATUS_LOST, PetRepository.STATUS_FOUND),
                selected = form.status,
                labelFor = { if (it == PetRepository.STATUS_LOST) "Pierdut" else "Gasit" },
                onSelected = { selected ->
                    petViewModel.updateForm { copy(status = selected) }
                }
            )
            FormTextField("Nume animal", form.name) { value ->
                petViewModel.updateForm { copy(name = value) }
            }
            FormTextField("Rasa", form.breed) { value ->
                petViewModel.updateForm { copy(breed = value) }
            }
            FormTextField("Culoare", form.color) { value ->
                petViewModel.updateForm { copy(color = value) }
            }
            FormTextField("Gen", form.gender) { value ->
                petViewModel.updateForm { copy(gender = value) }
            }
            FormTextField("Oras", form.city) { value ->
                petViewModel.updateForm { copy(city = value) }
            }
            FormTextField("Zona", form.area) { value ->
                petViewModel.updateForm { copy(area = value) }
            }
            OutlinedTextField(
                value = form.description,
                onValueChange = { value -> petViewModel.updateForm { copy(description = value) } },
                label = { Text("Descriere") },
                minLines = 3,
                modifier = Modifier.fillMaxWidth()
            )
            FormTextField("URL imagine (optional)", form.imageUrl) { value ->
                petViewModel.updateForm { copy(imageUrl = value) }
            }
            FormTextField("Telefon contact", form.contactPhone) { value ->
                petViewModel.updateForm { copy(contactPhone = value) }
            }
            FormTextField("Recompensa (optional)", form.reward) { value ->
                petViewModel.updateForm { copy(reward = value) }
            }
            if (state.errorMessage != null) {
                Text(
                    text = state.errorMessage,
                    color = MaterialTheme.colorScheme.error
                )
            }
            Button(
                onClick = { petViewModel.savePet(onSaved) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Salveaza anunt")
            }
        }
    }
}

@Composable
private fun ChoiceRow(
    options: List<String>,
    selected: String,
    labelFor: (String) -> String,
    onSelected: (String) -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        options.forEach { option ->
            FilterChip(
                selected = selected == option,
                onClick = { onSelected(option) },
                label = { Text(labelFor(option)) }
            )
        }
    }
}

@Composable
private fun FormTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
}
