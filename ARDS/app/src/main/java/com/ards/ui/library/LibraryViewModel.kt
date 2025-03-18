package com.ards.ui.library

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ards.remote.apimodel.MasterDataResponse
import com.ards.remote.apimodel.VideoByCategoryResponse

class LibraryViewModel : ViewModel() {

    private val repository = LibraryRepository()

    val isLoading = MutableLiveData<Boolean>()

    fun getLibrary(type: String, Id: Int): LiveData<Result<MasterDataResponse>> {
        val result = MutableLiveData<Result<MasterDataResponse>>()
        isLoading.value = true // Show loader before API call

        repository.getLibrary(type, Id).observeForever { apiResult ->
            result.postValue(apiResult)
            isLoading.value = false // Hide loader after API call
        }

        return result
    }

    fun getVideoBycategory(catId: String): LiveData<Result<VideoByCategoryResponse>> {
        val result = MutableLiveData<Result<VideoByCategoryResponse>>()
        isLoading.value = true // Show loader before API call

        repository.getVideoBycategory(catId).observeForever { apiResult ->
            result.postValue(apiResult)
            isLoading.value = false // Hide loader after API call
        }

        return result
    }
}