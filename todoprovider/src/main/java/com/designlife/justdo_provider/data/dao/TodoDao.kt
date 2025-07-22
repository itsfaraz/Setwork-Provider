package com.designlife.justdo_provider.data.dao


import android.database.Cursor
import com.designlife.justdo_provider.data.Todo

interface TodoDao {
    fun insertTodo(todo : Todo) : Long
    fun getAllTodo() : Cursor
    fun deleteTodo(id : Long) : Int
    fun updateTodo(todo : Todo) : Int
}