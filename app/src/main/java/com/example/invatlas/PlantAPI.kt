package com.example.invatlas

import com.example.invatlas.models.Plant
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST

interface PlantAPI {
    @GET("allplants")
    fun getAllPlants(): Call<List<Plant>>

    @POST("identify")
    fun identify(): Call<UserPlant>
}