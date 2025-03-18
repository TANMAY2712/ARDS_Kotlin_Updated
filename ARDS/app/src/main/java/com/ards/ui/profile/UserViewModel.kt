package com.ards.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ards.remote.apimodel.MasterDataResponse
import com.ards.remote.apimodel.UserProfileResponse

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
        dob: String,
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
            dob,
            Username,
            CountryCode,
            AuthToken
        ).observeForever { apiResult ->
            result.postValue(apiResult)
            isLoading.value = false // Hide loader after API call
        }

        return result
    }
}