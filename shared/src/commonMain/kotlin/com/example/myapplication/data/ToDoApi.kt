package com.example.myapplication.data

import co.touchlab.kermit.Logger
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class TodoApi(
    private val client: HttpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }
) {
    private val log = Logger.withTag("TodoApi")

    suspend fun getTodos(): List<Todo> {
        log.i { "Loading todos from API..." }
        val response: TodosResponse = client.get("https://dummyjson.com/todos?limit=20").body()
        log.i { "Loaded ${response.todos.size} todos" }
        return response.todos
    }
}