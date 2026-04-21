package com.designlife.justdo_provider.presentation

object ProviderUtils {

    fun getCurrentDay(): String {
        val format = java.text.SimpleDateFormat("EEEE", java.util.Locale.getDefault())
        return format.format(java.util.Date())
    }

    fun getCurrentDayNumber(): String {
        val format = java.text.SimpleDateFormat("d", java.util.Locale.getDefault())
        return format.format(java.util.Date())
    }

    fun getTimeGracefully(epoch: Long): String {
        val formatter = java.text.SimpleDateFormat("hh:mm a", java.util.Locale.getDefault())
        return formatter.format(java.util.Date(epoch))
    }

    // CONSTANTS
    internal val TASK_ID = "TASK_ID"
    // CLICKS
    internal val ACTION_TASK_CLICK = "ACTION_TASK_CLICK"
    internal val ACTION_REFRESH_CLICK = "ACTION_REFRESH_CLICK"
    internal val ACTION_CHAT_CLICK = "ACTION_CHAT_CLICK"
    internal val ACTION_MORE_CLICK = "ACTION_MORE_CLICK"
    internal val ACTION_NEW_TASK_CLICK = "ACTION_NEW_TASK_CLICK"

    // REFLECTION PATH
    var CLASS_PATH =  "com.designlife.justdo.MainActivity"



}