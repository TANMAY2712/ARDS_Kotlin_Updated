package com.ards.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ards.remote.apimodel.GenrateOTPResponse

class LoginViewModel : ViewModel() {
    private val repository = LoginRepository()

    val isLoading = MutableLiveData<Boolean>()  // Loader state

    fun sendOtp(phone: String): LiveData<Result<GenrateOTPResponse>> {
        val result = MutableLiveData<Result<GenrateOTPResponse>>()
        isLoading.value = true // Show loader before API call

        repository.sendOtp(phone).observeForever { apiResult ->
            result.postValue(apiResult)
            isLoading.value = false // Hide loader after API call
        }

        return result
    }
}
