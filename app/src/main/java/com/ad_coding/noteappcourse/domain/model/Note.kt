package com.ad_coding.noteappcourse.domain.model

import androidx.room.*

@Entity(tableName = "note_table")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val title: String = "",
    val content: String = "",
    val tipo: String = "",
    val fecha: String = "",
    // Aquí agregamos un campo para manejar las URIs de los archivos multimedia
    var multimedia: List<String> = emptyList() // Listado de URIs de multimedia
)

@Entity(
    tableName = "multimedia_table",
    foreignKeys = [
        ForeignKey(
            entity = Note::class,
            parentColumns = ["id"],
            childColumns = ["noteId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("noteId")]
)
data class Multimedia(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val uri: String,
    val type: String, // "image" o "video"
    val noteId: Int // Llave foránea que relaciona con Note
)

// Relación 1:N (Nota -> Multimedia)
data class NoteWithMultimedia(
    @Embedded val note: Note,
    @Relation(
        parentColumn = "id",
        entityColumn = "noteId"
    )
    val multimedia: List<Multimedia>
)
