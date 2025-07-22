package com.designlife.justdo_provider.domain.respository

import android.content.ContentValues
import android.database.Cursor
import com.designlife.justdo_provider.data.Todo
import com.designlife.justdo_provider.data.dao.TodoDao

class TodoRepository(
    private val todoDao: TodoDao
) {

    suspend fun insertTodo(todo: Todo) : Long{
        return todoDao.insertTodo(todo)
    }

    suspend fun insertTodo(contentValues: ContentValues) : Long{
        return todoDao.insertTodo(Todo.fromContentValue(contentValues))
    }

    suspend fun deleteTodoById(id : Long) : Int{
        return todoDao.deleteTodo(id)
    }

    suspend fun updateTodo(todo : Todo) : Int{
        return todoDao.updateTodo(todo)
    }

    suspend fun getAllTodos() : Cursor{
        return todoDao.getAllTodo()
    }
}