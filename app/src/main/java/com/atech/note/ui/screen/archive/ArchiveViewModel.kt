package com.atech.note.ui.screen.archive

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atech.note.data.database.model.Note
import com.atech.note.data.use_case.NotesUseCase
import com.atech.note.ui.screen.notes.NoteScreenEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArchiveViewModel @Inject constructor(
    private val noteUseCases: NotesUseCase
) : ViewModel() {
    private var recentDeletedNote: Note? = null
    private var job: Job? = null

    private val _archiveNotes = mutableStateOf<List<Note>>(emptyList())
    val archiveNotes: State<List<Note>> get() = _archiveNotes


    init {
        getNote()
    }

    fun onEvent(event: NoteScreenEvent) {
        when (event) {
            is NoteScreenEvent.ArchiveNote -> {
                viewModelScope.launch {
                    delay(300)
                    noteUseCases.insertNote(
                        event.note.copy(
                            isArchived = !event.note.isArchived
                        )
                    )
                    getNote()
                }
            }

            is NoteScreenEvent.DeleteNote -> {
                viewModelScope.launch {
                    noteUseCases.deleteNote(
                        event.note
                    )
                    recentDeletedNote = event.note
                    getNote()
                }
            }

            NoteScreenEvent.RestoreNote -> {
                viewModelScope.launch {
                    noteUseCases.insertNote(
                        recentDeletedNote ?: return@launch
                    )
                    recentDeletedNote = null
                    getNote()
                }
            }

            is NoteScreenEvent.AddToStart -> Unit
        }
    }

    private fun getNote() {
        job?.cancel()
        job = viewModelScope.launch {
            noteUseCases.getArchivedNotes()
                .onEach {
                    _archiveNotes.value = it
                }
                .launchIn(viewModelScope)
        }
    }
}