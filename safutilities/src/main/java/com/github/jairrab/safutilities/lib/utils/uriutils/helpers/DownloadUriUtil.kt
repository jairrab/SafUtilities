package com.github.jairrab.safutilities.lib.utils.uriutils.helpers

import android.annotation.SuppressLint
import android.content.ContentUris
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.text.TextUtils
import com.github.jairrab.safutilities.lib.utils.ContentResolverUtil

internal class DownloadUriUtil(
    private val contentResolverUtil: ContentResolverUtil
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
            val displayName = contentResolverUtil.getContentUriFileName(uri)
                ?.let { "${Environment.getExternalStorageDirectory()}/Download/$it" }

            if (displayName != null) return displayName

            val id = DocumentsContract.getDocumentId(uri)

            if (!TextUtils.isEmpty(id)) {
                if (id.startsWith("raw:")) {
                    return id.replaceFirst("raw:".toRegex(), "")
                }

                val contentUriPrefixesToTry = arrayOf(
                    "content://downloads/public_downloads",
                    "content://downloads/my_downloads"
                )

                for (contentUriPrefix in contentUriPrefixesToTry) {
                    try {
                        val contentUri = ContentUris.withAppendedId(
                            Uri.parse(contentUriPrefix),
                            java.lang.Long.valueOf(id)
                        )

                        val path = contentResolverUtil.getData(contentUri)

                        if (path != null) return path

                    } catch (e: NumberFormatException) { //In Android 8 and Android P the id is not a number
                        return uri.path!!.replaceFirst("^/document/raw:".toRegex(), "")
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

            return contentResolverUtil.getData(contentUri)
        }
    }
}
