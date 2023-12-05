package com.atech.note.ui.screen

import android.content.SharedPreferences
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.atech.note.ui.screen.theme.ThemeEvent
import com.atech.note.utils.ThemePref
import com.atech.note.utils.getTheme
import com.atech.note.utils.saveTheme
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val pref: SharedPreferences
) : ViewModel() {
    private val _uiThemeState = mutableStateOf(
        ThemePref()
    )
    val uiThemeState: State<ThemePref> get() = _uiThemeState

    init {
        _uiThemeState.value = getUiTheme()
    }

    private fun getUiTheme(): ThemePref =
        getTheme(pref)

    fun onEvent(event: ThemeEvent) {
        when (event) {
            ThemeEvent.ToggleDynamicTheme -> {
                _uiThemeState.value = _uiThemeState.value.copy(
                    dynamicColor = !_uiThemeState.value.dynamicColor
                )
                saveTheme(
                    pref = pref,
                    themePref = _uiThemeState.value
                )
            }

            is ThemeEvent.ToggleTheme -> {
                _uiThemeState.value = _uiThemeState.value.copy(
                    themeType = event.themeType
                )
                saveTheme(
                    pref = pref,
                    themePref = _uiThemeState.value
                )
            }
        }
    }

}