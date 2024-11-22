package com.ad_coding.noteappcourse.data.di

import android.content.Context
import androidx.room.Room
import com.ad_coding.noteappcourse.data.local.NoteDatabase
import com.ad_coding.noteappcourse.domain.repository.NoteRepository
import com.ad_coding.noteappcourse.data.repository.NoteRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.ad_coding.noteappcourse.data.local.dao.MultimediaDao

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(@ApplicationContext context: Context): NoteDatabase =
        Room.databaseBuilder(
            context,
            NoteDatabase::class.java,
            NoteDatabase.DATABASE_NAME
        )
            // Se borra la base de datos y se recrea en caso de que se actualice la versión
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideNoteRepository(database: NoteDatabase): NoteRepository =
        NoteRepositoryImpl(
            noteDao = database.noteDao(),
            multimediaDao = database.multimediaDao()  // Inyecta MultimediaDao aquí
        )
}
