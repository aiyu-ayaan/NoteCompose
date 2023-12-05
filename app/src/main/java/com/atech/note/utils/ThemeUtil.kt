package com.atech.note.utils

import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.Keep
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoMode
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.ui.graphics.vector.ImageVector

enum class ThemeType(val value: Pair<String, ImageVector>) {
    LIGHT("Light" to Icons.Default.LightMode),
    DARK("Dark" to Icons.Default.DarkMode),
    SYSTEM("System" to Icons.Default.AutoMode)
}


@Keep
data class ThemePref(
    val themeType: ThemeType = ThemeType.SYSTEM,
    val dynamicColor: Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
)

fun saveTheme(pref: SharedPreferences, themePref: ThemePref) {
    pref.edit().apply {
        putString("themeType", themePref.themeType.name)
        putBoolean("dynamicColor", themePref.dynamicColor)
    }.apply()

}

fun getTheme(pref: SharedPreferences): ThemePref {
    val themeType = pref.getString("themeType", ThemeType.SYSTEM.name)
    val dynamicColor =
        pref.getBoolean("dynamicColor", Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
    return ThemePref(ThemeType.valueOf(themeType!!), dynamicColor)
}