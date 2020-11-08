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
    /** Picks file using SAF. */
    fun pickFile(
        fragment: Fragment,
        requestCode: Int,
        mimeType: MimeType,
        initialUri: Uri? = null
    )

    /** Pick directory using SAF */
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun pickDirectory(
        fragment: Fragment,
        requestCode: Int,
        pickerInitialUri: Uri? = null,
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

    /** Return a content URI for a given File. */
    fun getContentUri(file: File, authority: String): Uri

    /** Gets file name from given [uri] of type content uri */
    fun getContentUriFileName(uri: Uri): String?

    /** Gets file size from given [uri] of type content uri */
    fun getContentUriSize(uri: Uri): Long

    /** Gets uri info from given [uri] and column projection given by [projection] */
    fun getContentUriInfo(uri: Uri, projection: String): String?

    /** Gets [File] from given [uri] */
    fun getFile(uri: Uri): File?

    /** Gets [FileInputStream] from given [uri] */
    fun getFileInputStream(uri: Uri): FileInputStream?

    /** Gets [InputStream] from given [uri] */
    fun getInputStream(uri: Uri): InputStream?

    /** Gets [OutputStream] from given [uri] */
    fun getOutputStream(uri: Uri): OutputStream?

    /** Deletes all user files */
    fun deleteAllUserFiles()

    companion object {
        const val LOG_TAG = "SafUtilities"

        fun getInstance(context: Context): SafUtilities {
            return SafUtilitiesLibrary.getInstance(context)
        }
    }
}

