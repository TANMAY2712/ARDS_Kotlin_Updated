package com.ards.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ards.remote.apimodel.MasterDataResponse

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
}