package com.example.inventory.data

import kotlinx.coroutines.flow.Flow

interface NotesRepository {
    /**
     * Retrieve all the notes from the data source.
     */
    fun getAllNotesStream(): Flow<List<Note>>

    /**
     * Retrieve a specific note from the data source.
     */
    fun getNoteStream(id: Int): Flow<Note?>

    /**
     * Insert a new note in the data source.
     */
    suspend fun insertNote(note: Note)

    /**
     * Delete a note from the data source.
     */
    suspend fun deleteNote(note: Note)

    /**
     * Update a note in the data source.
     */
    suspend fun updateNote(note: Note)
}
