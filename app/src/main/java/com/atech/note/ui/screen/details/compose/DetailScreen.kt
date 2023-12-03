package com.atech.note.ui.screen.details.compose

import androidx.activity.compose.BackHandler
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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.atech.note.ui.screen.details.DetailScreenEvent
import com.atech.note.ui.screen.details.DetailViewModel
import com.atech.note.ui.screen.details.TextFieldState
import com.atech.note.ui.theme.captionColor
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    viewModel: DetailViewModel = hiltViewModel(),
    navController: NavController = rememberNavController(),
) {
    val scrollState = rememberScrollState()
    val scrollBarBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val title = viewModel.title.value
    val body = viewModel.body.value
    val topAppbarState = viewModel.topBarState.value
    val createOrUpdateAtState = viewModel.createdOrUpdatedAt.value
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { uiEvent ->
            when (uiEvent) {
                is DetailViewModel.UiEvent.ShowSnackBar -> snackBarHostState.showSnackbar(
                    uiEvent.message
                )
            }
        }
    }

    HandleBackPress(title, body, viewModel, navController)
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBarBehavior.nestedScrollConnection),
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(text = viewModel.type.value)
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            handleBackPress(
                                title = title.text,
                                body = body.text,
                                viewModel = viewModel,
                                navController = navController
                            )
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.onEvent(
                                DetailScreenEvent.ChangeTopBarState(
                                    Pair(topAppbarState.first, !topAppbarState.second)
                                )
                            )
                        },
                    ) {
                        Icon(
                            imageVector = if (topAppbarState.second) Icons.Default.Star else Icons.Outlined.StarBorder,
                            contentDescription = "Back",
                        )
                    }
                    IconButton(
                        onClick = {
                            viewModel.onEvent(
                                DetailScreenEvent.ChangeTopBarState(
                                    Pair(!topAppbarState.first, topAppbarState.second)
                                )
                            )
                        },
                    ) {
                        Icon(
                            imageVector = if (topAppbarState.first) Icons.Default.Archive else Icons.Outlined.Archive,
                            contentDescription = "Back",
                        )
                    }
                    if (viewModel.type == DetailViewModel.Type.EDIT) IconButton(onClick = {
                        viewModel.onEvent(DetailScreenEvent.DeleteNote(action = { navController.navigateUp() }))
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Delete, contentDescription = "Delete"
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
                    text = createOrUpdateAtState,
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
                text = title.text,
                hint = title.hint,
                singleLine = false,
                isHintVisible = title.isHintVisible,
                onValueChange = { title ->
                    viewModel.onEvent(DetailScreenEvent.EnteredTitle(title))
                },
                onFocusChange = { focusState ->
                    viewModel.onEvent(
                        DetailScreenEvent.ChangeTitleFocus(focusState)
                    )
                },
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth(),
                textStyle = MaterialTheme.typography.titleLarge
            )

            EditTextField(
                text = body.text,
                hint = body.hint,
                singleLine = false,
                isHintVisible = body.isHintVisible,
                onValueChange = { body ->
                    viewModel.onEvent(DetailScreenEvent.EnteredBody(body))
                },
                onFocusChange = { focusState ->
                    viewModel.onEvent(
                        DetailScreenEvent.ChangeBodyFocus(focusState)
                    )
                },
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth(),
                textStyle = MaterialTheme.typography.bodyMedium
            )

        }
    }
}

@Composable
private fun HandleBackPress(
    title: TextFieldState,
    body: TextFieldState,
    viewModel: DetailViewModel,
    navController: NavController
) {
    BackHandler {
        handleBackPress(
            title = title.text,
            body = body.text,
            viewModel = viewModel,
            navController = navController
        )
    }
}

private fun handleBackPress(
    title: String, body: String, viewModel: DetailViewModel, navController: NavController
) {
    if (title.isNotEmpty() || body.isNotEmpty()) viewModel.onEvent(DetailScreenEvent.SaveNote(action = { navController.navigateUp() }))
    else navController.navigateUp()
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
            modifier = Modifier.onFocusChanged(onFocusChange),
            value = text,
            onValueChange = onValueChange,
            textStyle = textStyle.copy(
                color = MaterialTheme.colorScheme.onSurface,
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            singleLine = singleLine
        )
        if (isHintVisible) Text(
            text = hint,
            style = textStyle,
            color = MaterialTheme.colorScheme.captionColor
        )
    }
}


@Composable
@Preview(
    showBackground = true,

    )
fun DetailScreenPreview() {
    DetailScreen()
}
