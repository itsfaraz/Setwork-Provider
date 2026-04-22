package com.designlife.justdo_provider.domain

import com.designlife.justdo.common.data.network.ProviderWeatherService
import com.designlife.justdo.common.data.network.reponse.MeteoResponse
import com.designlife.justdo_provider.common.ProviderUtils

class WeatherUpdateRepository(
    private val weatherService: ProviderWeatherService
) {
    suspend fun fetchReleaseUpdates(): MeteoResponse? {
        val response = weatherService.fetchCurrentWeather(
            latitude = ProviderUtils.LATITUDE,
            longitude = ProviderUtils.LONGITUDE
        )
        if (response.isSuccessful){
            response.body()?.let {
                ProviderUtils.APP_WEATHER_REPORT = it
                return it
            }
        }
        return null
    }

}