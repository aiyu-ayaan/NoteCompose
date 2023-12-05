package com.atech.note.ui.screen.theme

import com.atech.note.utils.ThemeType

sealed class ThemeEvent {
    data object ToggleDynamicTheme : ThemeEvent()
    data class ToggleTheme(val themeType: ThemeType) : ThemeEvent()
}