package com.example.myapplication.ui.todos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.example.myapplication.data.Todo
import com.example.myapplication.data.TodoApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TodoUiState(
    val isLoading: Boolean = true,
    val todos: List<Todo> = emptyList(),
    val hasError: Boolean = false
)

class TodoViewModel(private val api: TodoApi) : ViewModel() {

    private val log = Logger.withTag("TodoViewModel")
    private val _state = MutableStateFlow(TodoUiState())
    val state: StateFlow<TodoUiState> = _state.asStateFlow()

    init { load() }

    fun load() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, hasError = false) }
            try {
                val todos = api.getTodos()
                _state.update { it.copy(isLoading = false, todos = todos) }
            } catch (e: Exception) {
                log.e(e) { "Failed to load todos" }
                _state.update { it.copy(isLoading = false, hasError = true) }
            }
        }
    }

    fun toggle(id: Int) {
        log.d { "Toggle todo $id" }
        _state.update { s ->
            s.copy(todos = s.todos.map { if (it.id == id) it.copy(completed = !it.completed) else it })
        }
    }

    fun add(text: String) {
        if (text.isBlank()) return
        log.d { "Add todo: $text" }
        _state.update { s ->
            val newId = (s.todos.maxOfOrNull { it.id } ?: 0) + 1
            s.copy(todos = listOf(Todo(newId, text.trim(), false)) + s.todos)
        }
    }

    fun delete(id: Int) {
        log.d { "Delete todo $id" }
        _state.update { s -> s.copy(todos = s.todos.filterNot { it.id == id }) }
    }
}