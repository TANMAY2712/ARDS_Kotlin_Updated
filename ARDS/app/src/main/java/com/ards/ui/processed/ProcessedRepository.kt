package com.ards.ui.playground.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ards.model.Playground
import com.ards.remote.remote.ApiFactory
import com.ards.remote.remote.service.ArdsService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProcessesRepository {
    private val apiService: ArdsService = ApiFactory.RetrofitClient.createService(ArdsService::class.java)

    fun getPlayground(apiUrl: String): LiveData<List<Playground>> {
        val liveData = MutableLiveData<List<Playground>>()

        apiService.getPlayground(apiUrl).enqueue(object : Callback<List<Playground>> {
            override fun onResponse(call: Call<List<Playground>>, response: Response<List<Playground>>) {
                if (response.isSuccessful) {
                    liveData.postValue(response.body())
                } else {
                    liveData.postValue(emptyList()) // Handle error case
                }
            }

            override fun onFailure(call: Call<List<Playground>>, t: Throwable) {
                liveData.postValue(emptyList()) // Handle network failure
            }
        })

        return liveData
    }
}
