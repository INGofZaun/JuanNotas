package com.ad_coding.noteappcourse.domain.repository

import com.ad_coding.noteappcourse.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    suspend fun insertNote(note: Note)
    suspend fun deleteNote(note: Note)
    suspend fun updateNote(note: Note)
    fun getAllNotes(): Flow<List<Note>>
    suspend fun getNoteById(id: Int): Note?
    suspend fun getMultimediaForNote(idNota: Int): List<String> // Este método está en la interfaz
}
