package com.github.jairrab.safutilities.lib.utils.uriutilhelpers

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import androidx.annotation.RequiresApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

internal class ContentResolverUtil(
    private val context: Context
) {
    // read https://medium.com/@sriramaripirala/android-10-open-failed-eacces-permission-denied-da8b630a89df
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    suspend fun copyUriToDirectory(uri: Uri?, outputDir: File?): File? = coroutineScope {
        if (outputDir?.exists() == false) {
            outputDir.mkdirs()
                .also { println("^^^ creating directory $outputDir") }
        }
        val outputFile = File(outputDir, getFileNameFromUri(uri))
        copyUriToFile(uri, outputFile)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun copyUriToFile(uri: Uri?, outputFile: File): File? {
        return withContext(Dispatchers.Default) {
            uri?.let {
                getFileInputStream(it)?.let { inputStream ->
                    inputStream.copyTo(FileOutputStream(outputFile))
                    outputFile
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun getFileInputStream(uri: Uri): FileInputStream? {
        return context.contentResolver.openFileDescriptor(uri, "r", null)
            ?.let { FileInputStream(it.fileDescriptor) }
    }

    fun getFileNameFromUri(uri: Uri?): String {
        return context.contentResolver.getFileName(uri)
    }

    private fun ContentResolver.getFileName(fileUri: Uri?): String {
        return fileUri?.let {
            query(
                fileUri,
                null,
                null,
                null,
                null
            )?.let { cursor ->
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                cursor.moveToFirst()
                val n = cursor.getString(nameIndex)
                cursor.close()
                n
            } ?: ""
        } ?: ""
    }
}
