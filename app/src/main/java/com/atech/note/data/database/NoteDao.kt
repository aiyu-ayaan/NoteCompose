package com.atech.note.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.atech.note.data.model.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * FROM notes_table WHERE isArchived == 0 ORDER BY isStared , created DESC ")
    fun getAllNotes(): Flow<List<Note>>


    @Query("SELECT * FROM notes_table WHERE isArchived == 1 ORDER BY isStared , created DESC ")
    fun getAllArchivedNotes(): Flow<List<Note>>

    @Query("SELECT * FROM notes_table WHERE id = :id")
    fun getNoteById(id: Int): Note?


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note)

    @Delete
    suspend fun delete(note: Note)
}