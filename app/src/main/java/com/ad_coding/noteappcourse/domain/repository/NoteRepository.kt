package com.ad_coding.noteappcourse.domain.repository

import com.ad_coding.noteappcourse.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    fun getAllNotes(): Flow<List<Note>>

    suspend fun getNoteById(id: Int): Note?

    suspend fun insertNote(note: Note)
   // suspend fun insertFoto(id:Int,Foto: String)
    //suspend fun getAllfotos():Flow<List<String>>

    suspend fun deleteNote(note: Note)

    suspend fun updateNote(note: Note)
}