package com.ad_coding.noteappcourse.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "multimedia",
    foreignKeys = [
        ForeignKey(
            entity = NoteEntity::class,
            parentColumns = ["id"], // Especifica la columna primaria de NoteEntity
            childColumns = ["idNota"], // Especifica la columna que será foránea en MultimediaEntity
            onDelete = ForeignKey.CASCADE // Elimina las multimedia asociadas cuando se elimina una nota
        )
    ],
    indices = [Index(value = ["idNota"])] // Agrega un índice para optimizar consultas por idNota
)
data class MultimediaEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null, // ID autogenerado para MultimediaEntity
    val idNota: Int, // Referencia a la nota relacionada
    val uri: String, // URI del archivo multimedia
    val tipo: String // Puede ser "image", "video", "audio", etc.
)

// Función de extensión para convertir una lista de MultimediaEntity a una lista de URIs
fun List<MultimediaEntity>.toUriList(): List<String> = this.map { it.uri }

// Función para convertir una lista de URIs a una lista de MultimediaEntity, asociadas a una nota específica
fun List<String>.toMultimediaEntities(idNota: Int): List<MultimediaEntity> = this.map { uri ->
    MultimediaEntity(
        idNota = idNota,
        uri = uri,
        tipo = when {
            uri.endsWith(".jpg", true) || uri.endsWith(".png", true) || uri.endsWith(".jpeg", true) -> "image"
            uri.endsWith(".mp4", true) || uri.endsWith(".avi", true) || uri.endsWith(".mkv", true) -> "video"
            uri.endsWith(".mp3", true) || uri.endsWith(".wav", true) -> "audio"
            else -> "unknown"
        }
    )
}
