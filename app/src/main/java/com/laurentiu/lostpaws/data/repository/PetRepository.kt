package com.laurentiu.lostpaws.data.repository

import com.laurentiu.lostpaws.data.local.dao.PetDao
import com.laurentiu.lostpaws.data.local.entity.PetEntity
import kotlinx.coroutines.flow.Flow

class PetRepository(private val petDao: PetDao) {
    val pets: Flow<List<PetEntity>> = petDao.observeAllPets()
    val totalCount: Flow<Int> = petDao.observeTotalCount()
    val lostCount: Flow<Int> = petDao.observeStatusCount(STATUS_LOST)
    val foundCount: Flow<Int> = petDao.observeStatusCount(STATUS_FOUND)
    val favoriteCount: Flow<Int> = petDao.observeFavoriteCount()

    fun petsByFilter(status: String): Flow<List<PetEntity>> {
        return if (status == FILTER_ALL) petDao.observeAllPets() else petDao.observePetsByStatus(status)
    }

    suspend fun getPetById(id: Long): PetEntity? = petDao.getPetById(id)

    suspend fun addPetAnnouncement(pet: PetEntity): Long = petDao.insertPet(pet)

    suspend fun updateFavorite(petId: Long, isFavorite: Boolean) {
        petDao.updateFavorite(petId, isFavorite)
    }

    suspend fun markResolved(petId: Long, isResolved: Boolean) {
        petDao.updateResolved(petId, isResolved)
    }

    suspend fun deletePet(pet: PetEntity) {
        petDao.deletePet(pet)
    }

    suspend fun seedDemoPetsIfNeeded() {
        if (petDao.countPets() > 0) return

        val now = System.currentTimeMillis()
        petDao.insertPets(
            listOf(
                PetEntity(
                    ownerUserId = 0L,
                    name = "Bella",
                    type = TYPE_DOG,
                    status = STATUS_LOST,
                    breed = "Labrador",
                    color = "Auriu",
                    gender = "Femela",
                    city = "Bucuresti",
                    area = "Titan",
                    description = "Catelusa prietenoasa, poarta zgarda rosie.",
                    imageUrl = "",
                    contactPhone = "0712 000 111",
                    reward = "200 lei",
                    isFavorite = false,
                    isResolved = false,
                    createdAt = now - 1_000L
                ),
                PetEntity(
                    ownerUserId = 0L,
                    name = "Mimi",
                    type = TYPE_CAT,
                    status = STATUS_FOUND,
                    breed = "Europeana",
                    color = "Gri cu alb",
                    gender = "Femela",
                    city = "Bucuresti",
                    area = "Militari",
                    description = "Pisica gasita langa metrou, foarte blanda.",
                    imageUrl = "",
                    contactPhone = "0722 000 222",
                    reward = "-",
                    isFavorite = false,
                    isResolved = false,
                    createdAt = now - 2_000L
                ),
                PetEntity(
                    ownerUserId = 0L,
                    name = "Rex",
                    type = TYPE_DOG,
                    status = STATUS_LOST,
                    breed = "Ciobanesc",
                    color = "Negru cu maro",
                    gender = "Mascul",
                    city = "Brasov",
                    area = "Centrul Vechi",
                    description = "Raspunde la numele Rex si este speriat de trafic.",
                    imageUrl = "",
                    contactPhone = "0733 000 333",
                    reward = "300 lei",
                    isFavorite = false,
                    isResolved = false,
                    createdAt = now - 3_000L
                ),
                PetEntity(
                    ownerUserId = 0L,
                    name = "Luna",
                    type = TYPE_CAT,
                    status = STATUS_FOUND,
                    breed = "Siameza",
                    color = "Crem",
                    gender = "Femela",
                    city = "Cluj-Napoca",
                    area = "Marasti",
                    description = "Pisica tanara gasita in scara blocului.",
                    imageUrl = "",
                    contactPhone = "0744 000 444",
                    reward = "-",
                    isFavorite = false,
                    isResolved = false,
                    createdAt = now - 4_000L
                )
            )
        )
    }

    companion object {
        const val FILTER_ALL = "ALL"
        const val STATUS_LOST = "LOST"
        const val STATUS_FOUND = "FOUND"
        const val TYPE_DOG = "Dog"
        const val TYPE_CAT = "Cat"
        const val TYPE_OTHER = "Other"
    }
}
