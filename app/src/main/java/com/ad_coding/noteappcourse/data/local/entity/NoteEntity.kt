package com.ad_coding.noteappcourse.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val title: String,
    val content: String,
    val tipo: String,
    val fecha: String,
    val multimedia: String? // Cambié "foto" a "multimedia"
)
