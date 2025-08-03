package com.example.todolist

data class Task(
    var id: Int = 0,
    var title: String,
    var description: String = "",
    var priority: Int = 1,
    var isCompleted: Boolean = false
)
