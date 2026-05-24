package com.laurentiu.lostpaws.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.laurentiu.lostpaws.data.repository.RemotePetImage
import com.laurentiu.lostpaws.data.repository.RemotePetRepository
import kotlinx.coroutines.launch

data class RemotePetsUiState(
    val isLoading: Boolean = false,
    val images: List<RemotePetImage> = emptyList(),
    val errorMessage: String? = null
)

class RemotePetsViewModel(private val remotePetRepository: RemotePetRepository) : ViewModel() {
    var uiState by mutableStateOf(RemotePetsUiState())
        private set

    fun loadRemotePets() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorMessage = null)
            val result = remotePetRepository.fetchRemotePetImages()
            uiState = uiState.copy(
                isLoading = false,
                images = result.images,
                errorMessage = result.errorMessage
            )
        }
    }
}
