package com.yeongil.focusaid.dataSource.user

import android.content.Context
import android.content.SharedPreferences

class UserInfoPref(context: Context) {
    companion object {
        private const val EMAIL_KEY = "EMAIL"
        private const val USER_NAME_KEY = "USER_NAME"
    }

    private val sharedPref: SharedPreferences =
        context.getSharedPreferences(
            "com.yeongil.focusaid.USER",
            Context.MODE_PRIVATE
        )

    fun getUserInfo(): UserInfoDto? {
        val userName = sharedPref.getString(USER_NAME_KEY, null)
        val email = sharedPref.getString(EMAIL_KEY, null)

        if (userName == null || email == null) return null

        return UserInfoDto(email, userName)
    }
}