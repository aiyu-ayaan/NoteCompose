package com.atech.note.utils

sealed class Navigation(val route: String) {
    data object NotesScreen : Navigation("notes_screen")
    data object AddEditNoteScreen : Navigation("add_edit_note_screen")
}