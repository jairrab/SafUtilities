package com.github.jairrab.safutilities.lib.utils

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class AppStorageUtils(
    private val context: Context,
    private val uriUtil: UriUtil
) {
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    suspend fun copyToAppFolder(uri: Uri?, destination: File, authority: String): Uri? {
        return uriUtil.copyUriToFile(uri, destination)
            ?.let { uriUtil.getFileProviderUri(authority, it) }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun copyToExternalStorage(fileToCopy: File, destinationUri: Uri) {
        withContext(Dispatchers.IO) {
            context.contentResolver.openOutputStream(destinationUri)
                ?.let { os ->
                    os.write(fileToCopy.readBytes())
                    os.close()
                }
        }
    }

    fun deleteAllUserFiles() = try {
        val externalFilesDir = context.getExternalFilesDir(null)
        externalFilesDir?.deleteRecursively()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}