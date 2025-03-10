package com.ards.remote.apimodel

import androidx.annotation.Keep

class NotificationFaultListResponse(
    val ResponseCode: Int = 0,
    val SuccessMessage: String? = "",
    val Data: DataResponse
) {
    @Keep
    data class DataResponse(
        val faults: List<Faults>,
        val output_url: String = "",
        val IsNextPage: Int = 0,
        val TotalFaults: Int = 0,
        val RecordsPerPage: Int = 0,
    ){
        @Keep
        data class Faults(
            val part_name: String = "",
            val TrainName: String = "",
            val TrainNumber: String = "",
            val coach_number: String = "",
            val coach_position: String = "",
            val Fault_ID: String = "",
            val faulty_details: String = "",
            val faulty_probablity: String = "",
            val fault_timestamp: String = "",
            val Image_path: String = "",
            val CreatedDate: String = "",
        )
    }
}