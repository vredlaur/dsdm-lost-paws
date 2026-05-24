package com.laurentiu.lostpaws

import android.app.Application
import com.laurentiu.lostpaws.data.local.AppDatabase
import com.laurentiu.lostpaws.data.remote.RetrofitProvider
import com.laurentiu.lostpaws.data.repository.AuthRepository
import com.laurentiu.lostpaws.data.repository.PetRepository
import com.laurentiu.lostpaws.data.repository.RemotePetRepository
import com.laurentiu.lostpaws.data.session.SessionManager

class LostPawsApplication : Application() {
    val database: AppDatabase by lazy { AppDatabase.getInstance(this) }
    val sessionManager: SessionManager by lazy { SessionManager(this) }
    val authRepository: AuthRepository by lazy {
        AuthRepository(database.userDao(), sessionManager)
    }
    val petRepository: PetRepository by lazy {
        PetRepository(database.petDao())
    }
    val remotePetRepository: RemotePetRepository by lazy {
        RemotePetRepository(
            RetrofitProvider.dogApiService,
            RetrofitProvider.catApiService
        )
    }
}
