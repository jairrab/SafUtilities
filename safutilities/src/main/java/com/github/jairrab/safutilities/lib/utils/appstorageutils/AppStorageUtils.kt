package com.github.jairrab.safutilities.lib.utils.appstorageutils

import android.content.Context
import android.net.Uri
import com.github.jairrab.safutilities.lib.FileProviderUtil
import com.github.jairrab.safutilities.lib.utils.ContentResolverUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

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
        return withContext(Dispatchers.Default) {
            if (!destination.exists()) {
                destination.mkdirs()
            }
            contentResolverUtil.getContentUriFileName(uri)?.let { child ->
                withContext(Dispatchers.Default) {
                    contentResolverUtil.getFileInputStream(uri)?.let { inputStream ->
                        val outputFile = File(destination, child)
                        inputStream.copyTo(FileOutputStream(outputFile))
                        outputFile
                    }
                }
            }
        }?.let { fileProviderUtil.getFileProviderUri(authority, it) }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun copyUriToFile(uri: Uri, destination: File, authority: String): Uri? {
        return withContext(Dispatchers.Default) {
            contentResolverUtil.getFileInputStream(uri)?.let { inputStream ->
                inputStream.copyTo(FileOutputStream(destination))
                destination
            }
        }?.let { fileProviderUtil.getFileProviderUri(authority, it) }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun copyToExternalStorage(fileToCopy: File, destinationUri: Uri) {
        withContext(Dispatchers.IO) {
            contentResolverUtil.getOutputStream(destinationUri)?.use { os ->
                os.write(fileToCopy.readBytes())
            }
        }
    }

    fun deleteAllUserFiles() = try {
        val externalFilesDir = context.getExternalFilesDir(null)
        externalFilesDir?.deleteRecursively()
    } catch (e: Exception) {
        e.printStackTrace()
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