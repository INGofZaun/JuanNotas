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

class NoteRepositoryImpl(
    private val noteDao: NoteDao,
    private val multimediaDao: MultimediaDao
) : NoteRepository {

    override suspend fun insertNote(note: Note) {
        // Guardar la nota y obtener el ID generado
        val noteId = noteDao.insertNote(note.toEntity()).toInt()

        // Guardar multimedia asociada, si existe
        note.multimedia?.let { uris ->
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
            note.multimedia = getMultimediaForNote(id) // Agregar multimedia al modelo
        }
    }

//    override suspend fun getMultimediaForNote(idNota: Int): List<String> {
//        return multimediaDao.getMultimediaForNota(idNota).toUriList()
//    }
override suspend fun getMultimediaForNote(idNota: Int): List<String> {
    val multimedia = multimediaDao.getMultimediaForNota(idNota).toUriList()
    Log.d("NoteRepositoryImpl", "Multimedia loaded for note $idNota: $multimedia")
    return multimedia
}

    private suspend fun saveMultimediaForNote(idNota: Int, uris: List<String>) {
        multimediaDao.deleteAllForNota(idNota)
        val multimediaEntities = uris.toMultimediaEntities(idNota)
        multimediaEntities.forEach {
            multimediaDao.insert(it)
        }
    }
}
