package com.ards.utils

import android.content.Context
import android.widget.Toast

class Constant {
    companion object {
        // Base URLs for APIs
        val BASE_URL = "https://dev.workerunionsupport.com/api/"
        val BASE_URLA = "http://98.70.56.87:8085/"
        val WUS_ROOMDB = "wusARDSDB.db"
        val ARDS_APIKEY = "AR-AUG-ARST-BIZBR-2019OLLY"
        const val MOBILE = "mobile"

        const val EncryptedSharedPreferencesKey = "WUS"
        const val SharedPreferencesKey = "ARDS_SharedPreferences"

        fun showShortToast(msg: String, ctx: Context){
            Toast.makeText(ctx,msg, Toast.LENGTH_SHORT).show()
        }

        fun showLongToast(msg: String, ctx: Context){
            Toast.makeText(ctx,msg,Toast.LENGTH_LONG).show()
        }
    }

    internal object ApiEndPoint {
        const val ChartByFaultType = "ToolKit/GetChartByFaultType"
        const val VideoByCategory = "ToolKit/GetVideoByCategory"
        const val GenrateOTP = "Login/GenrateOTP"
        const val VerifyOTP = "Login/VerifyOTP"
        const val SignIn = "Login/SignIn"
        const val NotificationList = "ToolKit/GetNotificationList"
        const val NotificationFault = "ToolKit/GetNotificationFaultList"
        const val UserProfile = "ToolKit/UpdateUserProfile"
        const val MasterData = "ToolKit/GetToolkitMasterData"
    }

}