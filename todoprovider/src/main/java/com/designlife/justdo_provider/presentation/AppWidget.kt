package com.designlife.justdo_provider.presentation

import android.app.Activity
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.RemoteViews
import androidx.annotation.DrawableRes
import com.designlife.justdo_provider.R
import com.designlife.justdo_provider.SetworkProvider
import com.designlife.justdo_provider.common.ProviderServiceLocator
import com.designlife.justdo_provider.common.ProviderUtils
import com.designlife.justdo_provider.presentation.view.TaskWidgetService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        val views = RemoteViews(context.packageName, R.layout.widget_task)
        val time = System.currentTimeMillis()
        val dayText = ProviderUtils.getCurrentDay(time)
        val dayNumberText = ProviderUtils.getMonthDay(time)
        views.setTextViewText(R.id.dayText,dayText)
        views.setTextViewText(R.id.dayNumber,dayNumberText)
        appWidgetManager.updateAppWidget(appWidgetIds, views)

        updateTemperature(context)


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


            // Home
            val homePendingIntent = PendingIntent.getBroadcast(
                context,
                4,
                Intent(context, AppWidget::class.java).apply {
                    action = ProviderUtils.ACTION_HOME
                },
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.setwork_home, homePendingIntent)


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
        // actionId {-1 means new task, -2 means chat, -3 means existing task, -4 means home }
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
                    updateDate(context)
                    updateTemperature(context)
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

                ProviderUtils.ACTION_HOME -> {
                    val activity : Activity = Class.forName(ProviderUtils.CLASS_PATH).newInstance() as Activity
                    activity?.let {
                        val intent = Intent(context, activity::class.java).apply {
                            putExtra("fromProvider", true)
                            putExtra("actionId", -4)
                            putExtra("taskId", -1)
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        }
                        context.startActivity(intent)
                    }
                }

                ProviderUtils.ACTION_MORE_CLICK -> {

                }
            }
        }catch (e : Exception){
            Log.e("PROVIDER_FLOW", "onReceive: ${e.message}")
        }

    }

    private fun updateDate(context: Context) {
        val views = RemoteViews(context.packageName, R.layout.widget_task)

        val time = System.currentTimeMillis()
        val dayText = ProviderUtils.getCurrentDay(time)
        val dayNumberText = ProviderUtils.getMonthDay(time)
        views.setTextViewText(R.id.dayText,dayText)
        views.setTextViewText(R.id.dayNumber,dayNumberText)

        val manager = AppWidgetManager.getInstance(context)
        val component = ComponentName(context, AppWidget::class.java)

        manager.updateAppWidget(component, views)
    }

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
        context?.let {
            initialUpdate(context)
            startClockUpdates(context)
            startTemperatureUpdates(context)
        }
    }

    private fun initialUpdate(context: Context) {
        updateDate(context)
        updateTemperature(context)
        updateList(context)
    }

    private fun startTemperatureUpdates(context: Context) {
        val interval = 15 * 60 * 1000L


        val handler = Handler(Looper.getMainLooper())

        val runnable = object : Runnable {
            override fun run() {
                updateTemperature(context)
                val delay = interval - (System.currentTimeMillis() % interval)
                handler.postDelayed(this, delay)
            }
        }

        handler.post(runnable)
    }

    private fun startClockUpdates(context: Context) {
        val handler = Handler(Looper.getMainLooper())

        val runnable = object : Runnable {
            override fun run() {
                updateClock(context)

                val delay = 60000 - (System.currentTimeMillis() % 60000)
                handler.postDelayed(this, delay)
            }
        }

        handler.post(runnable)
    }

    private fun updateClock(context: Context) {
        val views = RemoteViews(context.packageName, R.layout.widget_task)

        val time = System.currentTimeMillis()
        val clockTime = ProviderUtils.getClockTime(time)
        val clockHMA = ProviderUtils.getClockHMA(time)

        views.setTextViewText(R.id.clockHour, clockTime)
        views.setTextViewText(R.id.clockHMA, clockHMA)

        val manager = AppWidgetManager.getInstance(context)
        val component = ComponentName(context, AppWidget::class.java)

        manager.updateAppWidget(component, views)
    }

    private fun updateTemperature(context: Context) {
        try {
            ProviderServiceLocator.provideWeatherUpdateRepository()?.let {
                CoroutineScope(Dispatchers.IO).launch{
                    it.fetchReleaseUpdates()?.let {
                        withContext(Dispatchers.Main.immediate){
                            updateTemperatureInfo(context)
                        }
                    }
                }
            }
        }catch (e : Exception){
            e.printStackTrace()
            updateTemperatureInfo(context)
        }
    }


    private fun updateTemperatureInfo(context: Context){
        val views = RemoteViews(context.packageName, R.layout.widget_task)
        val weather = ProviderUtils.APP_WEATHER_REPORT
        val time = System.currentTimeMillis()
        val temperature = weather.currentWeather.temperature
        val temperatureIcon : Icon = getWeatherIcon(context,weather.currentWeather.weatherCode,weather.currentWeather.isDay)
        val clockTime = ProviderUtils.getClockTime(time)
        val clockHMA = ProviderUtils.getClockHMA(time)

        val temperatureFormatted = "%.1f".format(temperature)
        views.setTextViewText(R.id.clockHour,clockTime)
        views.setTextViewText(R.id.clockHMA,clockHMA)
        views.setTextViewText(R.id.clockTemp,temperatureFormatted)
        views.setImageViewIcon(R.id.clockTempIcon,temperatureIcon)

        val appWidgetManager = AppWidgetManager.getInstance(context)
        val componentName = ComponentName(context, AppWidget::class.java)

        appWidgetManager.updateAppWidget(componentName, views)
    }
    private fun updateList(context: Context) {
        val manager = AppWidgetManager.getInstance(context)
        val component = ComponentName(context, AppWidget::class.java)

        manager.notifyAppWidgetViewDataChanged(
            manager.getAppWidgetIds(component),
            R.id.listView
        )
    }

    fun getWeatherIcon(context: Context, weatherCode: Int, isDay: Int): Icon {
        @DrawableRes val resId = when (weatherCode) {
            0 -> if (isDay == 1) R.drawable.ic_temp_day else R.drawable.ic_temp_night
            1, 2, 3 -> R.drawable.ic_temp_cloud
            45, 48 -> R.drawable.ic_temp_cloud
            51, 53, 55 -> R.drawable.ic_temp_rain
            56, 57 -> R.drawable.ic_temp_rain
            61, 63, 65 -> R.drawable.ic_temp_rain
            66, 67 -> R.drawable.ic_temp_rain
            71, 73, 75 -> R.drawable.ic_temp_snow
            77 -> R.drawable.ic_temp_snow
            80, 81, 82 -> R.drawable.ic_temp_rain
            85, 86 -> R.drawable.ic_temp_snow
            95, 96, 99 -> R.drawable.ic_temp_rain
            else -> R.drawable.ic_temp_day
        }

        return Icon.createWithResource(context, resId)
    }
}