package com.nammapustaka.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class Book(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val author: String,
    val category: String,       // Story, Science, History, etc.
    val summaryKannada: String = "",
    val summary: String = "",
    val coverImageUri: String = "",
    val qrCode: String = "",
    val totalPages: Int = 0,
    val isAvailable: Boolean = true,
    val addedDate: Long = System.currentTimeMillis()
)
