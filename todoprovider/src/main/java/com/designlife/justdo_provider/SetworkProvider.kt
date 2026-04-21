package com.designlife.justdo_provider

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import com.designlife.justdo_provider.data.ProviderTask
import com.designlife.justdo_provider.presentation.AppWidget

class SetworkProvider(
    private val context: Context
) {
    fun addTask(task: ProviderTask){
        try {
            SetworkProvider.setTask(task)
            updateRemoteView()
        }catch (e : Exception){
            e.printStackTrace()
        }
    }
    
    fun removeTask(task: ProviderTask){
        try {
            SetworkProvider.tasks.removeIf { stask -> stask.id == task.id }
            updateRemoteView()
        }catch (e : Exception){
            e.printStackTrace()
        }
    }

    fun addTaskList(tasks: List<ProviderTask>){
        try {
            SetworkProvider.setTasks(tasks)
            updateRemoteView()
        }catch (e : Exception){
            e.printStackTrace()
        }

    }

    private fun updateRemoteView(){
        val manager = AppWidgetManager.getInstance(context)
        val component = ComponentName(context, AppWidget::class.java)

        manager.notifyAppWidgetViewDataChanged(
            manager.getAppWidgetIds(component),
            R.id.listView
        )
    }


    companion object{
        internal val tasks = mutableListOf<ProviderTask>()

        private fun setTasks(newTasks: List<ProviderTask>) {
            tasks.clear()
            tasks.addAll(newTasks)
        }

        private fun setTask(newTasks: ProviderTask) {
            tasks.add(newTasks)
        }
    }
}

