package com.designlife.justdo.common.data.network

import com.designlife.justdo.common.data.network.reponse.MeteoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ProviderWeatherService {
    @GET("forecast")
    suspend fun fetchCurrentWeather(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current_weather") currentWeather: Boolean = true,
        @Query("timezone") timeZone: String = "auto",
    ) : Response<MeteoResponse>
}