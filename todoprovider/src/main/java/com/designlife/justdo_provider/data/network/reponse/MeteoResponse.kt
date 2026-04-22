package com.designlife.justdo.common.data.network.reponse

import com.google.gson.annotations.SerializedName

data class MeteoResponse(
    @SerializedName("latitude") val latitude : Double = 0.0,
    @SerializedName("longitude") val longitude : Double = 0.0,
    @SerializedName("timezone") val timezone : String = "",
    @SerializedName("elevation")  val elevation : Int = 0,
    @SerializedName("current_weather") val currentWeather : CurrentWeather = CurrentWeather(),
){
    data class CurrentWeather(
        @SerializedName("time") val refreshTime : String = "",
        @SerializedName("interval") val interval : Int = 0,
        @SerializedName("temperature") val temperature : Double = 0.0,
        @SerializedName("windspeed") val windSpeed : Double = 0.0,
        @SerializedName("winddirection") val windDirection : Int = 0,
        @SerializedName("is_day") val isDay : Int = 0,
        @SerializedName("weathercode") val weatherCode : Int = 0
    )
}