package com.ards.ui.libvideo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ards.remote.apimodel.VideoByCategoryResponse

class LibVideoViewModel : ViewModel() {

    private val repository = LibVideoRepository()

    val isLoading = MutableLiveData<Boolean>()

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