package com.laurentiu.lostpaws.navigation

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val ADD_PET = "addPet"
    const val PET_DETAILS = "petDetails/{petId}"
    const val REMOTE_PETS = "remotePets"
    const val PROFILE = "profile"

    fun petDetails(petId: Long): String = "petDetails/$petId"
}
