package com.ards.utils

class Constant {
    companion object {
        // Base URLs for APIs
        val BASE_URL = "https://dev.workerunionsupport.com/api/"
        val BASE_URLA = "http://98.70.56.87:8085/"
        val WUS_ROOMDB = "wusARDSDB.db"
        val ARDS_APIKEY = "AR-AUG-ARST-BIZBR-2019OLLY"
        const val MOBILE = "mobile"
    }

    internal object ApiEndPoint {
        const val DALogin = "Login/SignIn"
        const val IfUserExists = "Worker/IfUserExists"
        const val GenrateOTP = "Login/GenrateOTP"
        const val VerifyOTP = "Login/VerifyOTP"
        const val MobileSignup = "Login/MobileSignup"
        const val SignOut = "Login/SignOut"
    }

}