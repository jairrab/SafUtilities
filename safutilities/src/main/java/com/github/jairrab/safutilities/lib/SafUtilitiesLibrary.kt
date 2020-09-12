package com.github.jairrab.safutilities.lib

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.github.jairrab.safutilities.SafUtilities
import com.github.jairrab.safutilities.lib.utils.appstorageutils.AppStorageUtils
import com.github.jairrab.safutilities.lib.utils.fileproviderutils.FileProviderUtils
import com.github.jairrab.safutilities.lib.utils.uriutils.UriUtil
import com.github.jairrab.safutilities.model.MimeType
import java.io.File
import java.io.FileInputStream

internal class SafUtilitiesLibrary(
    private val fileProviderUtils: FileProviderUtils,
    private val appStorageUtils: AppStorageUtils,
    private val uriUtil: UriUtil
) : SafUtilities {

    override fun getContentUri(file: File, authority: String): Uri {
        return uriUtil.getFileProviderUri(authority, file)
    }

    override fun pickFile(
        fragment: Fragment,
        requestCode: Int,
        mimeType: MimeType,
        initialUri: Uri?
    ) {
        fileProviderUtils.pickFile(fragment, requestCode, mimeType, initialUri)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun createFile(
        fragment: Fragment,
        file: File,
        defaultFileName: String,
        requestCode: Int,
        authority: String,
        pickerInitialUri: Uri?,
    ) {
        fileProviderUtils.createFile(
            fragment = fragment,
            file = file,
            defaultFileName = defaultFileName,
            requestCode = requestCode,
            authority = authority,
            pickerInitialUri = pickerInitialUri
        )
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun openDirectory(
        fragment: Fragment,
        pickerInitialUri: Uri?,
        requestCode: Int
    ) {
        fileProviderUtils.openDirectory(fragment, pickerInitialUri, requestCode)
    }

    override suspend fun copyUriToDirectory(uri: Uri?, destination: File, authority: String): Uri? {
        return appStorageUtils.copyUriToDirectory(uri, destination, authority)
    }

    override suspend fun copyUriToFile(uri: Uri?, destination: File, authority: String): Uri? {
        return appStorageUtils.copyUriToFile(uri, destination, authority)
    }

    override suspend fun copyToExternalStorage(fileToCopy: File, destinationUri: Uri) {
        appStorageUtils.copyToExternalStorage(fileToCopy, destinationUri)
    }

    override fun getFileNameFromContentUri(uri: Uri?): String {
        return uriUtil.getFileNameFromContentUri(uri)
    }

    override fun getFile(uri: Uri): File? {
        return uriUtil.getFile(uri)
    }

    override fun getFileInputStream(uri: Uri): FileInputStream? {
        return uriUtil.getFileInputStream(uri)
    }

    override fun deleteAllUserFiles() {
        appStorageUtils.deleteAllUserFiles()
    }

    companion object {
        fun getInstance(context: Context): SafUtilitiesLibrary {
            val uriUtil = UriUtil.getInstance(context)
            return SafUtilitiesLibrary(
                fileProviderUtils = FileProviderUtils.getInstance(uriUtil),
                appStorageUtils = AppStorageUtils(context, uriUtil),
                uriUtil = uriUtil
            )
        }
    }
}
