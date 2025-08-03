package com.example.todolist

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TaskDatabaseHelper(context: Context) : SQLiteOpenHelper(context, "task_db", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("""
            CREATE TABLE tasks (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT,
                description TEXT,
                priority INTEGER,
                isCompleted INTEGER
            )
        """)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS tasks")
        onCreate(db)
    }

    fun insertTask(task: Task) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("title", task.title)
            put("description", task.description)
            put("priority", task.priority)
            put("isCompleted", if (task.isCompleted) 1 else 0)
        }
        db.insert("tasks", null, values)
    }

    fun getAllTasks(): MutableList<Task> {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM tasks", null)
        val tasks = mutableListOf<Task>()
        if (cursor.moveToFirst()) {
            do {
                tasks.add(
                    Task(
                        id = cursor.getInt(0),
                        title = cursor.getString(1),
                        description = cursor.getString(2),
                        priority = cursor.getInt(3),
                        isCompleted = cursor.getInt(4) == 1
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return tasks
    }

    fun updateTask(task: Task) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("title", task.title)
            put("description", task.description)
            put("priority", task.priority)
            put("isCompleted", if (task.isCompleted) 1 else 0)
        }
        db.update("tasks", values, "id=?", arrayOf(task.id.toString()))
    }

    fun deleteTask(id: Int) {
        val db = writableDatabase
        db.delete("tasks", "id=?", arrayOf(id.toString()))
    }
}
