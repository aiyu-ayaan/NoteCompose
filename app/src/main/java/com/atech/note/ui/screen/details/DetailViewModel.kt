package com.atech.note.ui.screen.details

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atech.note.data.use_case.NotesUseCase
import com.atech.note.utils.getTimeAgo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


private const val TAG = "DetailViewModel"

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val noteUseCase: NotesUseCase, state: SavedStateHandle
) : ViewModel() {
    private val _noteId = state.get<Int>("noteId") ?: -1

    var type: String = "Add"


    init {
        Log.d(TAG, _noteId.toString())
        if (_noteId != -1) {
            type = "Edit"
            viewModelScope.launch {
                val note = noteUseCase.getNoteById(_noteId)
                _title.value = _title.value.copy(
                    text = note!!.title, isHintVisible = false
                )
                _body.value = _body.value.copy(
                    text = note.body, isHintVisible = false
                )
                _topBarState.value = _topBarState.value.copy(
                    first = note.isArchived, second = note.isStared
                )
                _createdOrUpdatedAt.value = if (note.updated != null)
                    "Edited ${note.updated.getTimeAgo()}"
                else "Created ${note.created.getTimeAgo()}"
            }
        }
    }


    private val _title = mutableStateOf(
        TextFieldState(
            hint = "Enter title", isHintVisible = true
        )
    )
    val title: State<TextFieldState> get() = _title

    private val _body = mutableStateOf(
        TextFieldState(
            hint = "Enter content", isHintVisible = true
        )
    )
    val body: State<TextFieldState> get() = _body


    private val _topBarState = mutableStateOf(Pair(false, false))
    val topBarState: State<Pair<Boolean, Boolean>> get() = _topBarState

    private val _createdOrUpdatedAt =
        mutableStateOf("Created ${System.currentTimeMillis().getTimeAgo()}")
    val createdOrUpdatedAt: State<String> get() = _createdOrUpdatedAt
}