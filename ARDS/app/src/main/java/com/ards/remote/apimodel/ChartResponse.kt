package com.ards.remote.apimodel

import androidx.annotation.Keep

class ChartResponse(
    val ResponseCode: String = "",
    val SuccessMessage: String? = "",
    val Data: DataResponse
) {
    @Keep
    data class DataResponse(
        val faults: List<Faults>,
        val StartDateActual: String = "",
        val EndDateActual: String = "",
    ){
        @Keep
        data class Faults(
            val Type: String = "",
            val FaultPercentage: Double = 0.0,
            val FaultCount: Int = 0
        )
    }
}