package com.atech.note.module

import android.content.Context
import androidx.room.Room
import com.atech.note.data.database.NoteDatabase
import com.atech.note.utils.RootScope
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {


    @Provides
    @Singleton
    fun provideNoteDao(
        @ApplicationContext context: Context,
        callback: NoteDatabase.NoteDatabaseCallback
    ): NoteDatabase =
        Room.databaseBuilder(
            context,
            NoteDatabase::class.java,
            NoteDatabase.DATABASE_NAME
        )
            .addCallback(callback)
            .fallbackToDestructiveMigration()
            .build()


    @Provides
    @Singleton
    fun provideDao(
        database: NoteDatabase
    ) = database.dao


    @Provides
    @RootScope
    @Singleton
    fun provideCoroutineScope() = CoroutineScope(SupervisorJob())
}