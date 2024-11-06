package com.example.projetoandroid.Repository

import com.example.projetoandroid.Server.ApiServices

class WeatherRepository (val api: ApiServices){

    fun getCurrentWeather(lat: Double, lgn: Double, unit: String)=
        api.getCurrentWeather(lat, lgn, unit, "e2fe29806478018a5daa8dd08d5d43e8")

    fun getForecastWeather(lat: Double, lgn: Double, unit: String)=
        api.getForecastWeather(lat, lgn, unit, "e2fe29806478018a5daa8dd08d5d43e8")
}