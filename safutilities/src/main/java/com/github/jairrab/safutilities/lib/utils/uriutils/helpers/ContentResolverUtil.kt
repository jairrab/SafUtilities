package com.github.jairrab.safutilities.lib.utils.uriutils.helpers

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils

class ContentResolverUtil(
    private val context: Context
) {
    fun getDisplayName(uri: Uri): String? {
        return getCursor(
            uri = uri,
            projection = arrayOf(MediaStore.MediaColumns.DISPLAY_NAME),
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                val fileName = cursor.getString(0)
                val path = "${Environment.getExternalStorageDirectory()}/Download/$fileName"
                if (!TextUtils.isEmpty(path)) {
                    path
                } else null
            } else null
        }
    }

    fun getData(uri: Uri):String?{
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
}