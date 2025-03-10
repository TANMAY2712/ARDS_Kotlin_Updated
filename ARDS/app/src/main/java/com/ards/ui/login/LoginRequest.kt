package com.ards.ui.login

data class LoginRequest(
    val Username: String,
    val CountryCode: String,
    val email: String,
    val APIKey: String,
)
