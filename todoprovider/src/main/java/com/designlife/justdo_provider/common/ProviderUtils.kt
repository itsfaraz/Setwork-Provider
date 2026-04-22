package com.designlife.justdo_provider.common

import com.designlife.justdo.common.data.network.reponse.MeteoResponse
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ProviderUtils {

    fun getCurrentDay(epoch: Long): String {
        val format = SimpleDateFormat("EEEE", Locale.getDefault())
        return format.format(Date(epoch))
    }

    fun getMonthDay(epoch: Long): String {
        val format = SimpleDateFormat("MMM d", Locale.ENGLISH)
        return format.format(Date(epoch))
    }

    fun getTimeGracefully(epoch: Long): String {
        val formatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return formatter.format(Date(epoch))
    }

    fun getClockTime(epoch: Long): String {
        val formatter = SimpleDateFormat("hh:mm", Locale.getDefault())
        return formatter.format(Date(epoch))
    }

    fun getClockHMA(epoch: Long): String {
        val formatter = SimpleDateFormat("a", Locale.getDefault())
        return formatter.format(Date(epoch))
    }

    // CONSTANTS
    internal val TASK_ID = "TASK_ID"
    // CLICKS
    internal val ACTION_TASK_CLICK = "ACTION_TASK_CLICK"
    internal val ACTION_REFRESH_CLICK = "ACTION_REFRESH_CLICK"
    internal val ACTION_CHAT_CLICK = "ACTION_CHAT_CLICK"
    internal val ACTION_MORE_CLICK = "ACTION_MORE_CLICK"
    internal val ACTION_NEW_TASK_CLICK = "ACTION_NEW_TASK_CLICK"
    internal val ACTION_HOME = "ACTION_HOME"

    // REFLECTION PATH
    var CLASS_PATH =  "com.designlife.justdo.MainActivity"

    // APPROX LOCATION
    internal var LATITUDE : Double = 19.0760
    internal var LONGITUDE : Double = 72.8777

    // APP WEATHER REPORT

    internal var APP_WEATHER_REPORT = MeteoResponse()

}