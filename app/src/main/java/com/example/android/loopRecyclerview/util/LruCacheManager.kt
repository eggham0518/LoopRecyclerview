package com.example.android.loopRecyclerview.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.os.Environment
import android.os.Environment.isExternalStorageRemovable
import com.example.android.loopRecyclerview.BuildConfig
import com.example.android.loopRecyclerview.ui.MainFragment.Companion.LIMIT_ITEM_COUNT
import timber.log.Timber
import java.io.*
import java.net.URLEncoder
import java.util.*

class LruCacheManager private constructor(private val mCacheDir: File, maxByteSize: Long) {
    private var cacheSize = 0
    private var cacheByteSize = 0
    private val maxCacheItemSize = LIMIT_ITEM_COUNT
    private var maxCacheByteSize = 1024 * 1024 * 5 // 5MB
            .toLong()
    private val mCompressFormat = CompressFormat.PNG
    private val mCompressQuality = 100
    private val mLinkedHashMap = Collections.synchronizedMap(LinkedHashMap<String, String?>(
            INITIAL_CAPACITY, LOAD_FACTOR, true))

    fun put(key: String, data: Bitmap?) {
        synchronized(mLinkedHashMap) {
            if (mLinkedHashMap[key] == null) {
                try {
                    val file = createFilePath(mCacheDir, key)
                    if (writeBitmapToFile(data!!, file)) {
                        put(key, file)
                        flushCache()
                    }
                } catch (e: FileNotFoundException) {
                } catch (e: IOException) {
                }
            }
        }
    }

    private fun put(key: String, file: String?) {
        mLinkedHashMap[key] = file
        cacheSize = mLinkedHashMap.size
        cacheByteSize += File(file).length().toInt()
    }

    private fun flushCache() {
        var eldestEntry: Map.Entry<String, String?>
        var eldestFile: File
        var eldestFileSize: Long
        var count = 0
        while (count < MAX_REMOVALS &&
                (cacheSize > maxCacheItemSize || cacheByteSize > maxCacheByteSize)) {
            eldestEntry = mLinkedHashMap.entries.iterator().next()
            eldestFile = File(eldestEntry.value)
            eldestFileSize = eldestFile.length()
            mLinkedHashMap.remove(eldestEntry.key)
            eldestFile.delete()
            cacheSize = mLinkedHashMap.size
            cacheByteSize -= eldestFileSize.toInt()
            count++
            if (BuildConfig.DEBUG) {
                Timber.d("removed File: $eldestFile")
            }
        }
    }

    operator fun get(key: String): Bitmap? {
        synchronized(mLinkedHashMap) {
            val file = mLinkedHashMap[key]
            if (file != null) {
                return BitmapFactory.decodeFile(file)
            } else {
                val existingFile = createFilePath(mCacheDir, key)
                if (File(existingFile).exists()) {
                    put(key, existingFile)
                    return BitmapFactory.decodeFile(existingFile)
                }
            }
            return null
        }
    }

    fun clearCache() {
        clearCache(mCacheDir)
    }

    @Throws(IOException::class, FileNotFoundException::class)
    private fun writeBitmapToFile(bitmap: Bitmap, file: String?): Boolean {
        var out: OutputStream? = null
        return try {
            out = BufferedOutputStream(FileOutputStream(file), CacheUtils.IO_BUFFER_SIZE)
            bitmap.compress(mCompressFormat, mCompressQuality, out)
        } finally {
            out?.close()
        }
    }

    companion object {
        private const val CACHE_FILENAME_PREFIX = "cache_"
        private const val MAX_REMOVALS = 4
        private const val INITIAL_CAPACITY = 32
        private const val LOAD_FACTOR = 0.75f
        private val cacheFileFilter = FilenameFilter { dir, filename -> filename.startsWith(CACHE_FILENAME_PREFIX) }

        fun openCache(cacheDir: File, maxByteSize: Long): LruCacheManager? {
            if (!cacheDir.exists()) {
                cacheDir.mkdir()
            }
            return if (cacheDir.isDirectory && cacheDir.canWrite()
                    && CacheUtils.getUsableSpace(cacheDir) > maxByteSize) {
                LruCacheManager(cacheDir, maxByteSize)
            } else null
        }



        fun clearCache(context: Context, uniqueName: String) {
            val cacheDir = getDiskCacheDir(context, uniqueName)
            clearCache(cacheDir)
        }

        private fun clearCache(cacheDir: File) {
            val files = cacheDir.listFiles(cacheFileFilter)
            for (i in files.indices) {
                files[i].delete()
            }
        }

        fun getDiskCacheDir(context: Context, uniqueName: String): File {
            val cachePath = if (Environment.getExternalStorageState() === Environment.MEDIA_MOUNTED ||
                    isExternalStorageRemovable()) CacheUtils.getExternalCacheDir(context)?.path else context.cacheDir.path
            return File(cachePath + File.separator + uniqueName)
        }

        fun createFilePath(cacheDir: File, key: String): String? {
            try {
                return cacheDir.absolutePath + File.separator +
                        CACHE_FILENAME_PREFIX + URLEncoder.encode(key.replace("*", ""), "UTF-8")
            } catch (e: UnsupportedEncodingException) {
            }
            return null
        }
    }

    init {
        maxCacheByteSize = maxByteSize
    }
}