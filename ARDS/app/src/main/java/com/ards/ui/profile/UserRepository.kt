package com.ards.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ards.remote.apimodel.MasterDataRequest
import com.ards.remote.apimodel.MasterDataResponse
import com.ards.remote.apimodel.UploadFileResponse
import com.ards.remote.apimodel.UserProfileRequest
import com.ards.remote.apimodel.UserProfileResponse
import com.ards.remote.remote.ApiFactory
import com.ards.remote.remote.service.ArdsService
import com.ards.utils.ArdsConstant
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

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

    fun uploadProfileImage(userImage: MultipartBody.Part, apikey: RequestBody): LiveData<Result<UploadFileResponse>> {
        val liveData = MutableLiveData<Result<UploadFileResponse>>()

        val call = apiService.uploadfile(
            userImage,apikey
        )
        call.enqueue(object : Callback<UploadFileResponse> {
            override fun onResponse(
                call: Call<UploadFileResponse>,
                response: Response<UploadFileResponse>
            ) {

                if (response.isSuccessful) {
                    liveData.value = Result.success(response.body()!!)
                } else {
                    liveData.value = Result.failure(Exception("Error: ${response.message()}"))
                }
            }

            override fun onFailure(call: Call<UploadFileResponse>, t: Throwable) {
                liveData.value = Result.failure(t)
            }
        })

        return liveData
    }

}
