package com.ad_coding.noteappcourse.data.repository

import com.ad_coding.noteappcourse.data.local.dao.NoteDao
import com.ad_coding.noteappcourse.domain.model.Note
import com.ad_coding.noteappcourse.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import com.ad_coding.noteappcourse.data.mapper.toEntity
import com.ad_coding.noteappcourse.data.mapper.asExternalModel
import kotlinx.coroutines.flow.map
import com.ad_coding.noteappcourse.data.local.dao.MultimediaDao
import com.ad_coding.noteappcourse.data.local.entity.toMultimediaEntities
import com.ad_coding.noteappcourse.data.local.entity.toUriList
import android.util.Log
import com.ad_coding.noteappcourse.data.local.entity.MultimediaEntity

class NoteRepositoryImpl(
    private val noteDao: NoteDao,
    private val multimediaDao: MultimediaDao
) : NoteRepository {

    override suspend fun insertNote(note: Note) {
        // Guardar la nota y obtener el ID generado
        val noteId = noteDao.insertNote(note.toEntity()).toInt()

        // Guardar multimedia asociada, si existe
        note.multimedia?.let { uris ->
            Log.d("NoteRepositoryImpl", "Guardando multimedia para la nota $noteId: $uris")
            saveMultimediaForNote(noteId, uris)
        }
    }

    override suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note.toEntity())
        note.id?.let { multimediaDao.deleteAllForNota(it) }
    }

    override suspend fun updateNote(note: Note) {
        noteDao.updateNote(note.toEntity())
        // Actualizar multimedia asociada
        note.id?.let {
            multimediaDao.deleteAllForNota(it)
            note.multimedia?.let { uris ->
                saveMultimediaForNote(it, uris)
            }
        }
    }

    override fun getAllNotes(): Flow<List<Note>> {
        return noteDao.getAllNotes().map { entities ->
            entities.map { it.asExternalModel() }
        }
    }

    override suspend fun getNoteById(id: Int): Note? {
        return noteDao.getNoteById(id)?.asExternalModel()?.also { note ->
<<<<<<< HEAD
            note.multimedia = getMultimediaForNote(id) // Aquí se asegura que se carguen los multimedia de la base de datos
        }
    }

=======
            note.multimedia = getMultimediaForNote(id) // Cambié el nombre de la función aquí
        }
    }

    // Corregido el nombre de la función para que coincida con la interfaz
>>>>>>> 1ddbf024e2fdd11cf8892dad9507b98a17433e36
    override suspend fun getMultimediaForNote(idNota: Int): List<String> {
        val multimedia = multimediaDao.getMultimediaForNota(idNota).toUriList()
        Log.d("NoteRepositoryImpl", "Multimedia loaded for note $idNota: $multimedia")
        return multimedia
    }
<<<<<<< HEAD

=======
>>>>>>> 1ddbf024e2fdd11cf8892dad9507b98a17433e36

    private suspend fun saveMultimediaForNote(idNota: Int, uris: List<String>) {
        multimediaDao.deleteAllForNota(idNota)

        // Procesar cada URI y clasificarlo
        val multimediaEntities = uris.map { uri ->
            val type = classifyMultimediaType(uri)  // Clasificación del tipo de multimedia
            Log.d("NoteRepositoryImpl", "URI: $uri clasificado como tipo: $type")
            MultimediaEntity(uri = uri, tipo = type, idNota = idNota) // Aseguramos que el constructor reciba 'idNota' y 'tipo'
        }
<<<<<<< HEAD
        Log.d("NoteRepositoryImpl", "Guardando multimedia para la nota $idNota: $uris") // Aquí añades un Log adicional
=======
>>>>>>> 1ddbf024e2fdd11cf8892dad9507b98a17433e36

        multimediaEntities.forEach {
            multimediaDao.insert(it)
        }
    }

<<<<<<< HEAD


=======
>>>>>>> 1ddbf024e2fdd11cf8892dad9507b98a17433e36
    // Función para clasificar el tipo de multimedia (imagen, video, audio, etc.)
    fun classifyMultimediaType(uri: String): String {
        Log.d("classifyMultimediaType", "Evaluando URI: $uri")

        // Verificamos las extensiones directamente, además de los URI
        return when {
            uri.endsWith(".jpg", true) || uri.endsWith(".png", true) || uri.endsWith(".jpeg", true) -> "image"
            uri.endsWith(".mp4", true) || uri.endsWith(".mkv", true) || uri.endsWith(".avi", true) -> "video"
<<<<<<< HEAD
            uri.endsWith(".mp3", true) || uri.endsWith(".wav", true)
                    || uri.endsWith(".m4a", true) || uri.endsWith(".3gp", true) -> "audio"
=======
            uri.endsWith(".mp3", true) || uri.endsWith(".wav", true) || uri.endsWith(".aac", true) || uri.endsWith(".3gp", true) -> "audio"
>>>>>>> 1ddbf024e2fdd11cf8892dad9507b98a17433e36
            uri.startsWith("content://media/external/images") -> "image"
            uri.startsWith("content://media/external/video") -> "video"
            uri.startsWith("content://media/external/audio") -> "audio"
            else -> "unknown"
        }
    }
<<<<<<< HEAD

=======
>>>>>>> 1ddbf024e2fdd11cf8892dad9507b98a17433e36
}
