package com.laurentiu.lostpaws.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.laurentiu.lostpaws.LostPawsApplication
import com.laurentiu.lostpaws.ui.screens.add.AddPetScreen
import com.laurentiu.lostpaws.ui.screens.details.PetDetailsScreen
import com.laurentiu.lostpaws.ui.screens.home.HomeScreen
import com.laurentiu.lostpaws.ui.screens.login.LoginScreen
import com.laurentiu.lostpaws.ui.screens.profile.ProfileScreen
import com.laurentiu.lostpaws.ui.screens.register.RegisterScreen
import com.laurentiu.lostpaws.ui.screens.remote.RemotePetsScreen
import com.laurentiu.lostpaws.ui.viewmodel.AuthViewModel
import com.laurentiu.lostpaws.ui.viewmodel.LostPawsViewModelFactory
import com.laurentiu.lostpaws.ui.viewmodel.PetViewModel
import com.laurentiu.lostpaws.ui.viewmodel.RemotePetsViewModel

@Composable
fun LostPawsNavGraph(
    navController: NavHostController = rememberNavController()
) {
    val application = LocalContext.current.applicationContext as LostPawsApplication
    val factory = LostPawsViewModelFactory(application)
    val authViewModel: AuthViewModel = viewModel(factory = factory)
    val petViewModel: PetViewModel = viewModel(factory = factory)
    val remotePetsViewModel: RemotePetsViewModel = viewModel(factory = factory)
    val startDestination = if (application.sessionManager.isLoggedIn()) Routes.HOME else Routes.LOGIN

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onRegisterClick = { navController.navigate(Routes.REGISTER) }
            )
        }
        composable(Routes.REGISTER) {
            RegisterScreen(
                viewModel = authViewModel,
                onRegisterSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onLoginClick = { navController.popBackStack() }
            )
        }
        composable(Routes.HOME) {
            HomeScreen(
                petViewModel = petViewModel,
                onPetClick = { petId -> navController.navigate(Routes.petDetails(petId)) },
                onAddClick = { navController.navigate(Routes.ADD_PET) },
                onRemoteClick = { navController.navigate(Routes.REMOTE_PETS) },
                onProfileClick = { navController.navigate(Routes.PROFILE) }
            )
        }
        composable(Routes.ADD_PET) {
            AddPetScreen(
                petViewModel = petViewModel,
                onBackClick = { navController.popBackStack() },
                onSaved = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                }
            )
        }
        composable(
            route = Routes.PET_DETAILS,
            arguments = listOf(navArgument("petId") { type = NavType.LongType })
        ) { entry ->
            val petId = entry.arguments?.getLong("petId") ?: 0L
            PetDetailsScreen(
                petId = petId,
                petViewModel = petViewModel,
                onBackClick = { navController.popBackStack() },
                onDeleted = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.REMOTE_PETS) {
            RemotePetsScreen(
                viewModel = remotePetsViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Routes.PROFILE) {
            ProfileScreen(
                sessionManager = application.sessionManager,
                authViewModel = authViewModel,
                statsState = petViewModel.statsState,
                onBackClick = { navController.popBackStack() },
                onLogout = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}
