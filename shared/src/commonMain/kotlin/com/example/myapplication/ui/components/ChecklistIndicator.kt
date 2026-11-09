package com.example.myapplication.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.LocalPaper

@Composable
fun ChecklistIndicator(pageCount: Int, currentPage: Int, modifier: Modifier = Modifier) {
    val paper = LocalPaper.current
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount) { index ->
            val done = index < currentPage
            val current = index == currentPage
            val fill by animateColorAsState(if (done) paper.ink else Color.Transparent)
            val size by animateDpAsState(if (current) 26.dp else 20.dp)
            Box(
                Modifier
                    .size(size)
                    .background(fill, RoundedCornerShape(4.dp))
                    .border(if (current) 2.5.dp else 1.5.dp, paper.ink, RoundedCornerShape(4.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (done) Text("✓", color = paper.paper, fontSize = 13.sp)
            }
        }
    }
}