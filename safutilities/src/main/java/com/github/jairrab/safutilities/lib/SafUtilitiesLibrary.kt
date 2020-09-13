package com.github.jairrab.safutilities.lib

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.github.jairrab.safutilities.SafUtilities
import com.github.jairrab.safutilities.lib.utils.ContentResolverUtil
import com.github.jairrab.safutilities.lib.utils.appstorageutils.AppStorageUtils
import com.github.jairrab.safutilities.lib.utils.fileutils.FileUtils
import com.github.jairrab.safutilities.lib.utils.uriutils.UriUtil
import com.github.jairrab.safutilities.model.MimeType
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.io.OutputStream

internal class SafUtilitiesLibrary(
    private val appStorageUtils: AppStorageUtils,
    private val contentResolverUtil: ContentResolverUtil,
    private val fileProviderUtil: FileProviderUtil,
    private val fileUtils: FileUtils,
    private val uriUtil: UriUtil,
) : SafUtilities {

    override fun getContentUri(file: File, authority: String): Uri {
        return fileProviderUtil.getFileProviderUri(authority, file)
    }

    override fun pickFile(
        fragment: Fragment,
        requestCode: Int,
        mimeType: MimeType,
        initialUri: Uri?
    ) {
        fileUtils.pickFile(fragment, requestCode, mimeType, initialUri)
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
        fileUtils.createFile(
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
        fileUtils.openDirectory(fragment, pickerInitialUri, requestCode)
    }

    override suspend fun copyUriToDirectory(uri: Uri, destination: File, authority: String): Uri? {
        return appStorageUtils.copyUriToDirectory(uri, destination, authority)
    }

    override suspend fun copyUriToDirectory(uri: Uri, destination: File): File? {
        return appStorageUtils.copyUriToDirectory(uri, destination)
    }

    override suspend fun copyUriToFile(uri: Uri, destination: File, authority: String): Uri? {
        return appStorageUtils.copyUriToFile(uri, destination, authority)
    }

    override suspend fun copyUriToFile(uri: Uri, destination: File): File? {
        return appStorageUtils.copyUriToFile(uri, destination)
    }

    override suspend fun copyToExternalStorage(fileToCopy: File, destinationUri: Uri) {
        appStorageUtils.copyToExternalStorage(fileToCopy, destinationUri)
    }

    override fun getContentUriFileName(uri: Uri): String? {
        return contentResolverUtil.getContentUriFileName(uri)
    }

    override fun getContentUriData(uri: Uri, projection: String): String? {
        return contentResolverUtil.getContentUriData(uri, projection)
    }

    override fun getFile(uri: Uri): File? {
        return uriUtil.getPath(uri)?.let { File(it) }
    }

    override fun getFileInputStream(uri: Uri): FileInputStream? {
        return contentResolverUtil.getFileInputStream(uri)
    }

    override fun getInputStream(uri: Uri): InputStream? {
        return contentResolverUtil.getInputStream(uri)
    }

    override fun getOutputStream(uri: Uri): OutputStream? {
        return contentResolverUtil.getOutputStream(uri)
    }

    override fun getUriSize(uri: Uri): Long {
        return contentResolverUtil.getURISize(uri)
    }

    override fun deleteAllUserFiles() {
        appStorageUtils.deleteAllUserFiles()
    }

    companion object {
        fun getInstance(context: Context): SafUtilitiesLibrary {
            val contentResolverUtil = ContentResolverUtil(context)
            val fileProviderUtil = FileProviderUtil(context)
            return SafUtilitiesLibrary(
                appStorageUtils = AppStorageUtils.getInstance(context,contentResolverUtil, fileProviderUtil),
                contentResolverUtil = contentResolverUtil,
                fileProviderUtil = fileProviderUtil,
                fileUtils = FileUtils.getInstance(fileProviderUtil),
                uriUtil = UriUtil.getInstance(context, contentResolverUtil),
            )
        }
    }
}
