package com.atech.note.ui.screen.notes

import com.atech.note.data.database.model.Note

sealed class NoteScreenEvent {
    data class AddToStart(val note: Note) : NoteScreenEvent()
    data class ArchiveNote(val note: Note) : NoteScreenEvent()
    data class DeleteNote(val note: Note) : NoteScreenEvent()

    data object RestoreNote : NoteScreenEvent()
}