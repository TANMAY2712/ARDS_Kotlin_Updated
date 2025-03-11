package com.ards.ui.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ards.remote.apimodel.NotificationListResponse

class NotificationListViewModel: ViewModel() {

    /*private val _text = MutableLiveData<String>().apply {
        value = "This is library Fragment"
    }
    val text: LiveData<String> = _text*/

    private val repository = NotificationListRepository()

    val isLoading = MutableLiveData<Boolean>()

    fun getAllNotification(trainNo: String, pageNo: Int, userId: Int): LiveData<Result<NotificationListResponse>> {
        val result = MutableLiveData<Result<NotificationListResponse>>()
        isLoading.value = true // Show loader before API call

        repository.getAllNotification(trainNo, pageNo, userId).observeForever { apiResult ->
            result.postValue(apiResult)
            isLoading.value = false // Hide loader after API call
        }

        return result
    }
}