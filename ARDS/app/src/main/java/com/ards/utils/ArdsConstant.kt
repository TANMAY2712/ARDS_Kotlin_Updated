package com.ards.utils

import android.content.Context
import android.widget.Toast

class ArdsConstant {
    companion object {
        // Base URLs for APIs
        val BASE_URL = "https://dev.workerunionsupport.com/api/"
        val PREDICTION_BASE_URL = "http://98.70.56.87:8085/"
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
        const val GetPresignedUrl = "get_presigned_url"
        const val ModelInference = "model_inference"
    }

    internal object ToolkitApiKey {
        const val APIKEY = "APIKey"
        const val USERID = "UserId"
        const val NOTIFICATIONId = "NotificationId"
        const val TITLE = "title"
        const val BODY = "body"
        const val FAULTS = "faults"
        const val PART_NAME = "part_name"
        const val COACH_NUMBER = "coach_number"
        const val COACH_POSITION = "coach_position"
        const val FAULT_ID = "Fault_ID"
        const val FAULTY_DETAILS = "faulty_details"
        const val FAULTY_PROBABLITY = "faulty_probablity"
        const val IMAGE_PATH = "Image_path"
        const val STATION_NAME = "station_name"
        const val TRAIN_NUMBER = "train_number"
        const val TRAIN_NAME = "train_name"
        const val COACH_SIDE = "coach_side"
        const val OUTPUT_URL = "output_url"
    }

}