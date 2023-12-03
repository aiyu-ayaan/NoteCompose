package com.atech.note.ui.screen.details

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atech.note.data.database.model.Note
import com.atech.note.data.use_case.NotesUseCase
import com.atech.note.utils.getTimeAgo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


private const val TAG = "DetailViewModel"

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val noteUseCase: NotesUseCase, private val state: SavedStateHandle
) : ViewModel() {
    private val _noteId = state.get<Int>("noteId") ?: -1

    private var createdAt: Long = state.get<Long>("createdAt") ?: System.currentTimeMillis()
        set(value) {
            field = value
            state["createdAt"] = value
        }

    var type: Type = Type.ADD


    init {
        if (_noteId != -1) {
            type = Type.EDIT
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
                createdAt = note.created
                _createdOrUpdatedAt.value =
                    if (note.updated != null) "Edited ${note.updated.getTimeAgo()}"
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

    /**
     * first -> isArchived
     * second -> isStared
     */
    val topBarState: State<Pair<Boolean, Boolean>> get() = _topBarState

    private val _createdOrUpdatedAt = mutableStateOf("Created ${createdAt.getTimeAgo()}")
    val createdOrUpdatedAt: State<String> get() = _createdOrUpdatedAt


    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()


    fun onEvent(event: DetailScreenEvent) {
        when (event) {
            is DetailScreenEvent.EnteredTitle -> _title.value = _title.value.copy(
                text = event.value
            )

            is DetailScreenEvent.ChangeTitleFocus -> _title.value = _title.value.copy(
                isHintVisible = !event.focus.isFocused && _title.value.text.isBlank()
            )

            is DetailScreenEvent.EnteredBody -> _body.value = _body.value.copy(
                text = event.value
            )

            is DetailScreenEvent.ChangeBodyFocus -> _body.value = _body.value.copy(
                isHintVisible = !event.focus.isFocused && _body.value.text.isBlank()
            )

            is DetailScreenEvent.SaveNote -> saveNote(event.action)
            is DetailScreenEvent.ChangeTopBarState -> _topBarState.value = event.state
            is DetailScreenEvent.DeleteNote -> deleteNote(event.action)
        }
    }


    private fun saveNote(action: () -> Unit) {
        viewModelScope.launch {
            val note = Note(
                title = _title.value.text,
                body = _body.value.text,
                isArchived = _topBarState.value.first,
                isStared = _topBarState.value.second,
                created = createdAt,
                updated = if (type == Type.EDIT) System.currentTimeMillis() else null,
                id = if (_noteId != -1) _noteId else null
            )
            try {
                noteUseCase.insertNote(note = note)
                _eventFlow.emit(UiEvent.ShowSnackBar("Note saved"))
                action.invoke()
            } catch (e: Exception) {
                Log.d(TAG, "saveNote: Called")
                _eventFlow.emit(UiEvent.ShowSnackBar(e.message ?: "Couldn't save note."))
            }
        }
    }

    private fun deleteNote(action: () -> Unit) = viewModelScope.launch {
        if (_noteId == -1) {
            _eventFlow.emit(UiEvent.ShowSnackBar("Something went wrong !!!"))
            return@launch
        }
        noteUseCase.deleteNote(id = _noteId)
        _eventFlow.emit(UiEvent.ShowSnackBar("Note deleted"))
        action.invoke()
    }

    enum class Type(val value: String) {
        ADD("Add"), EDIT("Edit")
    }

    sealed class UiEvent {
        data class ShowSnackBar(val message: String) : UiEvent()
    }
}