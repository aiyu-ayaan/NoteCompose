package com.atech.note.ui.screen.notes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atech.note.data.database.model.Note
import com.atech.note.data.use_case.NotesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val notesUseCase: NotesUseCase
) : ViewModel() {
    private val _notes = mutableStateOf<List<Note>>(emptyList())
    val notes: State<List<Note>> get() = _notes

    private var _job: Job? = null

    init {
        getNotes()
    }


    private fun getNotes() {
        _job?.cancel()
        _job = notesUseCase.getNotes()
            .onEach {
                _notes.value = it
            }.launchIn(viewModelScope)
    }
}