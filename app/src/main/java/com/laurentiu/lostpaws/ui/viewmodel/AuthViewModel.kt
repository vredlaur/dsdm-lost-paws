package com.laurentiu.lostpaws.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.laurentiu.lostpaws.data.repository.AuthRepository
import kotlinx.coroutines.launch

data class AuthUiState(
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {
    var uiState by mutableStateOf(AuthUiState())
        private set

    fun updateFullName(value: String) {
        uiState = uiState.copy(fullName = value, errorMessage = null)
    }

    fun updateEmail(value: String) {
        uiState = uiState.copy(email = value, errorMessage = null)
    }

    fun updatePassword(value: String) {
        uiState = uiState.copy(password = value, errorMessage = null)
    }

    fun updateConfirmPassword(value: String) {
        uiState = uiState.copy(confirmPassword = value, errorMessage = null)
    }

    fun login(onSuccess: () -> Unit) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorMessage = null)
            val result = authRepository.login(uiState.email, uiState.password)
            uiState = uiState.copy(isLoading = false, errorMessage = result.message)
            if (result.success) {
                clearPasswordFields()
                onSuccess()
            }
        }
    }

    fun register(onSuccess: () -> Unit) {
        if (uiState.password != uiState.confirmPassword) {
            uiState = uiState.copy(errorMessage = "Parolele nu coincid.")
            return
        }

        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorMessage = null)
            val result = authRepository.register(uiState.fullName, uiState.email, uiState.password)
            uiState = uiState.copy(isLoading = false, errorMessage = result.message)
            if (result.success) {
                clearPasswordFields()
                onSuccess()
            }
        }
    }

    fun logout() {
        authRepository.logout()
        uiState = AuthUiState()
    }

    private fun clearPasswordFields() {
        uiState = uiState.copy(password = "", confirmPassword = "")
    }
}
