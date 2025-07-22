package com.designlife.justdo_provider.data

import android.content.ContentValues

data class Todo(
    val id : Long = 0L,
    val title : String = "",
    val color : String = "",
    val description : String = "",
    val completed : Boolean = false
){
    companion object{
        public const val columnId : String = "id"
        public const val columnTitle : String = "title"
        public const val columnCompleted : String = "completed"
        public const val columnColor : String = "color"
        public const val columnDescription : String = "description"
        fun fromContentValue(contentValues: ContentValues) : Todo{
            var newTodo = Todo()
            if (contentValues.containsKey(Todo.columnId)){
                newTodo = newTodo.copy(id = contentValues.get(Todo.columnId) as Long)
            }
            if (contentValues.containsKey(Todo.columnTitle)){
                newTodo = newTodo.copy(title = contentValues.get(Todo.columnTitle) as String)
            }
            if (contentValues.containsKey(Todo.columnCompleted)){
                newTodo = newTodo.copy(completed = contentValues.get(Todo.columnCompleted) as Boolean)
            }

            if (contentValues.containsKey(Todo.columnColor)){
                newTodo = newTodo.copy(color = contentValues.get(Todo.columnColor) as String)
            }

            if (contentValues.containsKey(Todo.columnDescription)){
                newTodo = newTodo.copy(description = contentValues.get(Todo.columnDescription) as String)
            }

            return newTodo
        }
    }
}
