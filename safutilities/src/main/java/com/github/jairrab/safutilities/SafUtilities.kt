package com.github.jairrab.safutilities

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.github.jairrab.safutilities.lib.SafUtilitiesLibrary
import com.github.jairrab.safutilities.model.MimeType
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.io.OutputStream

interface SafUtilities {
    fun getContentUri(file: File, authority: String): Uri

    fun pickFile(fragment: Fragment, requestCode: Int, mimeType: MimeType, initialUri: Uri? = null)

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun createFile(
        fragment: Fragment,
        file: File,
        defaultFileName: String,
        requestCode: Int,
        authority: String,
        pickerInitialUri: Uri? = null
    )

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun openDirectory(fragment: Fragment, pickerInitialUri: Uri? = null, requestCode: Int)

    suspend fun copyUriToDirectory(uri: Uri, destination: File, authority: String): Uri?

    suspend fun copyUriToDirectory(uri: Uri, destination: File): File?

    suspend fun copyUriToFile(uri: Uri, destination: File, authority: String): Uri?

    suspend fun copyUriToFile(uri: Uri, destination: File): File?

    suspend fun copyToExternalStorage(fileToCopy: File, destinationUri: Uri)

    fun getContentUriFileName(uri: Uri): String?

    fun getContentUriData(uri: Uri, projection: String): String?

    fun getFile(uri: Uri): File?

    fun getFileInputStream(uri: Uri): FileInputStream?

    fun getInputStream(uri: Uri): InputStream?

    fun getOutputStream(uri: Uri): OutputStream?

    fun getUriSize(uri: Uri): Long

    fun deleteAllUserFiles()

    companion object {
        fun getInstance(context: Context): SafUtilities {
            return SafUtilitiesLibrary.getInstance(context)
        }
    }
}

