package com.laurentiu.lostpaws.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.laurentiu.lostpaws.data.local.entity.PetEntity
import com.laurentiu.lostpaws.data.repository.PetRepository
import com.laurentiu.lostpaws.data.session.SessionManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

data class PetUiState(
    val pets: List<PetEntity> = emptyList(),
    val selectedFilter: String = PetRepository.FILTER_ALL,
    val searchQuery: String = "",
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

data class PetFormState(
    val name: String = "",
    val type: String = PetRepository.TYPE_DOG,
    val status: String = PetRepository.STATUS_LOST,
    val breed: String = "",
    val color: String = "",
    val gender: String = "",
    val city: String = "",
    val area: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val contactPhone: String = "",
    val reward: String = ""
)

data class PetStatsState(
    val total: Int = 0,
    val lost: Int = 0,
    val found: Int = 0,
    val favorite: Int = 0
)

class PetViewModel(
    private val petRepository: PetRepository,
    private val sessionManager: SessionManager
) : ViewModel() {
    var uiState by mutableStateOf(
        PetUiState(selectedFilter = sessionManager.getSelectedStatusFilter())
    )
        private set

    var formState by mutableStateOf(PetFormState())
        private set

    var selectedPet by mutableStateOf<PetEntity?>(null)
        private set

    var statsState by mutableStateOf(PetStatsState())
        private set

    private var sourcePets: List<PetEntity> = emptyList()
    private var petsJob: Job? = null

    init {
        viewModelScope.launch {
            petRepository.seedDemoPetsIfNeeded()
        }
        observePets()
        observeStats()
    }

    fun updateFilter(status: String) {
        uiState = uiState.copy(selectedFilter = status, isLoading = true, errorMessage = null)
        sessionManager.saveSelectedStatusFilter(status)
        observePets()
    }

    fun updateSearch(value: String) {
        uiState = uiState.copy(searchQuery = value)
        applySearchFilter()
    }

    fun clearMessages() {
        uiState = uiState.copy(errorMessage = null, successMessage = null)
    }

    fun updateForm(update: PetFormState.() -> PetFormState) {
        formState = formState.update()
        clearMessages()
    }

    fun resetForm() {
        formState = PetFormState()
    }

    fun savePet(onSuccess: () -> Unit) {
        val validationError = validateForm()
        if (validationError != null) {
            uiState = uiState.copy(errorMessage = validationError)
            return
        }

        viewModelScope.launch {
            val pet = PetEntity(
                ownerUserId = sessionManager.getUserId(),
                name = formState.name.trim(),
                type = formState.type,
                status = formState.status,
                breed = formState.breed.trim().ifBlank { "-" },
                color = formState.color.trim().ifBlank { "-" },
                gender = formState.gender.trim().ifBlank { "-" },
                city = formState.city.trim(),
                area = formState.area.trim(),
                description = formState.description.trim(),
                imageUrl = formState.imageUrl.trim(),
                contactPhone = formState.contactPhone.trim(),
                reward = formState.reward.trim().ifBlank { "-" },
                isFavorite = false,
                isResolved = false,
                createdAt = System.currentTimeMillis()
            )
            petRepository.addPetAnnouncement(pet)
            resetForm()
            uiState = uiState.copy(successMessage = "Anuntul a fost salvat.")
            onSuccess()
        }
    }

    fun loadPet(petId: Long) {
        viewModelScope.launch {
            selectedPet = petRepository.getPetById(petId)
        }
    }

    fun updateFavorite(pet: PetEntity) {
        viewModelScope.launch {
            petRepository.updateFavorite(pet.id, !pet.isFavorite)
            selectedPet = selectedPet?.takeIf { it.id == pet.id }?.copy(isFavorite = !pet.isFavorite)
        }
    }

    fun markResolved(pet: PetEntity) {
        viewModelScope.launch {
            petRepository.markResolved(pet.id, true)
            selectedPet = selectedPet?.takeIf { it.id == pet.id }?.copy(isResolved = true)
        }
    }

    fun deletePet(pet: PetEntity, onDeleted: () -> Unit) {
        viewModelScope.launch {
            petRepository.deletePet(pet)
            selectedPet = null
            onDeleted()
        }
    }

    fun currentUserId(): Long = sessionManager.getUserId()

    private fun observePets() {
        petsJob?.cancel()
        petsJob = viewModelScope.launch {
            petRepository.petsByFilter(uiState.selectedFilter).collect { pets ->
                sourcePets = pets
                uiState = uiState.copy(isLoading = false)
                applySearchFilter()
            }
        }
    }

    private fun observeStats() {
        viewModelScope.launch {
            combine(
                petRepository.totalCount,
                petRepository.lostCount,
                petRepository.foundCount,
                petRepository.favoriteCount
            ) { total, lost, found, favorite ->
                PetStatsState(total, lost, found, favorite)
            }.collect { stats ->
                statsState = stats
            }
        }
    }

    private fun applySearchFilter() {
        val query = uiState.searchQuery.trim().lowercase()
        val filtered = if (query.isBlank()) {
            sourcePets
        } else {
            sourcePets.filter { pet ->
                listOf(pet.name, pet.type, pet.status, pet.city, pet.area, pet.breed)
                    .any { it.lowercase().contains(query) }
            }
        }
        uiState = uiState.copy(pets = filtered)
    }

    private fun validateForm(): String? {
        return when {
            formState.name.trim().length < 2 -> "Numele animalului este obligatoriu."
            formState.city.trim().length < 2 -> "Orasul este obligatoriu."
            formState.area.trim().length < 2 -> "Zona este obligatorie."
            formState.description.trim().length < 10 -> "Descrierea trebuie sa aiba minimum 10 caractere."
            formState.contactPhone.trim().length < 7 -> "Telefonul de contact este obligatoriu."
            formState.imageUrl.isNotBlank() && !formState.imageUrl.startsWith("http", ignoreCase = true) -> "URL-ul imaginii trebuie sa inceapa cu http."
            else -> null
        }
    }
}
