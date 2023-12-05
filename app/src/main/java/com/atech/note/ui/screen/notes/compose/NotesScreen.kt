package com.atech.note.ui.screen.notes.compose

import androidx.annotation.StringRes
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.InvertColors
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material.icons.twotone.Archive
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material.icons.twotone.Unarchive
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.atech.note.R
import com.atech.note.data.database.model.Note
import com.atech.note.ui.screen.notes.NoteScreenEvent
import com.atech.note.ui.screen.notes.NoteViewModel
import com.atech.note.ui.theme.NoteComposeTheme
import com.atech.note.ui.theme.SwipeGreen
import com.atech.note.ui.theme.SwipeRed
import com.atech.note.ui.theme.borderColor
import com.atech.note.ui.theme.captionColor
import com.atech.note.ui.theme.grid_0_5
import com.atech.note.ui.theme.grid_1
import com.atech.note.ui.theme.grid_2
import com.atech.note.utils.Navigation
import kotlinx.coroutines.launch
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox


@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class, ExperimentalFoundationApi::class
)
@Composable
fun NotesScreen(
    modifier: Modifier = Modifier,
    viewModel: NoteViewModel = hiltViewModel(),
    navController: NavController = rememberNavController()
) {
    val scrollBarBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    val notes = viewModel.notes.value
    val context = LocalContext.current
    Scaffold(modifier = modifier
        .fillMaxSize()
        .nestedScroll(scrollBarBehavior.nestedScrollConnection),
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name)
                    )
                },
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                scrollBehavior = scrollBarBehavior,
            )
        },
        bottomBar = {
            BottomAppBar(
                actions = {
                    IconButton(onClick = {
                        navController.navigate(Navigation.ArchiveScreen.route)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Archive,
                            contentDescription = stringResource(
                                id = R.string.archive
                            )
                        )
                    }
                    IconButton(onClick = {
                        navController.navigate(Navigation.ThemeScreen.route)
                    }) {
                        Icon(
                            imageVector = Icons.Default.InvertColors,
                            contentDescription = stringResource(
                                id = R.string.theme
                            )
                        )
                    }
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            navController.navigate(Navigation.AddEditNoteScreen.route)
                        },
                        containerColor = MaterialTheme.colorScheme.primary,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.surface

                        )
                    }
                },
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary,
            )
        }) {
        if (notes.isEmpty()) {
            EmptyScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = it.calculateTopPadding())
            )
            return@Scaffold
        }
        LazyColumn(
            modifier = Modifier.consumeWindowInsets(it),
            contentPadding = it,
        ) {
            items(items = notes, key = { note -> note.id!! }) { notes ->
                NoteItem(modifier = Modifier
                    .animateItemPlacement(
                        animationSpec = spring(
                            dampingRatio = 2f, stiffness = 600f
                        )
                    ), note = notes, onClick = {
                    navController.navigate(
                        Navigation.AddEditNoteScreen.route + "?noteId=${notes.id}"
                    )
                }, onIconClick = { currentNote ->
                    viewModel.onEvent(NoteScreenEvent.AddToStart(currentNote))
                }, onArchive = { currentNote ->
                    viewModel.onEvent(
                        NoteScreenEvent.ArchiveNote(
                            currentNote
                        )
                    )
                }, onDelete = { currentNote ->
                    viewModel.onEvent(NoteScreenEvent.DeleteNote(currentNote))
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
                })
            }
        }
    }
}

@Composable
fun NoteItem(
    modifier: Modifier = Modifier,
    note: Note,
    isIconEnable: Boolean = true,
    onClick: (Int) -> Unit = {},
    onIconClick: (Note) -> Unit = {},
    onArchive: (Note) -> Unit = {},
    onDelete: (Note) -> Unit = {}
) {
    val archive = SwipeAction(onSwipe = {
        onArchive(note)
    }, icon = {
        Icon(
            imageVector = if (isIconEnable) Icons.TwoTone.Archive else Icons.TwoTone.Unarchive,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.surface
        )
    }, background = SwipeGreen
    )
    val delete = SwipeAction(onSwipe = {
        onDelete(note)
    }, icon = {
        Icon(
            imageVector = Icons.TwoTone.Delete,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.surface
        )
    }, background = SwipeRed
    )
    SwipeableActionsBox(
        modifier = modifier,
        startActions = listOf(archive),
        endActions = listOf(delete),
        swipeThreshold = 100.dp
    ) {
        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = grid_1, vertical = grid_0_5)
                .clickable {
                    note.id?.let { onClick(it) }
                },
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            border = BorderStroke(
                .5.dp, MaterialTheme.colorScheme.borderColor
            ),
        ) {
            Column(
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = note.title,
                        modifier = Modifier.padding(horizontal = grid_2, vertical = grid_0_5),
                        textAlign = TextAlign.Start,
                        style = MaterialTheme.typography.titleLarge
                    )
                    IconButton(
                        enabled = isIconEnable,
                        onClick = {
                            onIconClick(note)
                        }) {
                        Icon(
                            imageVector = if (note.isStared) Icons.Default.Star else Icons.Outlined.StarBorder,
                            contentDescription = "Star",
                            tint = if (isIconEnable) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.captionColor
                        )
                    }
                }
                Spacer(modifier = Modifier.padding(top = grid_1))
                Text(
                    text = note.body,
                    modifier = Modifier.padding(horizontal = grid_2, vertical = grid_0_5),
                    textAlign = TextAlign.Start,
                    color = MaterialTheme.colorScheme.captionColor,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 3
                )
            }
        }
    }
}

@Composable
fun EmptyScreen(
    modifier: Modifier = Modifier,
    @StringRes textId: Int = R.string.empty_screen
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier.size(200.dp),
            painter = painterResource(id = R.drawable.im_empty),
            contentDescription = stringResource(id = R.string.empty),
        )
        Text(
            text = stringResource(id = textId),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
@Preview(
    showBackground = true
)
fun NotesScreenPreview() {
    NoteComposeTheme {
        Surface {
            EmptyScreen()
        }
    }
}
