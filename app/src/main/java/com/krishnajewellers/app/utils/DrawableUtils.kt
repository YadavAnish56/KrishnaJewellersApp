package com.krishnajewellers.app.utils

import android.content.Context
import com.krishnajewellers.app.R

object DrawableUtils {
    fun getDrawableResId(context: Context, resName: String): Int {
        val id = context.resources.getIdentifier(resName, "drawable", context.packageName)
        return if (id != 0) id else R.drawable.ic_kj_logo
    }
}
