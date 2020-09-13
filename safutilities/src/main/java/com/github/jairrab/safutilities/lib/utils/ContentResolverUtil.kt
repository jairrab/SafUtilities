package com.github.jairrab.safutilities.lib.utils

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
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

    fun getContentUriData(uri: Uri, projection: String): String? {
        return getCursor(
            uri = uri,
            projection = arrayOf(projection)
        )?.use {
            if (it.moveToFirst()) {
                it.getString(it.getColumnIndex(projection))
            } else null
        }
    }

    fun getData(uri: Uri): String? {
        return getCursor(
            uri = uri,
            projection = arrayOf("_data"),
        )?.use { if (it.moveToFirst()) it.getString(it.getColumnIndex("_data")) else null }
    }

    fun getData(uri: Uri, selection: String, selectionArgs: Array<String>): String? {
        return getCursor(
            uri = uri,
            projection = arrayOf("_data"),
            selection = selection,
            selectionArgs = selectionArgs
        )?.use { if (it.moveToFirst()) it.getString(it.getColumnIndex("_data")) else null }
    }

    fun getURISize(uri: Uri): Long {
        return getCursor(
            uri = uri,
            projection = arrayOf(OpenableColumns.SIZE)
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                cursor.getLong(cursor.getColumnIndex(OpenableColumns.SIZE))
            } else -1L
        } ?: -1L
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
            context.contentResolver.openFileDescriptor(uri, "r")
                ?.use { FileInputStream(it.fileDescriptor) }
        } else {
            getInputStream(uri) as? FileInputStream
        }
    }

    fun getInputStream(uri: Uri): InputStream? {
        return context.contentResolver.openInputStream(uri)
    }

    fun getOutputStream(uri: Uri): OutputStream? {
        return context.contentResolver.openOutputStream(uri)
    }
}