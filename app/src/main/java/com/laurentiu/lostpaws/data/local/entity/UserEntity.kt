package com.laurentiu.lostpaws.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "users",
    indices = [Index(value = ["email"], unique = true)]
)
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val fullName: String,
    @ColumnInfo(collate = ColumnInfo.NOCASE)
    val email: String,
    val password: String,
    val createdAt: Long
)
