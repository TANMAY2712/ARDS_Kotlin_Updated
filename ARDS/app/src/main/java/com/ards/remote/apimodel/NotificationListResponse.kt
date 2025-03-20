package com.ards.remote.apimodel

import androidx.annotation.Keep

class NotificationListResponse (
    val ResponseCode: Int = 0,
    val SuccessMessage: String? = "",
    val Data: DataResponse
) {
    @Keep
    data class DataResponse(
        val faults: List<Faults>,
        val IsNextPage: Int = 0,
        val TotalFaults: Int = 0,
        val RecordsPerPage: Int = 0,
    ){
        @Keep
        data class Faults(
            val Id: Int = 0,
            val UserName: String = "",
            val MobileNumber: String = "",
            val title: String = "",
            val body: String = "",
            val station_name: String = "",
            val train_number: String = "",
            val train_name: String = "",
            val coach_side: String = "",
            val output_url: String = "",
            val createdDate: String = "",
            val TotalFaults: String = "",
        )
    }
}