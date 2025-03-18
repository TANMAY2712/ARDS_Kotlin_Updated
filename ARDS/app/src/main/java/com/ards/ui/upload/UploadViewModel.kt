package com.ards.ui.upload

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ards.remote.apimodel.ModelInferenceResponse
import com.ards.remote.apimodel.PreSignedUrlResponse

class UploadViewModel : ViewModel() {

    //private val repository = UploadRepository()

    val isLoading = MutableLiveData<Boolean>()

    fun getPreSignedUrl(trainNo: String, station: String, recSide: String): LiveData<Result<PreSignedUrlResponse>> {
        val result = MutableLiveData<Result<PreSignedUrlResponse>>()
        isLoading.value = true // Show loader before API call

        /*repository.getPresignedUrl(trainNo, station, recSide).observeForever { apiResult ->
            result.postValue(apiResult)
            isLoading.value = false // Hide loader after API call
        }*/

        return result
    }

    fun modelInference(presignedUrl:String, trainNo: String, station: String, recSide: String): LiveData<Result<ModelInferenceResponse>> {
        val result = MutableLiveData<Result<ModelInferenceResponse>>()
        isLoading.value = true // Show loader before API call

        /*repository.performModelInference(presignedUrl, trainNo, station, recSide).observeForever { apiResult ->
            result.postValue(apiResult)
            isLoading.value = false // Hide loader after API call
        }*/

        return result
    }
}