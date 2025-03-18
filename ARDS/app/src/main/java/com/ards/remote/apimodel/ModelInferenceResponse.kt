package com.ards.remote.apimodel

import androidx.annotation.Keep

class ModelInferenceResponse(
    val statusCode: Int=0,
    val data: InferenceData
){
    @Keep
    data class InferenceData(
        val faults: List<FaultList>,
        val output_url: String = "",
        val progress: String = ""
    ){
        @Keep
        data class FaultList(
            val coach_number: String = "",
            val coach_position: Int = 0,
            val fault_info: String = "",
            val fault_probability: String = "",
            val fault_timestamp: String = "",
            val image_url: String = "",
            val part_name: String = "",
            val recorded_side: String = "",
            val station_code: String = "",
            val station_name: String = "",
            val train_name: String = "",
            val train_number: String = "",
            val uploaded_time: String = "",
        )
    }
}