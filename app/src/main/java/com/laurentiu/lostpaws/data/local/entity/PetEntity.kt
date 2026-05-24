package com.laurentiu.lostpaws.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pet_announcements")
data class PetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val ownerUserId: Long,
    val name: String,
    val type: String,
    val status: String,
    val breed: String,
    val color: String,
    val gender: String,
    val city: String,
    val area: String,
    val description: String,
    val imageUrl: String,
    val contactPhone: String,
    val reward: String,
    val isFavorite: Boolean,
    val isResolved: Boolean,
    val createdAt: Long
)
