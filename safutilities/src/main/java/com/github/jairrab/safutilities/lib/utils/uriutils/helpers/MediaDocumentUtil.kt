package com.github.jairrab.safutilities.lib.utils.uriutils.helpers

import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import com.github.jairrab.safutilities.lib.utils.ContentResolverUtil

internal class MediaDocumentUtil(
    private val contentResolverUtil: ContentResolverUtil
) {
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun getPath(uri: Uri): String? {
        val docId = DocumentsContract.getDocumentId(uri)
        val split = docId.split(":").toTypedArray()
        val contentUri = when (split[0]) {
            "image" -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            "video" -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            "audio" -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            else -> return null
        }
        return contentResolverUtil.getContentUriData(contentUri, "_id=?", arrayOf(split[1]))
    }
}
