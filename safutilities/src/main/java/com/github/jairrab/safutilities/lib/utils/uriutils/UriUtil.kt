package com.github.jairrab.safutilities.lib.utils.uriutils

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract.isDocumentUri
import androidx.annotation.AnyRes
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import com.github.jairrab.safutilities.lib.utils.uriutils.helpers.*
import java.io.File
import java.io.FileInputStream

class UriUtil private constructor(
    private val context: Context,
    private val appDirectoryUriUtil: AppDirectoryUriUtil,
    private val downloadUriUtil: DownloadUriUtil,
    private val externalStorageUtil: ExternalStorageUtil,
    private val mediaDocumentUtil: MediaDocumentUtil,
    private val mediaFilePathForNUtil: MediaFilePathForNUtil,
    private val gDriveUriUtil: GDriveUriUtil,
) {
    /**
     * Android 10 compliant approach to copying from a URI selected using SAF to
     * a file stored in the app's directory
     */
    suspend fun copyUriToDirectory(uri: Uri?, outputDir: File?): File? {
        return appDirectoryUriUtil.copyUriToDirectory(uri, outputDir).also {
            if (outputDir?.exists() == true) {
                println("^^^ copied uri:${uri?.lastPathSegment} to directory:${outputDir.name}")
            }
        }
    }

    suspend fun copyUriToFile(uri: Uri?, file: File): File? {
        return appDirectoryUriUtil.copyUriToFile(uri, file)
    }

    fun getFileNameFromContentUri(uri: Uri?): String {
        return appDirectoryUriUtil.getFileNameFromUri(uri)
    }

    fun getFile(uri: Uri): File? {
        return getPath(uri)?.let { File(it) }
    }

    fun getFileProviderUri(authority: String, file: File): Uri {
        return FileProvider.getUriForFile(context, authority, file)
    }

    fun getFileInputStream(uri: Uri): FileInputStream? {
        return appDirectoryUriUtil.getFileInputStream(uri)
    }

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
            isApi19() && isDocumentUri(context, u) -> when {
                isExternalStorageDocument(u) -> externalStorageUtil.getPath(u)
                    .also { println("^^^ isExternalStorageDocument uri: $it") }
                isDownloadsDocument(u) -> downloadUriUtil.getPath(u)
                    .also { println("^^^ isDownloadsDocument uri: $it") }
                isMediaDocument(u) -> mediaDocumentUtil.getPath(u)
                    .also { println("^^^ isMediaDocument uri: $it") }
                else -> null
            }

            "content".equals(u.scheme, true) -> when {
                isGooglePhotosUri(u) -> u.lastPathSegment.also { println("^^^ isGooglePhotosUri uri: $it") }
                isApi24() -> mediaFilePathForNUtil.getPath(u)
                    .also { println("^^^ isApi24 uri: $it") }
                else -> u.path
                //else                 -> u.path.also { println("^^^ content uri: $it") }
            }

            "file".equals(u.scheme, true) -> u.path
            //"file".equals(u.scheme, true)          -> u.path.also { println("^^^ file uri: $it") }

            else -> null.also { println("^^^ uri path is null") }
        }
    } ?: null.also { println("^^^ uri path is null") }

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
            isApi19() && isDocumentUri(context, u) -> when {
                isExternalStorageDocument(u) -> externalStorageUtil.getPath(u)
                    .also { println("^^^ isExternalStorageDocument uri: $it") }
                isDownloadsDocument(u) -> downloadUriUtil.getPath(u)
                    .also { println("^^^ isDownloadsDocument uri: $it") }
                isMediaDocument(u) -> mediaDocumentUtil.getPath(u)
                    .also { println("^^^ isMediaDocument uri: $it") }
                isGoogleDriveUri(u) -> gDriveUriUtil.getPath(u)
                    .also { println("^^^ document isGoogleDriveUri uri: $it") }
                else -> null
            }

            "content".equals(u.scheme, true) -> when {
                isGooglePhotosUri(u) -> u.lastPathSegment.also { println("^^^ isGooglePhotosUri uri: $it") }
                isGoogleDriveUri(u) -> gDriveUriUtil.getPath(u)
                    .also { println("^^^ content isGoogleDriveUri uri: $it") }
                isApi24() -> mediaFilePathForNUtil.getPath(u)
                    .also { println("^^^ isApi24 uri: $it") }
                //else                 -> dataColumnUtil.getDataColumn(u).also { println("^^^ content uri: $it") }
                else -> u.path.also { println("^^^ content uri: $it") }
            }

            "file".equals(u.scheme, true) -> u.path.also { println("^^^ file uri: $it") }

            else -> null.also { println("^^^ uri path is null") }
        }
    } ?: null.also { println("^^^ uri path is null") }

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
        fun getInstance(context: Context): UriUtil {
            val contentResolverUtil = ContentResolverUtil(context)
            return UriUtil(
                context = context,
                appDirectoryUriUtil = AppDirectoryUriUtil(context),
                downloadUriUtil = DownloadUriUtil(contentResolverUtil),
                externalStorageUtil = ExternalStorageUtil(),
                mediaDocumentUtil = MediaDocumentUtil(contentResolverUtil),
                mediaFilePathForNUtil = MediaFilePathForNUtil(contentResolverUtil, context),
                gDriveUriUtil = GDriveUriUtil(contentResolverUtil, context),
            )
        }
    }
}