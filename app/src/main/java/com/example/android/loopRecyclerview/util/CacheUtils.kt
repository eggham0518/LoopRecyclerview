package com.example.android.loopRecyclerview.util

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Environment
import android.os.StatFs
import java.io.File

object CacheUtils {
    const val IO_BUFFER_SIZE = 8 * 1024

    @SuppressLint("NewApi")
    fun getExternalCacheDir(context: Context): File? {
        if (hasExternalCacheDir()) {
            return context.externalCacheDir
        }
        val cacheDir = "/Android/data/" + context.packageName + "/cache/"
        return File(Environment.getExternalStorageDirectory().path + cacheDir)
    }

    @SuppressLint("NewApi")
    fun getUsableSpace(path: File): Long {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return path.usableSpace
        }
        val stats = StatFs(path.path)
        return stats.blockSize.toLong() * stats.availableBlocks.toLong()
    }

    fun hasExternalCacheDir(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO
    }
}