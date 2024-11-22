package com.ad_coding.noteappcourse.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ad_coding.noteappcourse.data.local.dao.NoteDao
import com.ad_coding.noteappcourse.data.local.entity.NoteEntity
import com.ad_coding.noteappcourse.data.local.entity.MultimediaEntity
import com.ad_coding.noteappcourse.data.local.dao.MultimediaDao

@Database(
    entities = [NoteEntity::class, MultimediaEntity::class],
    version = 2,
    exportSchema = false
)
abstract class NoteDatabase : RoomDatabase() {

    // Proporciona acceso a los DAOs
    abstract fun noteDao(): NoteDao
    abstract fun multimediaDao(): MultimediaDao // Agrega multimediaDao aquí

    companion object {
        const val DATABASE_NAME = "note_database"
    }
}
