package com.github.jairrab.safutilities.lib.utils.fileproviderutils

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.github.jairrab.safutilities.lib.utils.fileproviderutils.helpers.CreateFile
import com.github.jairrab.safutilities.lib.utils.fileproviderutils.helpers.OpenDirectory
import com.github.jairrab.safutilities.lib.utils.fileproviderutils.helpers.PickFile
import com.github.jairrab.safutilities.lib.utils.uriutils.UriUtil
import com.github.jairrab.safutilities.model.MimeType
import java.io.File

internal class FileProviderUtils(
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
        fun getInstance(uriUtil: UriUtil):FileProviderUtils{
            return FileProviderUtils(
                createFile = CreateFile(uriUtil),
                pickFile = PickFile(),
                openDirectory = OpenDirectory()
            )
        }
    }
}