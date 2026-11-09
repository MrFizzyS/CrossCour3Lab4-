package com.example.myapplication.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.myapplication.generated.resources.*
import com.example.myapplication.ui.components.PaperSheet
import com.example.myapplication.ui.theme.LocalPaper
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    onOpenTodos: () -> Unit,
    onSignedOut: () -> Unit,
    viewModel: HomeViewModel = koinViewModel()
) {
    val paper = LocalPaper.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                HomeEffect.NavigateToAuth -> onSignedOut()
            }
        }
    }

    Box(Modifier.fillMaxSize().background(paper.desk).safeDrawingPadding().padding(12.dp)) {
        PaperSheet(Modifier.fillMaxSize()) {
            Spacer(Modifier.height(24.dp))
            Text(
                stringResource(Res.string.home_title),
                style = MaterialTheme.typography.headlineMedium,
                color = paper.ink
            )
            Spacer(Modifier.height(12.dp))
            val name = state.displayName
            if (name != null) {
                Text(
                    stringResource(Res.string.home_greeting, name),
                    style = MaterialTheme.typography.bodyLarge,
                    color = paper.ink
                )
            } else if (state.isLoading) {
                CircularProgressIndicator(color = paper.ink, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
            }
            Spacer(Modifier.weight(1f))
            Button(
                onClick = onOpenTodos,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = paper.ink, contentColor = paper.paper)
            ) { Text(stringResource(Res.string.home_open_tasks)) }
            TextButton(
                onClick = viewModel::onLogout,
                modifier = Modifier.fillMaxWidth()
            ) { Text(stringResource(Res.string.logout), color = paper.ink) }
            Spacer(Modifier.height(16.dp))
        }
    }
}