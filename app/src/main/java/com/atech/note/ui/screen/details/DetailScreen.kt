package com.atech.note.ui.screen.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.atech.note.R
import com.atech.note.data.database.model.Note
import com.atech.note.ui.theme.captionColor
import com.atech.note.utils.getTimeAgo
import com.atech.note.utils.noteDemo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
) {
    val scrollState = rememberScrollState()
    val scrollBarBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val note = noteDemo

    Scaffold(
        modifier =
        modifier
            .fillMaxSize()
            .nestedScroll(scrollBarBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Edit")
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                                  navController.navigateUp()
                        },
                    )
                    {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { /*TODO Handle isStared*/ },
                    )
                    {
                        Icon(
                            imageVector =
                            if (note.isStared) Icons.Default.Star else Icons.Outlined.StarBorder,
                            contentDescription = "Back",
                        )
                    }
                    IconButton(
                        onClick = { /*TODO Handle isArchive*/ },
                    )
                    {
                        Icon(
                            imageVector = if (note.isArchived) Icons.Default.Archive else Icons.Outlined.Archive,
                            contentDescription = "Back",
                        )
                    }
                    IconButton(onClick = { /*TODO Handle delete*/ }) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = "Delete"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                    navigationIconContentColor = MaterialTheme.colorScheme.primary,
                    actionIconContentColor = MaterialTheme.colorScheme.primary,
                ),
                scrollBehavior = scrollBarBehavior,
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier.fillMaxWidth()

            ) {
                Text(
                    text = if (note.updated != null) "Last Edit ${note.updated.getTimeAgo()}"
                    else "Created ${note.created.getTimeAgo()}",
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.Center),
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.captionColor,
                    )
                )
            }
        },
    ) {
        Column(
            Modifier
                .padding(it)
                .verticalScroll(scrollState),
        ) {

            EditTextField(
                text = note.title,
                hint = stringResource(id = R.string.enter_title),
                singleLine = false,
                isHintVisible = false,
                onValueChange = { /*TODO Handle title change*/ },
                onFocusChange = { /*TODO Handle title focus*/ },
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth(),
                textStyle = MaterialTheme.typography.titleLarge
            )

            EditTextField(
                text = note.body,
                hint = stringResource(id = R.string.enter_body),
                singleLine = false,
                isHintVisible = false,
                onValueChange = { /*TODO Handle title change*/ },
                onFocusChange = { /*TODO Handle title focus*/ },
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth(),
                textStyle = MaterialTheme.typography.bodyMedium
            )

        }
    }
}

@Composable
fun EditTextField(
    modifier: Modifier = Modifier,
    text: String,
    hint: String,
    isHintVisible: Boolean = true,
    textStyle: TextStyle = TextStyle(),
    singleLine: Boolean,
    onValueChange: (String) -> Unit = {},
    onFocusChange: (FocusState) -> Unit = {}
) {
    Box(modifier) {
        BasicTextField(
            modifier = Modifier
                .onFocusChanged(onFocusChange),
            value = text,
            onValueChange = onValueChange,
            textStyle = textStyle.copy(
                color = MaterialTheme.colorScheme.onSurface
            ),
            singleLine = singleLine
        )
        if (isHintVisible)
            Text(text = hint, style = textStyle, color = textStyle.color.copy(alpha = 0.5f))
    }
}


@Composable
@Preview(
    showBackground = true,

    )
fun DetailScreenPreview() {
    DetailScreen()
}
