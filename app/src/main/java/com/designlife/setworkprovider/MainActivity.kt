package com.designlife.setworkprovider

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.designlife.justdo_provider.ServiceLocator
import com.designlife.justdo_provider.data.Todo
import com.designlife.justdo_provider.data.dao.TodoDao
import com.designlife.justdo_provider.domain.provider.TodoProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private val todoList = mutableListOf<Todo>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val todoDao : TodoDao = DummyData()
        ServiceLocator.setTodoDao(todoDao)
        fetchTodo()
        setContent {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                LazyColumn(Modifier.fillMaxSize(.8F)) {
                    items(todoList.size) { index ->
                        val todo = todoList[index]
                        Text(text = "${todo.title}")
                        Spacer(modifier = Modifier.height(1.dp))
                    }
                }
            }
        }
    }

    private fun insertOneRecord() {
        CoroutineScope(Dispatchers.IO).launch {
            val contentValues = ContentValues()

            contentValues.put(Todo.columnId, 1)
            contentValues.put(Todo.columnTitle, "Mastering Content Provider")
            contentValues.put(Todo.columnColor, "Red")
            contentValues.put(Todo.columnDescription, "Basics of cp")
            contentValues.put(Todo.columnCompleted, true)
            contentResolver.insert(TodoProvider.CONTENT_URI_2, contentValues)
        }
    }

    private fun fetchTodo() {
        try{
            val cursor = contentResolver.query(TodoProvider.CONTENT_URI_1, null, null, null, null)
            Log.i(TAG, "fetchTodo: ${cursor}")
            cursor?.let {
                while (it.moveToNext()) {
                    val id = it.getInt(0)
                    val title = it.getString(1)
                    val color = it.getString(2)
                    val description = it.getString(3)
                    val isCompleted = it.getInt(4)
                    todoList.add(Todo(id.toLong(), title, color, description, isCompleted == 0))
                }
            }
        }catch (e : Exception){
            Log.i(TAG, "fetchTodo: Exception Occurs ${e.message}")
            e.printStackTrace()
        }
    }
}