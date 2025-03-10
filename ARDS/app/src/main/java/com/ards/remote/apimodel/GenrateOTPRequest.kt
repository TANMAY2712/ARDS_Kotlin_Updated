package com.ards.domain.model

data class GenrateOTPRequest(
    val Username: String,
    val CountryCode: String,
    val email: String,
    val APIKey: String,
)

