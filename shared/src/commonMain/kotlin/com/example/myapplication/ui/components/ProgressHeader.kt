package com.example.myapplication.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.myapplication.generated.resources.*
import com.example.myapplication.ui.theme.LocalPaper
import org.jetbrains.compose.resources.stringResource

@Composable
fun ProgressHeader(
    done: Int,
    total: Int,
    darkTheme: Boolean,
    onDarkThemeChange: (Boolean) -> Unit,
    onShowOnboarding: () -> Unit
) {
    val paper = LocalPaper.current
    val target = if (total == 0) 0f else done / total.toFloat()
    val progress by animateFloatAsState(target)

    Column(Modifier.fillMaxWidth().padding(top = 20.dp, bottom = 8.dp)) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                stringResource(Res.string.todos_title),
                style = MaterialTheme.typography.headlineMedium,
                color = paper.ink
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("🌙")
                Spacer(Modifier.width(8.dp))
                Switch(checked = darkTheme, onCheckedChange = onDarkThemeChange)
            }
        }
        Text(
            stringResource(Res.string.completed_of, done, total),
            style = MaterialTheme.typography.bodyLarge,
            color = paper.ink.copy(alpha = 0.75f)
        )
        Spacer(Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(50)),
            color = paper.ink,
            trackColor = paper.rule
        )
        TextButton(onClick = onShowOnboarding) {
            Text(stringResource(Res.string.test_onboarding), color = paper.ink)
        }
    }
}