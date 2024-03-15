package com.example.invatlas.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.invatlas.models.Plant
import com.example.invatlas.PlantAPI
import com.example.invatlas.RetrofitClient
import com.example.invatlas.UserPlant
import kotlinx.coroutines.launch

class PlantViewModel : ViewModel() {
    private var _plantList = mutableStateListOf<Plant>()
    private var _userPlant: UserPlant? = null
    var errorMessage: String by mutableStateOf("")
    val plantList: List<Plant>
        get() = _plantList

    var userPlant : UserPlant? = null
        get() = _userPlant

    fun getAllPlants() {
        viewModelScope.launch {
            try {
                val retrofit = RetrofitClient.getClient()
                val userApi = retrofit.create(PlantAPI::class.java)
                val plantResponse = userApi.getAllPlants().execute()
                _plantList.addAll(plantResponse.body().orEmpty())

            } catch (e: Exception) {
                errorMessage = e.message.toString()
            }
        }
    }

    fun identifyPlant() {
        viewModelScope.launch {
            try {
                val retrofit = RetrofitClient.getClient()
                val userApi = retrofit.create(PlantAPI::class.java)
                val plantResponse = userApi.identify().execute()
                userPlant = plantResponse.body()

            } catch (e: Exception) {
                errorMessage = e.message.toString()
            }
        }
    }

}
