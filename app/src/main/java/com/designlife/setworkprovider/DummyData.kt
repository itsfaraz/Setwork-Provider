package com.designlife.setworkprovider

import android.database.Cursor
import android.database.MatrixCursor
import com.designlife.justdo_provider.data.Todo
import com.designlife.justdo_provider.data.dao.TodoDao

class DummyData : TodoDao{
    override fun insertTodo(todo: Todo): Long {
        return 0L
    }

    override fun getAllTodo(): Cursor {
        val cursor = MatrixCursor(
            arrayOf("id", "title", "color", "description", "completed"),
            1 // initial capacity, you can adjust it based on your needs
        )
        // Add the dummy data to the cursor
        cursor.addRow(
            arrayOf(
                1,
                "Hello World",
                "Green",
                "Bye World",
                1
            )
        )
        return cursor
    }

    override fun deleteTodo(id: Long): Int {
        return 0
    }

    override fun updateTodo(todo: Todo): Int {
        return 0
    }
}