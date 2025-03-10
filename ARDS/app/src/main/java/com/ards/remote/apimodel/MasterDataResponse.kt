package com.ards.remote.apimodel

import androidx.annotation.Keep

class MasterDataResponse(
    val ResponseCode: Int = 0,
    val SuccessMessage: String? = "",
    val Data: List<DataResponse>
) {
    @Keep
    data class DataResponse(
        val MasterDataId: Int = 0,
        val MasterDataName: String = "",
        val MasterDataDescription: String = "",
        val MasterDataType: String = "",
        val IsActive: Boolean = false,
        val Id: Int = 0
        )
}
