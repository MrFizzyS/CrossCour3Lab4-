package com.example.myapplication.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.LocalPaper

@Composable
fun PaperSheet(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val paper = LocalPaper.current
    Surface(
        modifier = modifier,
        color = paper.paper,
        shape = RoundedCornerShape(4.dp),
        shadowElevation = 10.dp
    ) {
        Box {
            Canvas(Modifier.matchParentSize()) {
                val marginX = 56.dp.toPx()
                drawLine(
                    color = paper.margin,
                    start = Offset(marginX, 0f),
                    end = Offset(marginX, size.height),
                    strokeWidth = 1.5.dp.toPx()
                )
                listOf(0.18f, 0.5f, 0.82f).forEach { f ->
                    drawCircle(paper.desk, 7.dp.toPx(), Offset(24.dp.toPx(), size.height * f))
                }
            }
            Column(
                modifier = Modifier.fillMaxWidth().padding(start = 68.dp, end = 16.dp),
                content = content
            )
        }
    }
}