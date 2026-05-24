package com.laurentiu.lostpaws.data.repository

import com.laurentiu.lostpaws.data.remote.CatApiService
import com.laurentiu.lostpaws.data.remote.DogApiService

data class RemotePetImage(
    val title: String,
    val type: String,
    val imageUrl: String
)

data class RemotePetsResult(
    val images: List<RemotePetImage> = emptyList(),
    val errorMessage: String? = null
)

class RemotePetRepository(
    private val dogApiService: DogApiService,
    private val catApiService: CatApiService
) {
    suspend fun fetchRemotePetImages(): RemotePetsResult {
        return try {
            val dogResponse = dogApiService.getRandomDogImage()
            val dogImage = dogResponse.message
                ?.takeIf { it.startsWith("http", ignoreCase = true) }
                ?.let {
                    RemotePetImage(
                        title = "Imagine caine",
                        type = PetRepository.TYPE_DOG,
                        imageUrl = it
                    )
                }

            val catImages = catApiService.getCatImages()
                .mapNotNull { cat ->
                    val url = cat.url?.takeIf { it.startsWith("http", ignoreCase = true) }
                    url?.let {
                        RemotePetImage(
                            title = "Imagine pisica",
                            type = PetRepository.TYPE_CAT,
                            imageUrl = it
                        )
                    }
                }

            val images = listOfNotNull(dogImage) + catImages
            if (images.isEmpty()) {
                RemotePetsResult(errorMessage = "API-urile nu au returnat imagini valide.")
            } else {
                RemotePetsResult(images = images)
            }
        } catch (_: Exception) {
            RemotePetsResult(errorMessage = "Nu s-au putut incarca imaginile. Verifica internetul si incearca din nou.")
        }
    }
}
