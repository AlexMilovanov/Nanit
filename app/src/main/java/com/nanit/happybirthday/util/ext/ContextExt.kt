package com.nanit.happybirthday.util.ext

import android.content.Context
import com.nanit.happybirthday.NanitApp

val Context.myApplication: NanitApp
    get() = applicationContext as NanitApp