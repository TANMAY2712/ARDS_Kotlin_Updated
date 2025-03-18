package com.ards.sharedpreference

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.ards.utils.ArdsConstant

open class PreferenceHelper(context: Context) {
    private val PREF_NAME = "app_preferences"

    //private lateinit var preference: SharedPreferences

    private var preference: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)


    // Initialize in Application class or before use
/*    fun init(context: Context) {
        preference = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }*/

    companion object {
        private const val LOGIN = "LOGIN"
        private const val TOKEN = "TOKEN"
        private const val PROFILE = "PROFILE"
        private const val USERNAME = "USERNAME"
        private const val Address = "Address"

        private const val FULLNAME = "FULLNAME"
        private const val DOB = "DOB"
        private const val USERID = "USERID"
        private const val SELECTEDID = "SELECTEDID"
        private const val ParentUnionId = "ParentUnionId"
        private const val PHONE = "PHONE"
        private const val PFNUMBER = "PFNUMBER"
        private const val Fathername = "Fathername"
        private const val ROLEID = "RoleId"
        private const val PROFILECOMPLETED = "ProfileCompleted"
        private const val SUBSCRIPTIONALLOWED = "SubscriptionAllowed"
        private const val SUBSCRIPTIONID = "SubscriptionId"
        private const val SetUserBranchName = "SetUserBranchName"
        private const val SetUserBranchID = "SetUserBranchID"
        private const val SetUserDivisionID = "SetUserDivisionID"
        private const val SetUserZoneID = "SetUserZoneID"
        private const val SetUserDivisionName = "SetUserDivisionName"
        private const val SetUserZoneName = "SetUserZoneName"
        private const val EMAILID = "EmailId"
        private const val SetParentUnionName = "SetParentUnionName"
        private const val SetQRCode = "QRCode"
        private const val SELECTEDDATA = "SELECTEDDATA"
        private const val DesignationName = "DesignationName"
        private const val LOCALE = "LOCALE"
        private const val LANGUAGE_ID = "LANG_ID"
        private const val QUIZ = "QUIZ"
        private const val FIRSTPOPQUIZ = "FIRSTPOP"

        // Varible For admin check on Login/Signin API
        private const val ISADMIN = "ISADMIN"
        private const val ISSTUDENT = "ISSTUDENT"

        // Varible For canPublishMedia check on Login/Signin API
        private const val CAN_PUBLISH_MEDIA = "CAN_PUBLISH_MEDIA"
        private const val AGE = "AGE"

        //variable for finance person check on login/sign in API
        private const val FINANCE_PERSON = "FINANCE_PERSON"

        // Variable for Sending Firebase Token to Back end server
        private const val FBTOKEN = "FBTOKEN"

        // User Email
        private const val EMAIL = "EMAIL"
        private const val UserCountryCode = "UserCountryCode"

        //Fragment Redirection
        private const val FragmentRedirection = "PageRedirection"

        private const val SETZONEID = "SETZONEID"
        private const val DIVISIONID = "DIVISIONID"
        private const val BRANCHID = "BRANCHID"
        private const val Zone = "Zone"
        private const val DIVISION = "DIVISION"
        private const val BRANCH = "BRANCH"
        private const val LAST_POPUP_DATE = "lastPopupDate"

        private const val MOBILE = ArdsConstant.MOBILE


        private var preferenceManager: PreferenceHelper? = null

        fun getInstance(context: Context): PreferenceHelper {
            if (preferenceManager == null) {
                preferenceManager = PreferenceHelper(context)
            }
            return preferenceManager as PreferenceHelper
        }
    }

    // Save Data
    fun putString(key: String, value: String) {
        preference.edit().putString(key, value).apply()
    }

    fun putInt(key: String, value: Int) {
        preference.edit().putInt(key, value).apply()
    }

    fun putBoolean(key: String, value: Boolean) {
        preference.edit().putBoolean(key, value).apply()
    }

    fun setPhone(phone: String) {
        preference.edit().putString(PHONE, phone).apply()
    }

    val getPhone: String
        get() = preference.getString(PHONE, "").toString()


    fun setUserId(userId: Int) {
        preference.edit().putInt(USERID, userId).apply()
    }

    val getUserId: Int
        get() = preference.getInt(USERID, 0)

    fun setSelectedId(selectedId: Int) {
        preference.edit().putInt(SELECTEDID, selectedId).apply()
    }

    val getGetselectdId: Int
        get() = preference.getInt(SELECTEDID, 0)

    fun setUserName(username: String) {
        preference.edit().putString(USERNAME, username).apply()
    }

    val getUserName: String
        get() = preference.getString(USERNAME, "").toString()

    fun setParentUnionName(parentunion: String) {
        preference.edit().putString(SetParentUnionName, parentunion).apply()
    }
    val getParentUnionName: String
        get() = preference.getString(SetParentUnionName, "").toString()
    fun setFathername(fathername: String) {
        preference.edit().putString(Fathername, fathername).apply()
    }

    val getFathername: String
        get() = preference.getString(Fathername, "").toString()

    fun setAuthToken(authToken: String) {
        preference.edit().putString(TOKEN, authToken).apply()
    }

    val getAuthToken: String
        get() = preference.getString(TOKEN, "").toString()


    fun setUserImage(profile: String) {
        preference.edit().putString(PROFILE, profile).apply()
    }

    val getUserImage: String
        get() = preference.getString(PROFILE, "").toString()
    fun setUserAddress(address: String) {
        preference.edit().putString(Address, address).apply()
    }

    fun setRoleId(roleId: Int) {
        preference.edit().putInt(ROLEID, roleId).apply()
    }

    fun setUserBranchName(userBranch: String) {
        preference.edit().putString(SetUserBranchName, userBranch).apply()
    }

    fun setUserBranchID(userBranchID: Int) {
        preference.edit().putInt(SetUserBranchID, userBranchID).apply()
    }

    fun setEmailId(email: String) {
        preference.edit().putString(EMAILID, email).apply()
    }

    fun setUserAge(age: Int) {
        preference.edit().putInt(AGE, age).apply()
    }

    val getUserAge: Int = preference.getInt(AGE, 0)

    fun setAdmin(admin: Int) {
        preference.edit().putInt(ISADMIN, admin).apply()
    }

    val isAdmin: Int
        get() = preference.getInt(ISADMIN, 0)

    fun setStudent(student: Int) {
        preference.edit().putInt(ISSTUDENT, student).apply()
    }

    val isStudent: Int
        get() = preference.getInt(ISSTUDENT, 0)
    val getEmailId: String
        get() = preference.getString(EMAILID, "").toString()
    fun setUserDivisionID(userDivisionID: Int) {
        preference.edit().putInt(SetUserDivisionID, userDivisionID).apply()
    }

    fun setUserZoneID(userZoneID: Int) {
        preference.edit().putInt(SetUserZoneID, userZoneID).apply()
    }

    val getUserBranchName: String
        get() = preference.getString(SetUserBranchName, "").toString()

    val getUserBranchID: Int
        get() = preference.getInt(SetUserBranchID, 0)

    val getUserDivisionID: Int
        get() = preference.getInt(SetUserDivisionID, 0)

    val getUserZoneID: Int
        get() = preference.getInt(SetUserZoneID, 0)

    val getUserDivisionName: String
        get() = preference.getString(SetUserDivisionName, "").toString()

    val getUserZoneName: String
        get() = preference.getString(SetUserZoneName, "").toString()

    fun setUserDivisionName(userDivision: String) {
        preference.edit().putString(SetUserDivisionName, userDivision).apply()
    }

    fun setUserZoneName(userZone: String) {
        preference.edit().putString(SetUserZoneName, userZone).apply()
    }
    val getRoleId: Int
        get() = preference.getInt(ROLEID, 0)

    val getUserAddress: String
        get() = preference.getString(Address, "").toString()

    fun setParentId(selectedId: Int) {
        preference.edit().putInt(ParentUnionId, selectedId).apply()
    }

    val getParentId: Int
        get() = preference.getInt(ParentUnionId, 0)
    fun setFULLName(fullname: String) {
        preference.edit().putString(FULLNAME, fullname).apply()
    }

    val getFullName: String
        get() = preference.getString(FULLNAME, "").toString()

    fun setDOB(dob: String) {
        preference.edit().putString(DOB, dob).apply()
    }

    val getDOB: String
        get() = preference.getString(DOB, "").toString()

    fun setpfnumber(pfnumber: String) {
        preference.edit().putString(PFNUMBER, pfnumber).apply()
    }

    val getpfnumber: String
        get() = preference.getString(PFNUMBER, "").toString()

    fun setdesignation(designation: String) {
        preference.edit().putString(DesignationName, designation).apply()
    }

    val getdesignation: String
        get() = preference.getString(DesignationName, "").toString()

    // Retrieve Data
    fun getString(key: String, defaultValue: String = ""): String {
        return preference.getString(key, defaultValue) ?: defaultValue
    }

    fun getInt(key: String, defaultValue: Int = 0): Int {
        return preference.getInt(key, defaultValue)
    }

    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return preference.getBoolean(key, defaultValue)
    }

    // Remove a specific key
    fun remove(key: String) {
        preference.edit().remove(key).apply()
    }

    // Clear all preferences
    fun clear() {
        preference.edit().clear().apply()
    }
}
