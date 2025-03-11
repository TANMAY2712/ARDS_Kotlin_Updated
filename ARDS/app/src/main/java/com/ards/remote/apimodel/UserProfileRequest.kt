package com.ards.remote.apimodel

data class UserProfileRequest(
    val FullName: String,
    val OfficeAddress: String,
    val ZoneId: Int,
    val DivisionId: Int,
    val DOB: String,
    val Username: String,
    val CountryCode: String,
    val AuthToken: String
)
