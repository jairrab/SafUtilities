package com.github.jairrab.safutilities.lib.utils.appstorageutils

import android.content.Context
import android.net.Uri
import android.util.Log
import com.github.jairrab.fileutilities.FileUtilities
import com.github.jairrab.safutilities.SafUtilities.Companion.LOG_TAG
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
    suspend fun saveFileToContentUri(inputFile: File, outputUri: Uri) {
        withContext(Dispatchers.IO) {
            val outputStream = contentResolverUtil.getOutputStream(outputUri)
            if (outputStream != null) {
                FileUtilities.copyFile(inputFile, outputStream)
                Log.v(LOG_TAG, "Saving $inputFile to $outputUri")
            } else {
                Log.e(LOG_TAG, "Invalid $outputUri")
            }
        }
    }

    /**
     * Android 10 compliant approach to copying from a URI selected using SAF to
     * a file stored in the app's directory
     */
    // read https://medium.com/@sriramaripirala/android-10-open-failed-eacces-permission-denied-da8b630a89df
    suspend fun saveContentUriToDirectory(
        inputUri: Uri,
        outputDirectory: File,
        authority: String
    ): Uri? {
        val file = saveContentUriToDirectory(inputUri, outputDirectory)
        return if (file != null) {
            Log.v(LOG_TAG, "Saving $inputUri to $outputDirectory")
            return fileProviderUtil.getFileProviderUri(authority, file)
        } else {
            Log.e(LOG_TAG, "Invalid $inputUri")
            null
        }
    }

    suspend fun saveContentUriToDirectory(inputUri: Uri, outputDirectory: File): File? {
        return withContext(Dispatchers.IO) {
            val inputStream = contentResolverUtil.getInputStream(inputUri)
            if (inputStream != null) {
                val fileName = contentResolverUtil.getContentUriFileName(inputUri)
                if (fileName != null) {
                    Log.v(LOG_TAG, "Saving $inputUri to $outputDirectory")
                    FileUtilities.copyFileToDirectory(inputStream, outputDirectory, fileName)
                } else {
                    Log.e(LOG_TAG, "Invalid $inputUri")
                    null
                }
            } else {
                Log.e(LOG_TAG, "Invalid $inputUri")
                null
            }
        }
    }

    suspend fun saveContentUriToFile(inputUri: Uri, outputFile: File, authority: String): Uri? {
        val file = saveContentUriToFile(inputUri, outputFile)
        return if (file != null) {
            Log.v(LOG_TAG, "Saving $inputUri to $outputFile")
            fileProviderUtil.getFileProviderUri(authority, file)
        } else {
            Log.e(LOG_TAG, "Invalid $inputUri")
            null
        }
    }

    suspend fun saveContentUriToFile(inputUri: Uri, outputFile: File): File? {
        return withContext(Dispatchers.IO) {
            val inputStream = contentResolverUtil.getInputStream(inputUri)
            if (inputStream != null) {
                Log.v(LOG_TAG, "Saving $inputUri to $outputFile")
                FileUtilities.copyFile(inputStream, outputFile)
            } else {
                Log.e(LOG_TAG, "Invalid $inputUri")
                null
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