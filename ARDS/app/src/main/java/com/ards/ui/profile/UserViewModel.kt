package com.ards.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ards.remote.apimodel.MasterDataResponse
import com.ards.remote.apimodel.UploadFileResponse
import com.ards.remote.apimodel.UserProfileResponse
import com.ards.utils.ArdsConstant
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class UserViewModel: ViewModel() {

    private val repository = UserRepository()

    val isLoading = MutableLiveData<Boolean>()

    fun getUnionmaster(type: String,
                   Id: Int,
                   errMsg: String
    ): LiveData<Result<MasterDataResponse>> {
        val result = MutableLiveData<Result<MasterDataResponse>>()
        isLoading.value = true // Show loader before API call

        repository.getUnionType(type, Id,errMsg).observeForever { apiResult ->
            result.postValue(apiResult)
            isLoading.value = false // Hide loader after API call
        }

        return result
    }

    fun updateUserProfile(
        name: String,
        address: String,
        zoneId: Int,
        DivisionId: Int,
        dateOfBirth: String,
        Username: String,
        CountryCode:String,
        AuthToken:String
    ): LiveData<Result<UserProfileResponse>> {
        val result = MutableLiveData<Result<UserProfileResponse>>()
        isLoading.value = true // Show loader before API call

        repository.updateUserProfile(
            name,
            address,
            zoneId,
            DivisionId,
            dateOfBirth,
            Username,
            CountryCode,
            AuthToken
        ).observeForever { apiResult ->
            result.postValue(apiResult)
            isLoading.value = false // Hide loader after API call
        }

        return result
    }

    fun uploadProfileImage(file: File?, mediaTypeFile: Int): LiveData<Result<UploadFileResponse>> {
        val userImage = MultipartBody.Part.createFormData(
            "UserImage",
            file!!.name,
            file.asRequestBody("image/*".toMediaTypeOrNull())
        )

        val apikey: RequestBody = (ArdsConstant.ARDS_APIKEY)
            .toRequestBody("multipart/form-data".toMediaTypeOrNull())

        val result = MutableLiveData<Result<UploadFileResponse>>()
        isLoading.value = true // Show loader before API call

        repository.uploadProfileImage(
            userImage,
            apikey
        ).observeForever { apiResult ->
            result.postValue(apiResult)
            isLoading.value = false // Hide loader after API call
        }

        return result
    }
}