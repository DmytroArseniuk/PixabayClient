package com.arsars.pixabayclient.ui.screens.photo.search

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.arsars.pixabayclient.R
import com.arsars.pixabayclient.data.source.local.photos.Photo
import com.arsars.pixabayclient.extensions.toDp
import com.arsars.pixabayclient.ui.TagsRow
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoDetailsScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    viewModel: PhotoDetailsViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()


    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, stringResource(R.string.back_button))
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
            )
        }
    ) {
        Column(
            Modifier
                .padding(it)
                .verticalScroll(rememberScrollState())
        ) {
            val errorText = stringResource(R.string.some_error_alert)
            uiState.value.photo?.let {
                PhotoWithDetails(it) {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = errorText
                        )
                    }
                }
            }
        }
    }

}

@Composable
private fun PhotoWithDetails(photo: Photo, showImageLadingError: () -> Unit) {
    var loading by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        var width by remember { mutableStateOf(IntSize.Zero) }
        var height by remember {
            mutableStateOf(0)
        }
        val ratio by remember {
            mutableStateOf(photo.previewHeight.toFloat() / photo.previewWidth)
        }
        height = (width.width * ratio).roundToInt()
        Box(Modifier
            .fillMaxWidth()
            .onGloballyPositioned { coordinates ->
                width = coordinates.size
            }
            .height(height.toDp),
            contentAlignment = Alignment.Center) {
            AsyncImage(
                model = photo.imageURL,
                contentDescription = null,
                onState = {
                    when (it) {
                        is AsyncImagePainter.State.Loading -> loading = true
                        is AsyncImagePainter.State.Success -> loading = false
                        AsyncImagePainter.State.Empty -> {
                            loading = false
                        }
                        is AsyncImagePainter.State.Error -> {
                            loading = false
                            showImageLadingError()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 150.dp),
                contentScale = ContentScale.FillWidth
            )
            if (loading) {
                CircularProgressIndicator()
            }
        }
        Column(Modifier.padding(16.dp)) {
            Row {
                PhotoInfoIcon(
                    painterResource(id = R.drawable.thumb_up_icon),
                    stringResource(R.string.likes_description),
                    "${photo.likes}"
                )
                PhotoInfoIcon(
                    painterResource(id = R.drawable.comment_icon),
                    stringResource(R.string.comments_description),
                    "${photo.comments}"
                )
                PhotoInfoIcon(
                    painterResource(id = R.drawable.download_icon),
                    stringResource(R.string.downloads_description),
                    "${photo.downloads}"
                )
            }
            Text(
                text = photo.userName,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(top = 16.dp)
            )
            TagsRow(photo.tags)
        }

    }
}

@Composable
private fun PhotoInfoIcon(
    painter: Painter,
    description: String,
    text: String
) {
    Row {
        Icon(painter, description, Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text)
        Spacer(modifier = Modifier.width(16.dp))
    }
}