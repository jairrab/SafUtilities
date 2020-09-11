package com.github.jairrab.safutilities.lib.utils.uriutilhelpers

import android.content.Context
import android.database.Cursor
import android.net.Uri

internal class DataColumnUtil(
    private val context: Context
) {
    fun getDataColumn(
        uri: Uri,
        selection: String? = null,
        selectionArgs: Array<String>? = null
    ): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)
        try {
            cursor = context.contentResolver.query(
                uri,
                projection,
                selection,
                selectionArgs,
                null
            )
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

}
