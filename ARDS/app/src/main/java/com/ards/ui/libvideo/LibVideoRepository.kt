package com.ards.ui.libvideo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ards.remote.apimodel.VideoByCategoryRequest
import com.ards.remote.apimodel.VideoByCategoryResponse
import com.ards.remote.remote.ApiFactory
import com.ards.remote.remote.service.ArdsService
import com.ards.utils.ArdsConstant
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LibVideoRepository {
    private val apiService: ArdsService = ApiFactory.RetrofitClient.createService(ArdsService::class.java)

    fun getVideoBycategory(catgoryId: String): LiveData<Result<VideoByCategoryResponse>> {
        val liveData = MutableLiveData<Result<VideoByCategoryResponse>>()


        val call = apiService.getVideoByCategory(VideoByCategoryRequest(ArdsConstant.ARDS_APIKEY, catgoryId))
        call.enqueue(object : Callback<VideoByCategoryResponse> {
            override fun onResponse(call: Call<VideoByCategoryResponse>, response: Response<VideoByCategoryResponse>) {

                if (response.isSuccessful) {
                    liveData.value = Result.success(response.body()!!)
                } else {
                    liveData.value = Result.failure(Exception("Error: ${response.message()}"))
                }
            }

            override fun onFailure(call: Call<VideoByCategoryResponse>, t: Throwable) {
                liveData.value = Result.failure(t)
            }
        })

        return liveData
    }
}
