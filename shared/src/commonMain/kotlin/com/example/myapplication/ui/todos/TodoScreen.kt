package com.example.myapplication.ui.todos

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.data.TodoApi
import com.example.myapplication.generated.resources.*
import com.example.myapplication.ui.components.ProgressHeader
import com.example.myapplication.ui.components.TodoItem
import org.jetbrains.compose.resources.stringResource

private enum class Filter { ALL, ACTIVE, DONE }

@Composable
fun TodoScreen(
    darkTheme: Boolean,
    onDarkThemeChange: (Boolean) -> Unit,
    onShowOnboarding: () -> Unit,
    viewModel: TodoViewModel = viewModel { TodoViewModel(TodoApi()) }
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var newTask by remember { mutableStateOf("") }
    var filter by remember { mutableStateOf(Filter.ALL) }

    val visible = when (filter) {
        Filter.ALL -> state.todos
        Filter.ACTIVE -> state.todos.filter { !it.completed }
        Filter.DONE -> state.todos.filter { it.completed }
    }
    val labels = mapOf(
        Filter.ALL to stringResource(Res.string.filter_all),
        Filter.ACTIVE to stringResource(Res.string.filter_active),
        Filter.DONE to stringResource(Res.string.filter_done)
    )

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0),
        bottomBar = {
            Surface(shadowElevation = 8.dp, color = MaterialTheme.colorScheme.surface) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .imePadding()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = newTask,
                        onValueChange = { newTask = it },
                        label = { Text(stringResource(Res.string.add_hint)) },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(Modifier.width(12.dp))
                    Button(
                        onClick = { viewModel.add(newTask); newTask = "" },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.height(56.dp)
                    ) { Text(stringResource(Res.string.add)) }
                }
            }
        }
    ) { padding ->
        Column(Modifier.padding(padding)) {
            ProgressHeader(
                done = state.todos.count { it.completed },
                total = state.todos.size,
                darkTheme = darkTheme,
                onDarkThemeChange = onDarkThemeChange,
                onShowOnboarding = onShowOnboarding
            )

            Row(
                Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Filter.entries.forEach { f ->
                    FilterChip(
                        selected = filter == f,
                        onClick = { filter = f },
                        label = { Text(labels.getValue(f)) },
                        shape = RoundedCornerShape(50)
                    )
                }
            }

            when {
                state.isLoading -> Box(Modifier.fillMaxSize(), Alignment.Center) {
                    CircularProgressIndicator()
                }
                state.hasError -> Column(
                    Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(stringResource(Res.string.error_loading))
                    Spacer(Modifier.height(12.dp))
                    Button(onClick = viewModel::load) { Text(stringResource(Res.string.retry)) }
                }
                visible.isEmpty() -> Box(Modifier.fillMaxSize(), Alignment.Center) {
                    Text("🗒️  " + stringResource(Res.string.empty))
                }
                else -> LazyColumn(
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(visible, key = { it.id }) { todo ->
                        TodoItem(
                            todo = todo,
                            onToggle = { viewModel.toggle(todo.id) },
                            onDelete = { viewModel.delete(todo.id) }
                        )
                    }
                }
            }
        }
    }
}