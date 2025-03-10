package com.ards.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ards.remote.apimodel.NotificationListResponse

class HistoryViewModel : ViewModel() {

    private val repository = HistoryRepository()

    val isLoading = MutableLiveData<Boolean>()

    fun getHistory(trainNo: String, pageNo: Int, userId: Int): LiveData<Result<NotificationListResponse>> {
        val result = MutableLiveData<Result<NotificationListResponse>>()
        isLoading.value = true // Show loader before API call

        repository.getHistory(trainNo, pageNo, userId).observeForever { apiResult ->
            result.postValue(apiResult)
            isLoading.value = false // Hide loader after API call
        }

        return result
    }
}