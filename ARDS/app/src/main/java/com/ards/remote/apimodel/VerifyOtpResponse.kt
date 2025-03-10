package com.ards.remote.apimodel

import androidx.annotation.Keep

class VerifyOtpResponse(
    val ResponseCode: String = "",
    val SuccessMessage: String? = "",
    val Data: Boolean = false
)
