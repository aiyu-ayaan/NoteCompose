package com.atech.note.ui.screen.details

import androidx.compose.ui.focus.FocusState

sealed class DetailScreenEvent {
    data class EnteredTitle(val value: String) : DetailScreenEvent()
    data class ChangeTitleFocus(val focus: FocusState) : DetailScreenEvent()
    data class EnteredBody(val value: String) : DetailScreenEvent()
    data class ChangeBodyFocus(val focus: FocusState) : DetailScreenEvent()

    data class ChangeTopBarState(val state: Pair<Boolean, Boolean>) : DetailScreenEvent()

    data class DeleteNote(val action: () -> Unit) : DetailScreenEvent()
    data class SaveNote(val action: () -> Unit) : DetailScreenEvent()
}