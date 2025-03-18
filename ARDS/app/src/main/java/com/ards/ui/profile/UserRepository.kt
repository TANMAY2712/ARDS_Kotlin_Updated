package com.ards.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ards.remote.apimodel.MasterDataRequest
import com.ards.remote.apimodel.MasterDataResponse
import com.ards.remote.apimodel.UserProfileRequest
import com.ards.remote.apimodel.UserProfileResponse
import com.ards.remote.remote.ApiFactory
import com.ards.remote.remote.service.ArdsService
import com.ards.utils.ArdsConstant
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository {
    private val apiService: ArdsService = ApiFactory.RetrofitClient.createService(
        ArdsService::class.java
    )

    fun getUnionType(type: String, Id: Int, errMsg: String): LiveData<Result<MasterDataResponse>> {
        val liveData = MutableLiveData<Result<MasterDataResponse>>()


        val call = apiService.masterData(MasterDataRequest(ArdsConstant.ARDS_APIKEY, type, Id))
        call.enqueue(object : Callback<MasterDataResponse> {
            override fun onResponse(
                call: Call<MasterDataResponse>,
                response: Response<MasterDataResponse>
            ) {

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

    fun updateUserProfile(
        name: String,
        address: String,
        zoneId: Int,
        DivisionId: Int,
        dob: String,
        Username: String,
        CountryCode:String,
        AuthToken:String
    ): LiveData<Result<UserProfileResponse>> {
        val liveData = MutableLiveData<Result<UserProfileResponse>>()

        val call = apiService.updateUserProfile(
            UserProfileRequest(
                name,
                address,
                zoneId,
                DivisionId,
                dob,
                Username,
                CountryCode,
                AuthToken
            )
        )
        call.enqueue(object : Callback<UserProfileResponse> {
            override fun onResponse(
                call: Call<UserProfileResponse>,
                response: Response<UserProfileResponse>
            ) {

                if (response.isSuccessful) {
                    liveData.value = Result.success(response.body()!!)
                } else {
                    liveData.value = Result.failure(Exception("Error: ${response.message()}"))
                }
            }

            override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
                liveData.value = Result.failure(t)
            }
        })

        return liveData
    }
}
