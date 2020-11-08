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
    /** Return a content URI for a given File. */
    fun getContentUri(file: File, authority: String): Uri

    /** Picks file using SAF. */
    fun pickFile(
        fragment: Fragment,
        requestCode: Int,
        mimeType: MimeType,
        initialUri: Uri? = null
    )

    /**
     * Opens save dialog SAF file picker.
     * @param file File to save
     * @param defaultFileName Default file name or name of [file] if none provided
     */
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun openSavePicker(
        fragment: Fragment,
        requestCode: Int,
        file: File,
        defaultFileName: String? = null,
        pickerInitialUri: Uri? = null
    )

    /** Pick directory using SAF */
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun pickDirectory(
        fragment: Fragment,
        requestCode: Int,
        pickerInitialUri: Uri? = null,
    )

    /** Save given [inputFile] to [outputUri] that is of type content uri */
    suspend fun saveFileToContentUri(inputFile: File, outputUri: Uri)

    /**
     * Save given [inputUri] of type content uri to [outputDirectory]. Returns a
     * content uri provided by given [authority]
     **/
    suspend fun saveContentUriToDirectory(
        inputUri: Uri,
        outputDirectory: File,
        authority: String
    ): Uri?

    /** Save given [inputUri] of type content uri to [outputDirectory] */
    suspend fun saveContentUriToDirectory(
        inputUri: Uri,
        outputDirectory: File
    ): File?

    /**
     * Save given [inputUri] of type content uri to [outputFile]. Returns a
     * content uri provided by given [authority]
     **/
    suspend fun saveContentUriToFile(inputUri: Uri, outputFile: File, authority: String): Uri?

    /** Save given [inputUri] of type content uri to [outputFile] */
    suspend fun saveContentUriToFile(inputUri: Uri, outputFile: File): File?

    fun getContentUriFileName(uri: Uri): String?

    fun getContentUriData(uri: Uri, projection: String): String?

    fun getFile(uri: Uri): File?

    fun getFileInputStream(uri: Uri): FileInputStream?

    fun getInputStream(uri: Uri): InputStream?

    fun getOutputStream(uri: Uri): OutputStream?

    fun getUriSize(uri: Uri): Long

    fun deleteAllUserFiles()

    companion object {
        const val LOG_TAG = "SafUtilities"

        fun getInstance(context: Context): SafUtilities {
            return SafUtilitiesLibrary.getInstance(context)
        }
    }
}

