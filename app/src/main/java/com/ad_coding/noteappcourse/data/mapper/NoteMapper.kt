package com.ad_coding.noteappcourse.data.mapper

import com.ad_coding.noteappcourse.data.local.entity.NoteEntity
import com.ad_coding.noteappcourse.domain.model.Note
import com.ad_coding.noteappcourse.data.local.entity.MultimediaEntity

// Convertir NoteEntity a Note (modelo del dominio)
fun NoteEntity.asExternalModel(multimediaEntities: List<MultimediaEntity>? = null): Note {
    // Aquí obtenemos todas las URIs de multimedia (fotos, audios, videos)
    val multimediaUris = multimediaEntities?.map { it.uri } ?: emptyList()
    return Note(
        id = this.id,
        title = this.title,
        content = this.content,
        tipo = this.tipo,
        fecha = this.fecha,
        multimedia = multimediaUris  // Asignamos las URIs a la lista multimedia
    )
}

// Convertir Note a NoteEntity (entidad de la base de datos)
fun Note.toEntity(): NoteEntity = NoteEntity(
    id = this.id,
    title = this.title,
    content = this.content,
    tipo = this.tipo,
    fecha = this.fecha,
    multimedia = this.multimedia.joinToString(",") // Si multimedia es una lista de URIs, conviértela en un String (o adapta según el tipo que necesites)
)


