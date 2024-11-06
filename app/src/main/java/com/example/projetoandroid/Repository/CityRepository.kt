package com.example.projetoandroid.Repository

import com.example.projetoandroid.Server.ApiServices

class CityRepository(val api: ApiServices) {
    fun getCities(q: String, limit: Int)=
        api.getCitiesList(q,limit,"e2fe29806478018a5daa8dd08d5d43e8")
}