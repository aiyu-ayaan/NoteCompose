package com.atech.note.ui.screen.notes.compose

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
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
import com.atech.note.ui.screen.notes.NoteViewModel
import com.atech.note.ui.theme.NoteComposeTheme
import com.atech.note.ui.theme.borderColor
import com.atech.note.ui.theme.captionColor
import com.atech.note.ui.theme.grid_0_5
import com.atech.note.ui.theme.grid_1
import com.atech.note.ui.theme.grid_2
import com.atech.note.utils.Navigation

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun NotesScreen(
    modifier: Modifier = Modifier,
    viewModel: NoteViewModel = hiltViewModel(),
    navController: NavController = rememberNavController()
) {
    val scrollBarBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val notes = viewModel.notes.value
    Scaffold(modifier = modifier
        .fillMaxSize()
        .nestedScroll(scrollBarBehavior.nestedScrollConnection),
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
                    IconButton(onClick = { /*TODO navigate to archive*/ }) {
                        Icon(
                            imageVector = Icons.Default.Archive,
                            contentDescription = "Archive"
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
            items(notes) { notes ->
                NoteItem(
                    note = notes,
                    onClick = {
                        navController.navigate(
                            Navigation.AddEditNoteScreen.route +
                                    "?noteId=${notes.id}"
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun NoteItem(
    modifier: Modifier = Modifier, note: Note, onClick: (Int) -> Unit = {}
) {
    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = grid_1, vertical = grid_0_5)
            .clickable {
                note.id?.let { onClick(it) }
            },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            .5.dp,
            MaterialTheme.colorScheme.borderColor
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
                    modifier = Modifier
                        .padding(horizontal = grid_2, vertical = grid_0_5),
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.titleLarge
                )
                IconButton(onClick = {
                    /*TODO Handle star*/
                }) {
                    Icon(
                        imageVector = if (note.isStared) Icons.Default.Star else Icons.Outlined.StarBorder,
                        contentDescription = "Star",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Spacer(modifier = Modifier.padding(top = grid_1))
            Text(
                text = note.body,
                modifier = Modifier
                    .padding(horizontal = grid_2, vertical = grid_0_5),
                textAlign = TextAlign.Start,
                color = MaterialTheme.colorScheme.captionColor,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3
            )
        }
    }
}

@Composable
fun EmptyScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier.size(200.dp),
            painter = painterResource(id = R.drawable.im_empty),
            contentDescription = "Empty"
        )
        Text(
            text = "Press + to add note",
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
