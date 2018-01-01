package de.ehsun.smartbooking.utils

import android.text.TextUtils
import android.util.Patterns

object ValidationUtil {
    fun isValidEmail(text: String) =
            !TextUtils.isEmpty(text) && Patterns.EMAIL_ADDRESS.matcher(text).matches()


}