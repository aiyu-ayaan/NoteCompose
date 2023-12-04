package com.atech.note.ui.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
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
import com.atech.note.ui.theme.NoteComposeTheme
import com.atech.note.utils.Navigation
import com.atech.note.utils.setExit
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NoteComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Navigation.NotesScreen.route
                    ) {
                        setExit(
                            route = Navigation.NotesScreen.route,
                        ) {
                            NotesScreen(navController = navController)
                        }
                        setExit(
                            route = Navigation.AddEditNoteScreen.route + "?noteId={noteId}",
                            arguments = listOf(
                                navArgument(
                                    name = "noteId"
                                ) {
                                    type = NavType.IntType
                                    defaultValue = -1
                                }
                            )
                        ) {
                            DetailScreen(
                                navController = navController
                            )
                        }
                        setExit(
                            route = Navigation.ArchiveScreen.route
                        ) {
                            ArchiveScreen(navController = navController)
                        }
                    }
                }
            }
        }
    }
}
