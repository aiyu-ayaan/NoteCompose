package com.atech.note.ui.screen.archive.compose

import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.atech.note.R
import com.atech.note.ui.screen.archive.ArchiveViewModel
import com.atech.note.ui.screen.notes.NoteScreenEvent
import com.atech.note.ui.screen.notes.compose.EmptyScreen
import com.atech.note.ui.screen.notes.compose.NoteItem
import com.atech.note.ui.theme.NoteComposeTheme
import com.atech.note.utils.Navigation
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)
@Composable
fun ArchiveScreen(
    modifier: Modifier = Modifier,
    viewModel: ArchiveViewModel = hiltViewModel(),
    navController: NavController = rememberNavController()
) {
    val scrollBarBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val notes = viewModel.archiveNotes.value
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBarBehavior.nestedScrollConnection),
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.archive)) },
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
        }
    ) {
        if (notes.isEmpty()) {
            EmptyScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = it.calculateTopPadding()),
                textId = R.string.empty_archive,
            )
            return@Scaffold
        }
        LazyColumn(
            modifier = Modifier.consumeWindowInsets(it),
            contentPadding = it
        ) {
            items(
                items = notes, key = { note -> note.id!! }) { note ->
                NoteItem(
                    modifier = Modifier.animateItemPlacement(
                        animationSpec = spring(
                            dampingRatio = 2f, stiffness = 600f
                        )
                    ),
                    note = note,
                    isIconEnable = false,
                    onClick = {
                        navController.navigate(
                            Navigation.AddEditNoteScreen.route + "?noteId=${note.id}"
                        )
                    },
                    onArchive = {
                        viewModel.onEvent(NoteScreenEvent.ArchiveNote(note))
                    },
                    onDelete = {
                        viewModel.onEvent(NoteScreenEvent.DeleteNote(note))
                        scope.launch {
                            snackBarHostState.showSnackbar(
                                message = context.getString(R.string.deleted_message),
                                actionLabel = context.getString(R.string.undo)
                            ).let { result ->
                                if (result == SnackbarResult.ActionPerformed) {
                                    viewModel.onEvent(NoteScreenEvent.RestoreNote)
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ArchiveScreenPreview() {
    NoteComposeTheme {
        ArchiveScreen()
    }
}