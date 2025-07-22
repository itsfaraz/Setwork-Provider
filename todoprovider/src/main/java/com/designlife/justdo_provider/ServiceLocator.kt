package com.designlife.justdo_provider

import android.content.Context
import com.designlife.justdo_provider.data.dao.TodoDao
import com.designlife.justdo_provider.domain.respository.TodoRepository

object ServiceLocator {
    private var todoRepository: TodoRepository? = null
    private var todoDao : TodoDao? = null
    fun provideTodoRepository(context : Context) : TodoRepository {
        return todoRepository ?: createTodoRepository(context)
    }

    fun setTodoDao(todoDao: TodoDao){
        this.todoDao = todoDao
    }

    private fun createTodoRepository(context: Context): TodoRepository {
        todoRepository = TodoRepository(todoDao!!)
        return todoRepository!!
    }
}