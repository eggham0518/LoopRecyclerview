package com.example.android.devbyteviewer.util

import java.text.SimpleDateFormat
import java.util.*


val sdf = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'").apply {
    timeZone = TimeZone.getTimeZone("GMT")
}