package com.ards.ui.library

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ards.remote.apimodel.MasterDataRequest
import com.ards.remote.apimodel.MasterDataResponse
import com.ards.remote.remote.ApiFactory
import com.ards.remote.remote.service.ArdsService
import com.ards.utils.Constant
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LibraryRepository {
    private val apiService: ArdsService = ApiFactory.RetrofitClient.createService(ArdsService::class.java)

    fun getLibrary(type: String, Id: Int): LiveData<Result<MasterDataResponse>> {
        val liveData = MutableLiveData<Result<MasterDataResponse>>()


        val call = apiService.masterData(MasterDataRequest(Constant.ARDS_APIKEY, type, Id))
        call.enqueue(object : Callback<MasterDataResponse> {
            override fun onResponse(call: Call<MasterDataResponse>, response: Response<MasterDataResponse>) {

                if (response.isSuccessful) {
                    liveData.value = Result.success(response.body()!!)
                } else {
                    liveData.value = Result.failure(Exception("Error: ${response.message()}"))
                }
            }

            override fun onFailure(call: Call<MasterDataResponse>, t: Throwable) {
                liveData.value = Result.failure(t)
            }
        })

        return liveData
    }
}
