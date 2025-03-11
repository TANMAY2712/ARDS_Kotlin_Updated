package com.ards.remote.apimodel

import androidx.annotation.Keep

class UserProfileResponse(
    val ResponseCode: String = "",
    val SuccessMessage: String? = "",
    val Data: DataResponse
) {
    @Keep
    data class DataResponse(
        val UserId: Int = 0,
        val FullName: String = "",
        val MobileNumber: String = "",
        val CountryCode: String = "",
        val ProfileImage: String = "",
        val DOB: String = "",
        val DOJ: String = "",
        val OfficeAddress: String = "",
        val Age: Int = 0,
        val DivisionId: Int = 0,
        val ZoneId: Int = 0
    )
}