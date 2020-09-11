package com.github.jairrab.safutilities.lib.utils.uriutilhelpers

import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import android.provider.MediaStore
import androidx.annotation.RequiresApi

internal class MediaDocumentUtil(
    private val dataColumnUtil: DataColumnUtil
) {
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun getPath(uri: Uri): String? {
        val docId = DocumentsContract.getDocumentId(uri)
        val split = docId.split(":").toTypedArray()
        val contentUri = when (split[0]) {
            "image" -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            "video" -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            "audio" -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            else    -> return null
        }
        return dataColumnUtil.getDataColumn(
            uri = contentUri,
            selection = "_id=?",
            selectionArgs = arrayOf(split[1])
        )
    }
}
