package com.ards.utils

import android.text.TextUtils
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DateUtils {
    companion object {
        private val apiDateFormat: DateFormat =
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.UK)

        private val appDateFormat: DateFormat =
            SimpleDateFormat("dd MMM, yyyy", Locale.UK)
        private val appDateTimeFormat: DateFormat =
            SimpleDateFormat("dd MMM, yyyy | hh:mm a", Locale.UK)
        private val appTimeFormat: DateFormat =
            SimpleDateFormat("hh:mm a", Locale.UK)

        fun getAppDateFromApiDate(apiDate: String?): String {
            return if(!TextUtils.isEmpty(apiDate)) {
                try {
                    val date = apiDate?.let { apiDateFormat.parse(it) }
                    appDateFormat.format(date!!)
                } catch (e: ParseException) {
                    e.printStackTrace()
                    ""
                }
            }else {
                ""
            }
        }

        fun getAppDateFromDate(date: Date?): String? {
            return appDateFormat.format(date)
        }

        fun getAppTimeFromApiDate(apiDate: String?): String? {
            try {
                val date = apiDateFormat.parse(apiDate)!!
                return appTimeFormat.format(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return "NA"
        }

        fun getApiDateFromDate(date: Date): String {
            return apiDateFormat.format(date)
        }



    }
}