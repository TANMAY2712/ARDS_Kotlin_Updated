package com.ards.ui.otp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ards.remote.apimodel.GenrateOTPResponse
import com.ards.remote.apimodel.SignInResponse
import com.ards.remote.apimodel.VerifyOtpResponse

class OtpViewModel : ViewModel() {
    private val repository = OtpRepository()

    val isLoading = MutableLiveData<Boolean>()

    fun verifyOTP(phone: String, otp: Int): LiveData<Result<VerifyOtpResponse>> {
        val result = MutableLiveData<Result<VerifyOtpResponse>>()
        isLoading.value = true // Show loader before API call

        repository.verifyOTP(phone, otp).observeForever { apiResult ->
            result.postValue(apiResult)
            isLoading.value = false // Hide loader after API call
        }

        return result
    }

    fun siginIn(phone: String): LiveData<Result<SignInResponse>> {
        val result = MutableLiveData<Result<SignInResponse>>()
        isLoading.value = true // Show loader before API call

        repository.signIn(phone).observeForever { apiResult ->
            result.postValue(apiResult)
            isLoading.value = false // Hide loader after API call
        }

        return result
    }
}
