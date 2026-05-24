package com.laurentiu.lostpaws.data.session

import android.content.Context

class SessionManager(context: Context) {
    private val preferences = context.applicationContext.getSharedPreferences(
        "lost_paws_session",
        Context.MODE_PRIVATE
    )

    fun saveSession(userId: Long, email: String, fullName: String) {
        preferences.edit()
            .putBoolean(KEY_LOGGED_IN, true)
            .putLong(KEY_USER_ID, userId)
            .putString(KEY_USER_EMAIL, email)
            .putString(KEY_USER_FULL_NAME, fullName)
            .apply()
    }

    fun clearSession() {
        preferences.edit()
            .remove(KEY_LOGGED_IN)
            .remove(KEY_USER_ID)
            .remove(KEY_USER_EMAIL)
            .remove(KEY_USER_FULL_NAME)
            .apply()
    }

    fun isLoggedIn(): Boolean = preferences.getBoolean(KEY_LOGGED_IN, false)

    fun getUserId(): Long = preferences.getLong(KEY_USER_ID, 0L)

    fun getUserEmail(): String = preferences.getString(KEY_USER_EMAIL, "").orEmpty()

    fun getUserFullName(): String = preferences.getString(KEY_USER_FULL_NAME, "").orEmpty()

    fun saveSelectedStatusFilter(status: String) {
        preferences.edit().putString(KEY_STATUS_FILTER, status).apply()
    }

    fun getSelectedStatusFilter(): String = preferences.getString(KEY_STATUS_FILTER, "ALL") ?: "ALL"

    private companion object {
        const val KEY_LOGGED_IN = "isLoggedIn"
        const val KEY_USER_ID = "userId"
        const val KEY_USER_EMAIL = "userEmail"
        const val KEY_USER_FULL_NAME = "userFullName"
        const val KEY_STATUS_FILTER = "selectedStatusFilter"
    }
}
