package com.designlife.justdo_provider.presentation

import android.app.Activity
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.RemoteViews
import com.designlife.justdo_provider.R
import com.designlife.justdo_provider.SetworkProvider
import com.designlife.justdo_provider.presentation.view.TaskWidgetService

class AppWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        val views = RemoteViews(context.packageName, R.layout.widget_task)
        val dayText = ProviderUtils.getCurrentDay()
        val dayNumberText = ProviderUtils.getCurrentDayNumber()
        views.setTextViewText(R.id.dayText,dayText)
        views.setTextViewText(R.id.dayNumber,dayNumberText)
        appWidgetManager.updateAppWidget(appWidgetIds, views)

        for (appWidgetId in appWidgetIds) {

            val intent = Intent(context, TaskWidgetService::class.java).apply {
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))
            }

            val views = RemoteViews(context.packageName, R.layout.widget_task)
            views.setRemoteAdapter(R.id.listView, intent)
            views.setEmptyView(R.id.listView, R.id.emptyWidgetTask)

            // ADD
            val addPendingIntent = PendingIntent.getBroadcast(
                context,
                1,
                Intent(context, AppWidget::class.java).apply {
                    action = ProviderUtils.ACTION_NEW_TASK_CLICK
                },
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.btnAdd, addPendingIntent)

            // REFRESH
            val refreshPendingIntent = PendingIntent.getBroadcast(
                context,
                2,
                Intent(context, AppWidget::class.java).apply {
                    action = ProviderUtils.ACTION_REFRESH_CLICK
                },
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.emptyTaskBtnRefresh, refreshPendingIntent)


            // CHAT
            val chatPendingIntent = PendingIntent.getBroadcast(
                context,
                3,
                Intent(context, AppWidget::class.java).apply {
                    action = ProviderUtils.ACTION_CHAT_CLICK
                },
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.btnChat, chatPendingIntent)


            val clickIntent = Intent(context, AppWidget::class.java).apply {
                action = ProviderUtils.ACTION_TASK_CLICK
                setPackage(context.packageName)
            }

            val clickPendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                clickIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )

            views.setPendingIntentTemplate(R.id.listView, clickPendingIntent)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }


    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        // actionId { -1 means new task, -2 means chat, -3 means existing task }
        // taskId for existing tasks
        try {
            when (intent.action) {
                ProviderUtils.ACTION_NEW_TASK_CLICK -> {

                    val activity : Activity = Class.forName(ProviderUtils.CLASS_PATH).newInstance() as Activity
                    activity?.let {
                        val intent = Intent(context, activity::class.java).apply {
                            putExtra("fromProvider", true)
                            putExtra("actionId", -1)
                            putExtra("taskId", -1)
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        }
                        context.startActivity(intent)
                    }

                    // Always update
                    updateList(context)
                }

                ProviderUtils.ACTION_REFRESH_CLICK -> {
                    updateList(context)
                }

                ProviderUtils.ACTION_CHAT_CLICK -> {
                    val activity : Activity = Class.forName(ProviderUtils.CLASS_PATH).newInstance() as Activity
                    activity?.let {
                        val intent = Intent(context, activity::class.java).apply {
                            putExtra("fromProvider", true)
                            putExtra("actionId", -2)
                            putExtra("taskId", -1)
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        }
                        context.startActivity(intent)
                    }
                }

                ProviderUtils.ACTION_TASK_CLICK -> {
                    val taskId = intent.getIntExtra(ProviderUtils.TASK_ID, -1)
                    if (taskId != -1) {
                        val activity : Activity = Class.forName(ProviderUtils.CLASS_PATH).newInstance() as Activity
                        activity?.let {
                            val intent = Intent(context, activity::class.java).apply {
                                putExtra("fromProvider", true)
                                putExtra("actionId", -3)
                                putExtra("taskId", taskId)
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                            }
                            context.startActivity(intent)
                        }
                    }
                }

                ProviderUtils.ACTION_MORE_CLICK -> {

                }
            }
        }catch (e : Exception){
            Log.e("PROVIDER_FLOW", "onReceive: ${e.message}")
        }

    }


    private fun updateList(context: Context) {
        val manager = AppWidgetManager.getInstance(context)
        val component = ComponentName(context, AppWidget::class.java)

        manager.notifyAppWidgetViewDataChanged(
            manager.getAppWidgetIds(component),
            R.id.listView
        )
    }
}