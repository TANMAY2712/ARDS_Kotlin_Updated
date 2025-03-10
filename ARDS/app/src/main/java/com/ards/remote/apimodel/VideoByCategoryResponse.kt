package com.ards.remote.apimodel

import androidx.annotation.Keep

class VideoByCategoryResponse(
    val ResponseCode: Int = 0,
    val SuccessMessage: String? = "",
    val Data: List<DataResponse>
) {
    @Keep
    data class DataResponse(
        val Id: Int = 0,
        val VideoTiltle: String = "",
        val VideoDesc: String = "",
        val VideoUrl: String = "",
        val IsActive: Boolean = false,
        val CategoryId: Int = 0
    )
}