package com.example.myapplication.data

import kotlinx.serialization.Serializable

@Serializable
data class Todo(
    val id: Int,
    val todo: String,
    val completed: Boolean,
    val userId: Int = 0
)

@Serializable
data class TodosResponse(
    val todos: List<Todo>,
    val total: Int = 0,
    val skip: Int = 0,
    val limit: Int = 0
)