package com.example.myapplication.ui.todos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.myapplication.generated.resources.*
import com.example.myapplication.ui.components.PaperSheet
import com.example.myapplication.ui.components.ProgressHeader
import com.example.myapplication.ui.components.TodoItem
import com.example.myapplication.ui.theme.LocalPaper
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

private enum class Filter { ALL, ACTIVE, DONE }

@Composable
fun TodoScreen(
    darkTheme: Boolean,
    onDarkThemeChange: (Boolean) -> Unit,
    onShowOnboarding: () -> Unit,
    viewModel: TodoViewModel = koinViewModel()
) {
    val paper = LocalPaper.current
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
    val addTask = { viewModel.add(newTask); newTask = "" }

    Box(
        Modifier.fillMaxSize().background(paper.desk).safeDrawingPadding().padding(12.dp)
    ) {
        PaperSheet(Modifier.fillMaxSize()) {
            ProgressHeader(
                done = state.todos.count { it.completed },
                total = state.todos.size,
                darkTheme = darkTheme,
                onDarkThemeChange = onDarkThemeChange,
                onShowOnboarding = onShowOnboarding
            )

            Row(Modifier.padding(vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Filter.entries.forEach { f ->
                    FilterChip(
                        selected = filter == f,
                        onClick = { filter = f },
                        label = { Text(labels.getValue(f)) },
                        colors = FilterChipDefaults.filterChipColors(
                            labelColor = paper.ink,
                            selectedContainerColor = paper.rule.copy(alpha = 0.6f),
                            selectedLabelColor = paper.ink
                        )
                    )
                }
            }

            Box(Modifier.weight(1f).fillMaxWidth()) {
                when {
                    state.isLoading -> CircularProgressIndicator(Modifier.align(Alignment.Center), color = paper.ink)
                    state.hasError -> Column(
                        Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(stringResource(Res.string.error_loading), color = paper.ink)
                        Spacer(Modifier.height(12.dp))
                        Button(
                            onClick = viewModel::load,
                            colors = ButtonDefaults.buttonColors(containerColor = paper.ink, contentColor = paper.paper)
                        ) { Text(stringResource(Res.string.retry)) }
                    }
                    visible.isEmpty() -> Text(
                        stringResource(Res.string.empty),
                        Modifier.align(Alignment.Center),
                        color = paper.ink
                    )
                    else -> LazyColumn(
                        Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(visible, key = { it.id }) { todo ->
                            TodoItem(
                                todo = todo,
                                onToggle = { viewModel.toggle(todo.id) },
                                onDelete = { viewModel.delete(todo.id) },
                                modifier = Modifier.animateItem()
                            )
                        }
                    }
                }
            }

            Row(Modifier.padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                TextField(
                    value = newTask,
                    onValueChange = { newTask = it },
                    placeholder = { Text(stringResource(Res.string.add_hint)) },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { addTask() }),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = paper.ink,
                        unfocusedTextColor = paper.ink,
                        focusedIndicatorColor = paper.ink,
                        unfocusedIndicatorColor = paper.rule,
                        cursorColor = paper.ink
                    )
                )
                TextButton(onClick = { addTask() }) {
                    Text(stringResource(Res.string.add), color = paper.ink)
                }
            }
        }
    }
}