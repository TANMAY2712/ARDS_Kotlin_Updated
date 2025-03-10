package com.ards.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ards.remote.apimodel.ChartResponse
import com.ards.remote.apimodel.NotificationListResponse

class HomeViewModel : ViewModel() {
    private val repository = HomeRepository()

    val isLoading = MutableLiveData<Boolean>()

    fun getChartData(startDate: String, endDate: String): LiveData<Result<ChartResponse>> {
        val result = MutableLiveData<Result<ChartResponse>>()
        isLoading.value = true // Show loader before API call

        repository.chartData(startDate, endDate).observeForever { apiResult ->
            result.postValue(apiResult)
            isLoading.value = false // Hide loader after API call
        }

        return result
    }

    fun getRecentRecord(trainNo: String, page: Int, userId: Int): LiveData<Result<NotificationListResponse>> {
        val result = MutableLiveData<Result<NotificationListResponse>>()
        isLoading.value = true // Show loader before API call

        repository.recentRecord(trainNo, page, userId).observeForever { apiResult ->
            result.postValue(apiResult)
            isLoading.value = false // Hide loader after API call
        }

        return result
    }
}
