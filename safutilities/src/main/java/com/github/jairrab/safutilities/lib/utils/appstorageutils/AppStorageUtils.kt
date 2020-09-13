package com.github.jairrab.safutilities.lib.utils.appstorageutils

import android.content.Context
import android.net.Uri
import com.github.jairrab.fileutilities.FileUtilities
import com.github.jairrab.safutilities.lib.FileProviderUtil
import com.github.jairrab.safutilities.lib.utils.ContentResolverUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

internal class AppStorageUtils private constructor(
    private val contentResolverUtil: ContentResolverUtil,
    private val context: Context,
    private val fileProviderUtil: FileProviderUtil,
) {
    /**
     * Android 10 compliant approach to copying from a URI selected using SAF to
     * a file stored in the app's directory
     */
    // read https://medium.com/@sriramaripirala/android-10-open-failed-eacces-permission-denied-da8b630a89df
    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun copyUriToDirectory(uri: Uri, destination: File, authority: String): Uri? {
        return contentResolverUtil.getContentUriFileName(uri)?.let { child ->
            withContext(Dispatchers.Default) {
                contentResolverUtil.getFileInputStream(uri)?.let {
                    FileUtilities.copyFileToDirectory(it, destination, child)
                }
            }
        }?.let { fileProviderUtil.getFileProviderUri(authority, it) }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun copyUriToFile(uri: Uri, destination: File, authority: String): Uri? {
        return withContext(Dispatchers.Default) {
            contentResolverUtil.getFileInputStream(uri)?.let {
                FileUtilities.copyFile(it, destination)
            }
        }?.let { fileProviderUtil.getFileProviderUri(authority, it) }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun copyToExternalStorage(fileToCopy: File, destinationUri: Uri) {
        withContext(Dispatchers.IO) {
            contentResolverUtil.getOutputStream(destinationUri)?.use { os ->
                FileUtilities.copyFile(fileToCopy, os)
            }
        }
    }

    fun deleteAllUserFiles() {
        context.getExternalFilesDir(null)?.let {
            FileUtilities.deleteDirectoryFiles(it)
        }
    }


    companion object {
        fun getInstance(
            context: Context,
            contentResolverUtil: ContentResolverUtil,
            fileProviderUtil: FileProviderUtil
        ): AppStorageUtils {
            return AppStorageUtils(
                context = context,
                contentResolverUtil = contentResolverUtil,
                fileProviderUtil = fileProviderUtil
            )
        }
    }
}