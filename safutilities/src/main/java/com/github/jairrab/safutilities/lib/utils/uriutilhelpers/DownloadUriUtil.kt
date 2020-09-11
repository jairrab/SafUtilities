package com.github.jairrab.safutilities.lib.utils.uriutilhelpers

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment.getExternalStorageDirectory
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.TextUtils

internal class DownloadUriUtil(
    private val dataColumnUtil: DataColumnUtil,
    private val context: Context
) {
    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.<br></br>
     * <br></br>
     * Callers should check whether the path is local before assuming it
     * represents a local file.
     *
     * @param uri     The Uri to query.
     */
    @SuppressLint("NewApi")
    fun getPath(uri: Uri): String? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var cursor: Cursor? = null

            try {
                cursor = context.contentResolver.query(
                    uri,
                    arrayOf(MediaStore.MediaColumns.DISPLAY_NAME),
                    null,
                    null,
                    null
                )
                if (cursor != null && cursor.moveToFirst()) {
                    val fileName = cursor.getString(0)
                    val path = "${getExternalStorageDirectory()}/Download/$fileName"
                    if (!TextUtils.isEmpty(path)) {
                        return path
                    }
                }
            } finally {
                cursor?.close()
            }

            val id = DocumentsContract.getDocumentId(uri)

            if (!TextUtils.isEmpty(id)) {
                if (id.startsWith("raw:")) {
                    return id.replaceFirst("raw:".toRegex(), "")
                }

                val contentUriPrefixesToTry =
                    arrayOf(
                        "content://downloads/public_downloads",
                        "content://downloads/my_downloads"
                    )

                for (contentUriPrefix in contentUriPrefixesToTry) {
                    return try {
                        val contentUri = ContentUris.withAppendedId(
                            Uri.parse(contentUriPrefix),
                            java.lang.Long.valueOf(id)
                        )
                        dataColumnUtil.getDataColumn(
                            uri = contentUri,
                            selection = null,
                            selectionArgs = null
                        )
                    } catch (e: NumberFormatException) { //In Android 8 and Android P the id is not a number
                        uri.path!!.replaceFirst("^/document/raw:".toRegex(), "")
                            .replaceFirst("^raw:".toRegex(), "")
                    }
                }

                return null

            } else return null

        } else {
            val id = DocumentsContract.getDocumentId(uri)

            if (id.startsWith("raw:")) {
                return id.replaceFirst("raw:".toRegex(), "")
            }

            val contentUri = ContentUris.withAppendedId(
                Uri.parse("content://downloads/public_downloads"),
                id.toLong()
            )

            return dataColumnUtil.getDataColumn(
                uri = contentUri,
                selection = null,
                selectionArgs = null
            )
        }
    }
}
