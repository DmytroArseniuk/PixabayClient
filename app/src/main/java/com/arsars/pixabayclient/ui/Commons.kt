package com.arsars.pixabayclient.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
@OptIn(ExperimentalLayoutApi::class)
fun TagsRow(tags: List<String>) {
    FlowRow(Modifier.padding(top = 12.dp)) {
        tags.forEach {
            Text(
                text = it,
                modifier = Modifier
                    .border(BorderStroke(1.dp, Color.Gray), RoundedCornerShape(50))
                    .padding(vertical = 3.dp, horizontal = 6.dp)
            )
            Spacer(Modifier.width(10.dp))
        }
    }
}