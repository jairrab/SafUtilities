package com.github.jairrab.safutilities.lib.utils.fileutils

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.github.jairrab.safutilities.lib.FileProviderUtil
import com.github.jairrab.safutilities.lib.utils.fileutils.helpers.CreateFile
import com.github.jairrab.safutilities.lib.utils.fileutils.helpers.OpenDirectory
import com.github.jairrab.safutilities.lib.utils.fileutils.helpers.PickFile
import com.github.jairrab.safutilities.lib.utils.uriutils.UriUtil
import com.github.jairrab.safutilities.model.MimeType
import java.io.File

internal class FileUtils(
    private val createFile: CreateFile,
    private val pickFile: PickFile,
    private val openDirectory: OpenDirectory
) {
    fun pickFile(
        fragment: Fragment,
        requestCode: Int,
        mimeType: MimeType,
        initialUri: Uri?
    ) {
        pickFile.execute(
            fragment = fragment,
            requestCode = requestCode,
            mimeType = mimeType,
            initialUri = initialUri
        )
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun createFile(
        fragment: Fragment,
        file: File,
        defaultFileName: String,
        requestCode: Int,
        authority: String,
        pickerInitialUri: Uri?,
    ){
        createFile.execute(
            fragment = fragment,
            file = file,
            defaultFileName = defaultFileName,
            requestCode = requestCode,
            authority = authority,
            pickerInitialUri = pickerInitialUri
        )
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun openDirectory(
        fragment: Fragment,
        initialUri: Uri? = null,
        requestCode: Int
    ) {
        openDirectory.execute(
            fragment = fragment,
            pickerInitialUri = initialUri,
            requestCode = requestCode
        )
    }

    companion object{
        fun getInstance(fileProviderUtil: FileProviderUtil):FileUtils{
            return FileUtils(
                createFile = CreateFile(fileProviderUtil),
                pickFile = PickFile(),
                openDirectory = OpenDirectory()
            )
        }
    }
}