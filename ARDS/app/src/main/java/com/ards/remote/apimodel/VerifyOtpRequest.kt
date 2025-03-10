package com.ards.remote.apimodel

data class VerifyOtpRequest (
    val APIKey:String,
    val Username:String,
    val CountryCode:String,
    val OTP:Int,
    val RefferedCode:String
)