package com.atech.note.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes_table")
data class Note(
    val title: String,
    val body: String,
    val created: Long = System.currentTimeMillis(),
    val updated: Long? = null,
    val isStared: Boolean = false,
    val isArchived: Boolean = false,
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null
)