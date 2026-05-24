package com.laurentiu.lostpaws.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.laurentiu.lostpaws.LostPawsApplication

class LostPawsViewModelFactory(
    private val application: LostPawsApplication
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) ->
                AuthViewModel(application.authRepository) as T

            modelClass.isAssignableFrom(PetViewModel::class.java) ->
                PetViewModel(application.petRepository, application.sessionManager) as T

            modelClass.isAssignableFrom(RemotePetsViewModel::class.java) ->
                RemotePetsViewModel(application.remotePetRepository) as T

            else -> throw IllegalArgumentException("ViewModel necunoscut: ${modelClass.name}")
        }
    }
}
