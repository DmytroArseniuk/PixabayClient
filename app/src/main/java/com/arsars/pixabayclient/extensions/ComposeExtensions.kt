package com.arsars.pixabayclient.extensions

import android.content.res.Resources
import androidx.compose.ui.unit.dp

val Int.toDp get() = (this / Resources.getSystem().displayMetrics.density).toInt().dp

val Int.toPx get() = (this * Resources.getSystem().displayMetrics.density).toInt()