package com.ad_coding.noteappcourse.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete
import androidx.room.Update
import com.ad_coding.noteappcourse.data.local.entity.NoteEntity
import com.ad_coding.noteappcourse.data.local.entity.MultimediaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * FROM note")
    fun getAllNotes(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM note WHERE id = :id")
    suspend fun getNoteById(id: Int): NoteEntity?

    @Insert
    suspend fun insertNote(noteEntity: NoteEntity): Long

    @Delete
    suspend fun deleteNote(noteEntity: NoteEntity)

    @Update
    suspend fun updateNote(noteEntity: NoteEntity)

    // Métodos para multimedia
    @Insert
    suspend fun insertMultimedia(multimediaEntity: MultimediaEntity): Long

    @Query("SELECT * FROM multimedia WHERE idNota = :idNota")
    fun getMultimediaByNoteId(idNota: Int): Flow<List<MultimediaEntity>>

    @Delete
    suspend fun deleteMultimedia(multimediaEntity: MultimediaEntity)
}
