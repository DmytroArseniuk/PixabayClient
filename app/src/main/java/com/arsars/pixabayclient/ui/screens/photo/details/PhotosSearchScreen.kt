package com.arsars.pixabayclient.ui.screens.photo.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.AsyncImage
import com.arsars.pixabayclient.R
import com.arsars.pixabayclient.data.source.local.photos.Photo
import com.arsars.pixabayclient.extensions.noItems
import com.arsars.pixabayclient.extensions.toDp
import com.arsars.pixabayclient.extensions.toPx
import com.arsars.pixabayclient.ui.*
import com.arsars.pixabayclient.ui.screens.photo.details.PhotosSearchViewModel.UiAction.*
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotosSearchScreen(
    modifier: Modifier = Modifier,
    openDetails: (Photo) -> Unit,
    viewModel: PhotosSearchViewModel = hiltViewModel(),
) {
    val localFocusManager = LocalFocusManager.current
    val state = viewModel.uiState.collectAsState()
    val photos: LazyPagingItems<Photo> = viewModel.photos.collectAsLazyPagingItems()

    var photoToOpen by remember { mutableStateOf<Photo?>(null) }
    photoToOpen?.let {
        val dismissDialog = { photoToOpen = null }
        OpenDetailsDialog(it, dismissDialog = dismissDialog) {
            openDetails(it)
            dismissDialog()
        }
    }

    val listState: LazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val keyboardState by keyboardAsState()

    if (keyboardState == Keyboard.Closed) {
        localFocusManager.clearFocus()
    }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    SearchInput(
                        state.value.query,
                        viewModel.accept,
                        {
                            coroutineScope.launch {
                                listState.scrollToItem(0)
                            }
                            viewModel.accept(Search)
                        },
                        localFocusManager
                    )
                },
                navigationIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 16.dp, end = 24.dp)
                            .size(24.dp)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
            )
        }
    ) {
        Box(Modifier.padding(it)) {
            val errorText = stringResource(R.string.some_error_alert)
            PhotosListContainer(localFocusManager = localFocusManager,
                listState = listState,
                photos = photos,
                onItemClick = {
                    photoToOpen = it
                },
                notifyError = {
                    scope.launch {
                        snackbarHostState.showSnackbar(message = errorText)
                    }
                }
            )
            if (photos.noItems) {
                EmptyListState {
                    viewModel.accept(Search)
                }
            }
        }
    }
}

@Composable
fun EmptyListState(
    refresh: () -> Unit
) {
    Box(Modifier.fillMaxSize()) {
        Column(
            Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Nothing to show")
            Spacer(modifier = Modifier.height(16.dp))
            Button(refresh) {
                Text(text = "Refresh")
            }
        }
    }
}

@Composable
private fun BoxScope.PhotosListContainer(
    localFocusManager: FocusManager,
    listState: LazyListState,
    photos: LazyPagingItems<Photo>,
    onItemClick: (Photo) -> Unit,
    notifyError: () -> Unit
) {
    PhotosList(localFocusManager, listState, photos) {
        onItemClick(it)
    }
    when (photos.loadState.refresh) {
        is LoadState.Error -> {
            notifyError()
        }
        LoadState.Loading -> {
            LinearProgressIndicator(
                Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopStart)
            )
        }
        is LoadState.NotLoading -> {

        }
    }

    when (photos.loadState.append) {
        is LoadState.Error -> {
            notifyError()
        }
        LoadState.Loading -> {
            LinearProgressIndicator(
                Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
            )
        }
        is LoadState.NotLoading -> {
        }
    }
}

@Composable
private fun PhotosList(
    localFocusManager: FocusManager,
    listState: LazyListState = rememberLazyListState(),
    photos: LazyPagingItems<Photo>,
    onItemClick: (Photo) -> Unit,
) {
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                localFocusManager.clearFocus()
                return Offset.Zero
            }
        }
    }

    var width by remember { mutableStateOf(IntSize.Zero) }
    val cardPadding by remember { mutableStateOf(20) }
    LazyColumn(state = listState, modifier = Modifier
        .nestedScroll(nestedScrollConnection)
        .onGloballyPositioned { coordinates ->
            width = coordinates.size
        }) {
        items(photos, key = { it.id }) { item ->
            item?.let {
                val ratio = it.previewHeight.toFloat() / it.previewWidth
                val heightInPx = ((width.width - (cardPadding.toPx * 2)) * ratio).roundToInt()
                PhotoItem(it, heightInPx.toDp, cardPadding.dp) { onItemClick(it) }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun SearchInput(
    query: String,
    updateQuery: (PhotosSearchViewModel.UiAction) -> Unit,
    search: () -> Unit,
    localFocusManager: FocusManager
) {
    var searchInFocus by remember { mutableStateOf(false) }
    TextField(
        value = query,
        onValueChange = { value ->
            updateQuery(Input(value))
        },
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .onFocusEvent {
                searchInFocus = it.isFocused
            },
        textStyle = TextStyle(fontSize = 18.sp),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions {
            search()
            localFocusManager.clearFocus()
        },
        placeholder = { Text(stringResource(R.string.search)) },
        trailingIcon = {
            if (searchInFocus) {
                IconButton(
                    onClick = { updateQuery(Input("")) },
                    Modifier.padding(start = 24.dp, end = 16.dp)
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = stringResource(R.string.clear_search),
                        modifier = Modifier
                            .size(24.dp)
                    )
                }
            }
        },
        singleLine = true,
        shape = RectangleShape,
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            containerColor = Color.Transparent
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoItem(
    photo: Photo,
    height: Dp,
    horizontalPadding: Dp,
    onClick: () -> Unit
) {

    Card(onClick = onClick, Modifier.padding(horizontalPadding, 10.dp)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            AsyncImage(
                model = photo.previewURL,
                contentDescription = stringResource(R.string.loaded_image),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height),
                contentScale = ContentScale.FillWidth
            )
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(text = photo.userName)
                TagsRow(photo.tags)
            }

        }
    }
}

@Composable
fun OpenDetailsDialog(
    photo: Photo,
    dismissDialog: () -> Unit,
    openDetails: (Photo) -> Unit
) {
    AlertDialog(
        onDismissRequest = {
            dismissDialog()
        },
        title = {
            Text(text = stringResource(R.string.details_dialog_title))
        },
        text = {
            Text(stringResource(R.string.details_dialog_description))
        },
        confirmButton = {
            Button(
                onClick = {
                    openDetails(photo)
                }) {
                Text(stringResource(R.string.details_dialog_positive_action))
            }
        },
        dismissButton = {
            Button({ dismissDialog() }) {
                Text(stringResource(R.string.details_dialog_negative_action))
            }
        }
    )
}