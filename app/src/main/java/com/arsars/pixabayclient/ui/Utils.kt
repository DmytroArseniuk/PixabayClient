package com.arsars.pixabayclient.ui

import android.content.res.Resources
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems

val Int.toDp get() = (this / Resources.getSystem().displayMetrics.density).toInt().dp

val Int.toPx get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val <T : Any> LazyPagingItems<T>.noItems
    get() = loadState.append.endOfPaginationReached && itemCount == 0 || loadState.refresh is LoadState.Error && itemCount == 0