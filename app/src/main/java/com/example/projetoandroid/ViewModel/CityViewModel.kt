package com.example.projetoandroid.ViewModel

import androidx.lifecycle.ViewModel
import com.example.projetoandroid.Repository.CityRepository
import com.example.projetoandroid.Server.ApiClient
import com.example.projetoandroid.Server.ApiServices

class CityViewModel(val repository: CityRepository): ViewModel() {
    constructor(): this(CityRepository(ApiClient().getClient().create(ApiServices::class.java)))

    fun loadCity(q: String, limit: Int)=
        repository.getCities(q, limit)
}