package com.laurentiu.lostpaws.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.laurentiu.lostpaws.data.local.entity.PetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PetDao {
    @Query("SELECT * FROM pet_announcements ORDER BY createdAt DESC")
    fun observeAllPets(): Flow<List<PetEntity>>

    @Query("SELECT * FROM pet_announcements WHERE status = :status ORDER BY createdAt DESC")
    fun observePetsByStatus(status: String): Flow<List<PetEntity>>

    @Query("SELECT * FROM pet_announcements WHERE id = :id LIMIT 1")
    suspend fun getPetById(id: Long): PetEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPet(pet: PetEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPets(pets: List<PetEntity>)

    @Update
    suspend fun updatePet(pet: PetEntity)

    @Delete
    suspend fun deletePet(pet: PetEntity)

    @Query("UPDATE pet_announcements SET isFavorite = :isFavorite WHERE id = :petId")
    suspend fun updateFavorite(petId: Long, isFavorite: Boolean)

    @Query("UPDATE pet_announcements SET isResolved = :isResolved WHERE id = :petId")
    suspend fun updateResolved(petId: Long, isResolved: Boolean)

    @Query("SELECT COUNT(*) FROM pet_announcements")
    suspend fun countPets(): Int

    @Query("SELECT COUNT(*) FROM pet_announcements")
    fun observeTotalCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM pet_announcements WHERE status = :status")
    fun observeStatusCount(status: String): Flow<Int>

    @Query("SELECT COUNT(*) FROM pet_announcements WHERE isFavorite = 1")
    fun observeFavoriteCount(): Flow<Int>
}
