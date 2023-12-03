package com.atech.note.data.use_case

import com.atech.note.data.database.NoteDao
import com.atech.note.data.database.model.Note
import com.atech.note.utils.ValidateException
import javax.inject.Inject

data class NotesUseCase @Inject constructor(
    val getNotes: GetNotes,
    val getArchivedNotes: GetArchivedNotes,
    val getNoteById: GetNoteById,
    val insertNote: InsertNote,
    val deleteNote: DeleteNote
)

data class GetNotes @Inject constructor(
    private val dao: NoteDao
) {
    operator fun invoke() = dao.getAllNotes()
}

data class GetArchivedNotes @Inject constructor(
    private val dao: NoteDao
) {
    operator fun invoke() = dao.getAllArchivedNotes()
}

data class GetNoteById @Inject constructor(
    private val dao: NoteDao
) {
    operator fun invoke(id: Int) = dao.getNoteById(id)
}

data class InsertNote @Inject constructor(
    private val dao: NoteDao
) {
    @Throws(ValidateException::class)
    suspend operator fun invoke(note: Note) {
        if (note.title.isBlank())
            throw ValidateException("Title is empty")
        if (note.body.isBlank())
            throw ValidateException("Body is empty")
        dao.insert(note)
    }
}

data class DeleteNote @Inject constructor(
    private val dao: NoteDao
) {
    suspend operator fun invoke(note: Note) {
        dao.delete(note)
    }
}
