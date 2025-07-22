package com.designlife.justdo_provider.domain.provider

import android.content.ContentProvider
import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.designlife.justdo_provider.ServiceLocator
import com.designlife.justdo_provider.data.Todo
import com.designlife.justdo_provider.domain.respository.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class TodoProvider : ContentProvider() {

    private val MATCHER : UriMatcher = UriMatcher(UriMatcher.NO_MATCH)
    private val todoRepostory : TodoRepository by lazy {
        ServiceLocator.provideTodoRepository(context!!)
    }

    companion object{
        public const val AUTHORITY = "com.designlife.justdo_provider.domain.provider"

        public val PATH_ALL_TODO = "ALL_TODO"
        public val PATH_INSERT_TODO = "INSERT_TODO"
        public val PATH_DELETE_TODOS = "DELETE_TODOS"
        public val PATH_UPDATE_TODO = "UPDATE_TODOS"
        public val PATH_PARTIAL_TODO = "PARTIAL_TODOS"

        public val CONTENT_URI_1 : Uri = Uri.parse("content://${AUTHORITY}/${PATH_ALL_TODO}")
        public val CONTENT_URI_2 : Uri = Uri.parse("content://${AUTHORITY}/${PATH_INSERT_TODO}")
        public val CONTENT_URI_3 : Uri = Uri.parse("content://${AUTHORITY}/${PATH_DELETE_TODOS}/#")
        public val CONTENT_URI_4 : Uri = Uri.parse("content://${AUTHORITY}/${PATH_UPDATE_TODO}/#")
        public val CONTENT_URI_5 : Uri = Uri.parse("content://${AUTHORITY}/${PATH_PARTIAL_TODO}/#")

        private val ALL_TODO : Int = 1;
        private val INSERT_TODO : Int = 2;
        private val DELETE_TODO : Int = 3;
        private val UPDATE_TODO : Int = 4;
        private val PARTIAL_TODO : Int = 5;

        private val MIME_TYPE_1 = "${ContentResolver.CURSOR_DIR_BASE_TYPE}/vnd.com.designlife.justdo_provider.domain.provider.alltodo"
        private val MIME_TYPE_2 = "${ContentResolver.CURSOR_ITEM_BASE_TYPE}/vnd.com.designlife.justdo_provider.domain.provider.insert.todo"
        private val MIME_TYPE_3 = "${ContentResolver.CURSOR_ITEM_BASE_TYPE}/vnd.com.designlife.justdo_provider.domain.provider.delete.todo"
        private val MIME_TYPE_4 = "${ContentResolver.CURSOR_ITEM_BASE_TYPE}/vnd.com.designlife.justdo_provider.domain.provider.todo"
        private val MIME_TYPE_5 = "${ContentResolver.CURSOR_DIR_BASE_TYPE}/vnd.com.designlife.justdo_provider.domain.provider.partial"
    }

    init {
        MATCHER.addURI(AUTHORITY, PATH_ALL_TODO, ALL_TODO)
        MATCHER.addURI(AUTHORITY, PATH_INSERT_TODO, INSERT_TODO)
        MATCHER.addURI(AUTHORITY, PATH_UPDATE_TODO, UPDATE_TODO)
        MATCHER.addURI(AUTHORITY, PATH_DELETE_TODOS, DELETE_TODO)
        MATCHER.addURI(AUTHORITY, PATH_PARTIAL_TODO, PARTIAL_TODO)
    }

    override fun onCreate(): Boolean {
        return context != null
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        return when(MATCHER.match(uri)){
            ALL_TODO -> runBlocking(Dispatchers.IO) { todoRepostory?.getAllTodos() }
            PARTIAL_TODO -> null
            else -> null
        }
    }

    override fun getType(uri: Uri): String? {
        return when(MATCHER.match(uri)){
            ALL_TODO -> MIME_TYPE_1
            INSERT_TODO -> MIME_TYPE_2
            DELETE_TODO -> MIME_TYPE_3
            UPDATE_TODO -> MIME_TYPE_4
            PARTIAL_TODO -> MIME_TYPE_5
            else -> null
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return when(MATCHER.match(uri)){
            INSERT_TODO -> {insertTodo(uri,values)}
            else -> null
        }
    }

    private fun insertTodo(uri: Uri, values: ContentValues?): Uri? {
        var id : Long = -1
        todoRepostory?.let { repository ->
            runBlocking {
                values?.let {
                    id = repository.insertTodo(it)
                }
            }
        }
        if (id != -1L)
            return ContentUris.withAppendedId(uri,id)
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return when(MATCHER.match(uri)){
            DELETE_TODO -> { deleteTodo(uri,selection,selectionArgs)}
            else -> -1
        }
    }

    private fun deleteTodo(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val id = ContentUris.parseId(uri)
        var returnId = -1
        runBlocking(Dispatchers.IO) {
            todoRepostory?.let { repository ->
              returnId = repository.deleteTodoById(id)
            }
            context?.contentResolver?.notifyChange(uri,null)
        }
        return returnId;
    }


    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        return when(MATCHER.match(uri)){
            UPDATE_TODO -> {updateTodo(uri,values)}
            else -> -1
        }
    }

    private fun updateTodo(uri: Uri, values: ContentValues?): Int {
        val id = ContentUris.parseId(uri)
        var returnId = -1
        runBlocking(Dispatchers.IO) {
            values?.let {
                returnId = todoRepostory?.updateTodo(Todo.fromContentValue(values).copy(id = id)) ?: -1
            }
        }
        return returnId
    }
}