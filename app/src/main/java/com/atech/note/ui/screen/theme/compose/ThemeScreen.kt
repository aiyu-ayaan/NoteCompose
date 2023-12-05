package com.atech.note.ui.screen.theme.compose

import android.os.Build
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.FormatPaint
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.atech.note.R
import com.atech.note.ui.screen.ThemeViewModel
import com.atech.note.ui.screen.theme.ThemeEvent
import com.atech.note.ui.theme.NoteComposeTheme
import com.atech.note.ui.theme.captionColor
import com.atech.note.ui.theme.grid_1
import com.atech.note.ui.theme.grid_2
import com.atech.note.utils.ThemeType


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeScreen(
    modifier: Modifier = Modifier,
    viewModel: ThemeViewModel = hiltViewModel(),
    navController: NavController = rememberNavController()
) {
    val uiThemeState = viewModel.uiThemeState.value
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.theme)) },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(id = R.string.back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                    actionIconContentColor = MaterialTheme.colorScheme.primary,
                    navigationIconContentColor = MaterialTheme.colorScheme.primary
                )
            )
        },
    ) {
        Column(
            Modifier.padding(it)

        ) {
            CustomSwitch(res = R.string.dynamic_theme,
                des = R.string.dynamic_theme_des,
                checked = uiThemeState.dynamicColor,
                isEnable = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S,
                onCheckedChange = {
                    viewModel.onEvent(ThemeEvent.ToggleDynamicTheme)
                })
            val currentThemeType = uiThemeState.themeType
            ThemeSwitchPanel(themeType = currentThemeType,
                isSwitchEnable = when (currentThemeType) {
                    ThemeType.SYSTEM -> isSystemInDarkTheme()
                    ThemeType.DARK -> true
                    else -> false
                },
                onSwitchChange = { isEnable ->
                    viewModel.onEvent(
                        ThemeEvent.ToggleTheme(
                            if (isEnable) ThemeType.DARK
                            else ThemeType.LIGHT
                        )
                    )
                },
                onRadioButtonClick = { themeType ->
                    viewModel.onEvent(
                        ThemeEvent.ToggleTheme(
                            themeType
                        )
                    )
                }
            )
        }
    }
}

@Composable
fun CustomSwitch(
    modifier: Modifier = Modifier,
    @StringRes res: Int,
    @StringRes des: Int,
    checked: Boolean = true,
    isEnable: Boolean = true,
    onCheckedChange: (Boolean) -> Unit = {}
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = grid_1)
            .let {
                if (isEnable) it.clickable { onCheckedChange(!checked) }
                else it
            },
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Absolute.SpaceBetween
    ) {
        Icon(
            imageVector = Icons.Outlined.FormatPaint,
            contentDescription = null,
            tint = if (isEnable) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.captionColor
        )
        Column(
            Modifier
                .weight(1f)
                .padding(grid_2),
            horizontalAlignment = androidx.compose.ui.Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = res),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(end = grid_2, bottom = grid_1),
                color = if (isEnable) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.captionColor
            )
            Text(
                text = stringResource(id = des),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.captionColor,
                modifier = Modifier.padding(end = grid_2),
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = modifier,
            enabled = isEnable
        )

    }
}

@Composable
fun ThemeSwitchPanel(
    modifier: Modifier = Modifier,
    themeType: ThemeType = ThemeType.SYSTEM,
    isEnable: Boolean = true,
    isSwitchEnable: Boolean = true,
    onSwitchChange: (Boolean) -> Unit = {},
    onRadioButtonClick: (ThemeType) -> Unit = {}
) {
    var isVisible by rememberSaveable { mutableStateOf(false) }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = grid_1)
            .clickable {
                isVisible = !isVisible
            },
        horizontalAlignment = androidx.compose.ui.Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Absolute.SpaceBetween
        ) {
            Icon(
                imageVector = Icons.Outlined.WbSunny,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Column(
                Modifier
                    .weight(1f)
                    .padding(grid_2),
                horizontalAlignment = androidx.compose.ui.Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.theme),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(end = grid_2, bottom = grid_1),
                    color = if (isEnable) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.captionColor
                )
                Text(
                    text = stringResource(id = R.string.system),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.captionColor,
                    modifier = Modifier.padding(end = grid_2),
                )
            }
            Switch(
                checked = isSwitchEnable,
                onCheckedChange = onSwitchChange,
                modifier = modifier,
                enabled = isEnable
            )
        }
        AnimatedVisibility(
            visible = isVisible,
        ) {
            Divider(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
            )
            Column {
                repeat(ThemeType.entries.size) {
                    ThemeRadioButton(
                        selected = themeType == ThemeType.entries.toTypedArray()[it],
                        onClick = onRadioButtonClick,
                        themeType = ThemeType.entries[it]
                    )
                }
            }
        }
    }
}

@Composable
fun ThemeRadioButton(
    modifier: Modifier = Modifier,
    selected: Boolean = true,
    onClick: (ThemeType) -> Unit = { },
    themeType: ThemeType = ThemeType.SYSTEM,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = grid_1, vertical = grid_2)
            .clickable { onClick(themeType) },
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Absolute.SpaceBetween,
    ) {
        Row {
            Icon(
                imageVector = themeType.value.second,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = themeType.value.first,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(end = grid_2, start = grid_2)
            )
        }
        RadioButton(
            selected = selected, onClick = null
        )
    }
}


@Preview(showBackground = true)
@Composable
fun ThemeScreenPreview() {
    NoteComposeTheme {
        ThemeScreen()
    }
}