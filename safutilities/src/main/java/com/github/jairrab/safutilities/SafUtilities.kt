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

interface SafUtilities {
    fun getContentUri(file: File, authority: String): Uri

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun pickFile(
        fragment: Fragment,
        requestCode: Int,
        mimeType: MimeType,
        pickerInitialUri: Uri? = null
    )

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
    fun openDirectory(
        fragment: Fragment,
        pickerInitialUri: Uri? = null,
        requestCode: Int
    )

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    suspend fun copyToAppFolder(uri: Uri?, destination: File, authority: String): Uri?

    suspend fun copyToExternalStorage(fileToCopy: File, destinationUri: Uri)

    fun getFileNameFromContentUri(uri: Uri?): String

    fun getFile(uri: Uri): File?

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun getFileInputStream(uri: Uri): FileInputStream?

    companion object {
        fun getInstance(context: Context): SafUtilities {
            return SafUtilitiesLibrary.getInstance(context)
        }
    }
}

