package com.ards.ui.playground.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ards.model.Playground
import com.ards.remote.remote.ApiFactory
import com.ards.remote.remote.service.ArdsService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlaygroundRepository {
    private val apiService: ArdsService = ApiFactory.RetrofitClients.createService(ArdsService::class.java)

    fun getVideos(): LiveData<List<Playground>> {
        val liveData = MutableLiveData<List<Playground>>()

        apiService.getVideos().enqueue(object : Callback<List<Playground>> {
            override fun onResponse(call: Call<List<Playground>>, response: Response<List<Playground>>) {
                if (response.isSuccessful) {
                    liveData.postValue(response.body())
                } else {
                    liveData.postValue(emptyList())
                }
            }

            override fun onFailure(call: Call<List<Playground>>, t: Throwable) {
                liveData.postValue(emptyList())
            }
        })

        return liveData
    }
}
