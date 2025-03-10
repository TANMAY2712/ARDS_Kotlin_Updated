package com.ards.remote.apimodel

data class SignInRequest (
    val APIKey:String,
    val ClientIpAddress:String,
    val Username:String,
    val CountryCode:String,
    val Password:String,
    val FbToken:String,
    val AppType:String,
    )