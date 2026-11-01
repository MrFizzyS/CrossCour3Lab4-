package com.example.myapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.Todo
import com.example.myapplication.generated.resources.*
import com.example.myapplication.ui.theme.LocalPaper
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoItem(
    todo: Todo,
    onToggle: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val paper = LocalPaper.current
    val state = rememberSwipeToDismissBoxState()

    LaunchedEffect(state.currentValue) {
        if (state.currentValue != SwipeToDismissBoxValue.Settled) onDelete()
    }

    SwipeToDismissBox(
        state = state,
        modifier = modifier,
        backgroundContent = {
            val direction = state.dismissDirection
            // Підказку показуємо лише під час свайпу
            if (direction != SwipeToDismissBoxValue.Settled) {
                Box(
                    Modifier.fillMaxSize().padding(horizontal = 8.dp),
                    contentAlignment = if (direction == SwipeToDismissBoxValue.StartToEnd)
                        Alignment.CenterStart else Alignment.CenterEnd
                ) {
                    Text(
                        stringResource(Res.string.throw_away),
                        style = MaterialTheme.typography.labelLarge,
                        color = paper.margin
                    )
                }
            }
        }
    ) {
        Column(
            Modifier
                .graphicsLayer {
                    val direction = state.dismissDirection
                    // У стані спокою progress = 1, тому обнуляємо його вручну
                    val p = if (direction == SwipeToDismissBoxValue.Settled) 0f else state.progress
                    val dir = if (direction == SwipeToDismissBoxValue.StartToEnd) 1f else -1f
                    rotationZ = dir * p * 10f
                    val s = 1f - 0.2f * p
                    scaleX = s
                    scaleY = s
                    alpha = 1f - 0.5f * p
                }
                .fillMaxWidth()
                .background(paper.paper)
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onToggle)
                    .heightIn(min = 56.dp)
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    Modifier.size(22.dp).border(1.5.dp, paper.ink, RoundedCornerShape(3.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    if (todo.completed) Text("✓", color = paper.ink)
                }
                Spacer(Modifier.width(14.dp))
                Text(
                    todo.todo,
                    modifier = Modifier.weight(1f).alpha(if (todo.completed) 0.55f else 1f),
                    style = MaterialTheme.typography.bodyLarge,
                    color = paper.ink,
                    textDecoration = if (todo.completed) TextDecoration.LineThrough else null
                )
                // Явна кнопка видалення (зручно мишкою на web і desktop)
                TextButton(onClick = onDelete) {
                    Text("✕", color = paper.ink.copy(alpha = 0.6f))
                }
            }
            HorizontalDivider(color = paper.rule)
        }
    }
}