package com.ards.ui.library

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ards.remote.apimodel.MasterDataResponse

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
}