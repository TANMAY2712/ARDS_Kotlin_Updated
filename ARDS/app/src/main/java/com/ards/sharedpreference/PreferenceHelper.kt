package com.ards.sharedpreference


import android.content.Context
import android.content.SharedPreferences

object PreferenceHelper {
    private const val PREF_NAME = "app_preferences"

    private lateinit var preferences: SharedPreferences

    // Initialize in Application class or before use
    fun init(context: Context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    // Save Data
    fun putString(key: String, value: String) {
        preferences.edit().putString(key, value).apply()
    }

    fun putInt(key: String, value: Int) {
        preferences.edit().putInt(key, value).apply()
    }

    fun putBoolean(key: String, value: Boolean) {
        preferences.edit().putBoolean(key, value).apply()
    }

    // Retrieve Data
    fun getString(key: String, defaultValue: String = ""): String {
        return preferences.getString(key, defaultValue) ?: defaultValue
    }

    fun getInt(key: String, defaultValue: Int = 0): Int {
        return preferences.getInt(key, defaultValue)
    }

    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return preferences.getBoolean(key, defaultValue)
    }

    // Remove a specific key
    fun remove(key: String) {
        preferences.edit().remove(key).apply()
    }

    // Clear all preferences
    fun clear() {
        preferences.edit().clear().apply()
    }
}
