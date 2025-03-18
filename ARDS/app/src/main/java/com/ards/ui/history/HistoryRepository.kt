package com.ards.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import com.ards.remote.apimodel.NotificationListRequest
import com.ards.remote.apimodel.NotificationListResponse
import com.ards.remote.remote.ApiFactory
import com.ards.remote.remote.service.ArdsService
import com.ards.utils.ArdsConstant
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryRepository {
    private val apiService: ArdsService = ApiFactory.RetrofitClient.createService(ArdsService::class.java)

    fun getHistory(trainNo: String, pageNo: Int, userId: Int): LiveData<Result<NotificationListResponse>> {
        val liveData = MutableLiveData<Result<NotificationListResponse>>()

        val call = apiService.getAllNotification(NotificationListRequest(ArdsConstant.ARDS_APIKEY, trainNo, pageNo, userId))
        call.enqueue(object : Callback<NotificationListResponse> {
            override fun onResponse(call: Call<NotificationListResponse>, response: Response<NotificationListResponse>) {

                if (response.isSuccessful) {
                    liveData.value = Result.success(response.body()!!)
                } else {
                    liveData.value = Result.failure(Exception("Error: ${response.message()}"))
                }
            }

            override fun onFailure(call: Call<NotificationListResponse>, t: Throwable) {
                liveData.value = Result.failure(t)
            }
        })
        return liveData
    }
}
