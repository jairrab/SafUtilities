package com.github.jairrab.safutilities.lib.utils.uriutils

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import androidx.annotation.AnyRes
import com.github.jairrab.safutilities.lib.utils.ContentResolverUtil
import com.github.jairrab.safutilities.lib.utils.uriutils.helpers.*

internal class UriUtil private constructor(
    private val context: Context,
    private val downloadUriUtil: DownloadUriUtil,
    private val externalStorageUtil: ExternalStorageUtil,
    private val mediaDocumentUtil: MediaDocumentUtil,
    private val mediaFilePathForNUtil: MediaFilePathForNUtil,
    private val gDriveUriUtil: GDriveUriUtil,
) {
    fun getUriFromResource(@AnyRes resId: Int): Uri = Uri.parse(
        ContentResolver.SCHEME_ANDROID_RESOURCE +
            "://" + context.resources.getResourcePackageName(resId)
            + '/' + context.resources.getResourceTypeName(resId)
            + '/' + context.resources.getResourceEntryName(resId)
    )

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
    fun getPath(uri: Uri?) = uri?.let { u ->
        when {
            isApi19() && DocumentsContract.isDocumentUri(context, u) -> when {
                isExternalStorageDocument(u) -> externalStorageUtil.getPath(u)
                isDownloadsDocument(u) -> downloadUriUtil.getPath(u)
                isMediaDocument(u) -> mediaDocumentUtil.getPath(u)
                else -> null
            }

            "content".equals(u.scheme, true) -> when {
                isGooglePhotosUri(u) -> u.lastPathSegment
                isApi24() -> mediaFilePathForNUtil.getPath(u)
                else -> u.path
            }

            "file".equals(u.scheme, true) -> u.path

            else -> null
        }
    }

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
    suspend fun getPathWithGDrive(uri: Uri?) = uri?.let { u ->
        when {
            isApi19() && DocumentsContract.isDocumentUri(context, u) -> when {
                isExternalStorageDocument(u) -> externalStorageUtil.getPath(u)
                isDownloadsDocument(u) -> downloadUriUtil.getPath(u)
                isMediaDocument(u) -> mediaDocumentUtil.getPath(u)
                isGoogleDriveUri(u) -> gDriveUriUtil.getPath(u)
                else -> null
            }

            "content".equals(u.scheme, true) -> when {
                isGooglePhotosUri(u) -> u.lastPathSegment
                isGoogleDriveUri(u) -> gDriveUriUtil.getPath(u)
                isApi24() -> mediaFilePathForNUtil.getPath(u)
                else -> u.path
            }

            "file".equals(u.scheme, true) -> u.path

            else -> null
        }
    }

    private fun isApi24() = Build.VERSION.SDK_INT == Build.VERSION_CODES.N

    @SuppressLint("ObsoleteSdkInt")
    private fun isApi19() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

    /**
     * @param uri - The Uri to check.
     * @return - Whether the Uri authority is ExternalStorageProvider.
     */
    private fun isExternalStorageDocument(uri: Uri) =
        uri.authority == "com.android.externalstorage.documents"

    /**
     * @param uri - The Uri to check.
     * @return - Whether the Uri authority is DownloadsProvider.
     */
    private fun isDownloadsDocument(uri: Uri) =
        uri.authority == "com.android.providers.downloads.documents"

    /**
     * @param uri - The Uri to check.
     * @return - Whether the Uri authority is MediaProvider.
     */
    private fun isMediaDocument(uri: Uri) =
        uri.authority == "com.android.providers.media.documents"

    /**
     * @param uri - The Uri to check.
     * @return - Whether the Uri authority is Google Photos.
     */
    private fun isGooglePhotosUri(uri: Uri) =
        uri.authority == "com.google.android.apps.photos.content"

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Drive.
     */
    private fun isGoogleDriveUri(uri: Uri) =
        uri.authority == "com.google.android.apps.docs.storage" ||
            uri.authority == "com.google.android.apps.docs.storage.legacy"

    companion object {
        fun getInstance(context: Context, contentResolverUtil: ContentResolverUtil): UriUtil {
            return UriUtil(
                context = context,
                downloadUriUtil = DownloadUriUtil(contentResolverUtil),
                externalStorageUtil = ExternalStorageUtil(),
                mediaDocumentUtil = MediaDocumentUtil(contentResolverUtil),
                mediaFilePathForNUtil = MediaFilePathForNUtil(contentResolverUtil, context),
                gDriveUriUtil = GDriveUriUtil(contentResolverUtil, context),
            )
        }
    }
}