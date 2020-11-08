package com.github.jairrab.safutilities.lib.utils

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import android.util.Log
import com.github.jairrab.safutilities.SafUtilities.Companion.LOG_TAG
import java.io.FileInputStream
import java.io.InputStream
import java.io.OutputStream

internal class ContentResolverUtil(
    private val context: Context
) {
    fun getContentUriFileName(uri: Uri): String? {
        return getCursor(
            uri = uri,
            projection = arrayOf(OpenableColumns.DISPLAY_NAME)
        )?.use {
            if (it.moveToFirst()) {
                it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            } else null
        }
    }

    fun getContentUriSize(uri: Uri): Long {
        return getCursor(
            uri = uri,
            projection = arrayOf(OpenableColumns.SIZE)
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                cursor.getLong(cursor.getColumnIndex(OpenableColumns.SIZE))
            } else -1L
        } ?: -1L
    }

    fun getContentUriInfo(uri: Uri, projection: String): String? {
        return getCursor(
            uri = uri,
            projection = arrayOf(projection)
        )?.use {
            if (it.moveToFirst()) {
                it.getString(it.getColumnIndex(projection))
            } else null
        }
    }

    fun getContentUriData(uri: Uri): String? {
        return getCursor(
            uri = uri,
            projection = arrayOf("_data"),
        )?.use { if (it.moveToFirst()) it.getString(it.getColumnIndex("_data")) else null }
    }

    fun getContentUriData(
        uri: Uri,
        selection: String,
        selectionArgs: Array<String>
    ): String? {
        return getCursor(
            uri = uri,
            projection = arrayOf("_data"),
            selection = selection,
            selectionArgs = selectionArgs
        )?.use { if (it.moveToFirst()) it.getString(it.getColumnIndex("_data")) else null }
    }

    fun getCursor(
        uri: Uri,
        projection: Array<String>? = null,
        selection: String? = null,
        selectionArgs: Array<String>? = null,
        sortOrder: String? = null
    ): Cursor? {
        return context.contentResolver.query(
            uri,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )
    }

    fun getFileInputStream(uri: Uri): FileInputStream? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val fileDescriptor = context.contentResolver.openFileDescriptor(uri, "r")
            if (fileDescriptor != null) {
                fileDescriptor.use {
                    Log.v(LOG_TAG, "Returning inputStream from given uri | $uri")
                    FileInputStream(it.fileDescriptor)
                }
            } else {
                Log.e(LOG_TAG, "Invalid uri")
                null
            }
        } else {
            getInputStream(uri) as? FileInputStream
        }
    }

    fun getInputStream(uri: Uri): InputStream? {
        val inputStream = context.contentResolver.openInputStream(uri)
        return if (inputStream != null) {
            Log.v(LOG_TAG, "Returning inputStream from given uri | $uri")
            inputStream
        } else {
            Log.e(LOG_TAG, "Invalid uri")
            null
        }
    }

    fun getOutputStream(uri: Uri): OutputStream? {
        val outputStream = context.contentResolver.openOutputStream(uri)
        return if (outputStream != null) {
            Log.v(LOG_TAG, "Returning outputStream from given uri | $uri")
            outputStream
        } else {
            Log.e(LOG_TAG, "Invalid uri")
            null
        }
    }
}