package com.example.projetoandroid.ViewModel

import androidx.lifecycle.ViewModel
import com.example.projetoandroid.Repository.WeatherRepository
import com.example.projetoandroid.Server.ApiClient
import com.example.projetoandroid.Server.ApiServices

class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {

    constructor(): this(WeatherRepository(ApiClient().getClient().create(ApiServices::class.java)))

    fun loadCurrentWeather(lat: Double, lng: Double, unit: String)=
        repository.getCurrentWeather(lat, lng, unit)

    fun loadForecastWeather(lat: Double, lng: Double, unit: String)=
        repository.getForecastWeather(lat, lng, unit)
}