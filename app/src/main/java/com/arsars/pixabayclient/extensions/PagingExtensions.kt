package com.arsars.pixabayclient.extensions

import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems

val <T : Any> LazyPagingItems<T>.noItems
    get() = loadState.append.endOfPaginationReached && itemCount == 0 || loadState.refresh is LoadState.Error && itemCount == 0