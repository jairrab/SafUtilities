package com.github.jairrab.safutilities.lib.utils.fileutils

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.github.jairrab.safutilities.lib.FileProviderUtil
import com.github.jairrab.safutilities.lib.utils.fileutils.helpers.OpenSavePicker
import com.github.jairrab.safutilities.lib.utils.fileutils.helpers.PickDirectory
import com.github.jairrab.safutilities.lib.utils.fileutils.helpers.PickFile
import com.github.jairrab.safutilities.model.MimeType
import java.io.File

internal class FileUtils(
    private val openSavePicker: OpenSavePicker,
    private val pickDirectory: PickDirectory,
    private val pickFile: PickFile,
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
    fun openSavePicker(
        fragment: Fragment,
        requestCode: Int,
        file: File,
        defaultFileName: String?,
        pickerInitialUri: Uri?,
    ) {
        openSavePicker.execute(
            fragment = fragment,
            file = file,
            defaultFileName = defaultFileName,
            requestCode = requestCode,
            pickerInitialUri = pickerInitialUri
        )
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun pickDirectory(
        fragment: Fragment,
        requestCode: Int,
        initialUri: Uri? = null
    ) {
        pickDirectory.execute(
            fragment = fragment,
            requestCode = requestCode,
            pickerInitialUri = initialUri
        )
    }

    companion object{
        fun getInstance(fileProviderUtil: FileProviderUtil):FileUtils{
            return FileUtils(
                openSavePicker = OpenSavePicker(),
                pickFile = PickFile(),
                pickDirectory = PickDirectory()
            )
        }
    }
}