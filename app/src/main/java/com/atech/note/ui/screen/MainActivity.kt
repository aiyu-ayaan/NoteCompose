package com.atech.note.ui.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.atech.note.ui.screen.archive.compose.ArchiveScreen
import com.atech.note.ui.screen.details.compose.DetailScreen
import com.atech.note.ui.screen.notes.compose.NotesScreen
import com.atech.note.ui.screen.theme.compose.ThemeScreen
import com.atech.note.ui.theme.NoteComposeTheme
import com.atech.note.utils.Navigation
import com.atech.note.utils.ThemeType
import com.atech.note.utils.animatedCompose
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val themeViewModel: ThemeViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NoteComposeTheme(
                dynamicColor = themeViewModel.uiThemeState.value.dynamicColor,
                darkTheme = when (themeViewModel.uiThemeState.value.themeType) {
                    ThemeType.LIGHT -> false
                    ThemeType.DARK -> true
                    ThemeType.SYSTEM -> isSystemInDarkTheme()
                }
            ) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.surface
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Navigation.NotesScreen.route
                    ) {
                        animatedCompose(
                            route = Navigation.NotesScreen.route,
                        ) {
                            NotesScreen(navController = navController)
                        }
                        animatedCompose(route = Navigation.AddEditNoteScreen.route + "?noteId={noteId}",
                            arguments = listOf(navArgument(
                                name = "noteId"
                            ) {
                                type = NavType.IntType
                                defaultValue = -1
                            })) {
                            DetailScreen(
                                navController = navController
                            )
                        }
                        animatedCompose(
                            route = Navigation.ArchiveScreen.route
                        ) {
                            ArchiveScreen(navController = navController)
                        }
                        animatedCompose(
                            route = Navigation.ThemeScreen.route
                        ) {
                            ThemeScreen(
                                navController = navController, viewModel = themeViewModel
                            )
                        }

                    }
                }
            }
        }
    }
}
