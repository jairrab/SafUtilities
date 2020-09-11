package com.github.jairrab.safutilities.lib.utils

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.github.jairrab.safutilities.model.MimeType
import com.github.jairrab.safutilities.functions.mimeType
import java.io.File

class FileProviderUtils(
    private val uriUtil: UriUtil
) {
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun pickFile(
        fragment: Fragment,
        requestCode: Int,
        mimeType: MimeType,
        pickerInitialUri: Uri? = null
    ) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)

            type = mimeType.value

            if (mimeType == MimeType.ALL) {
                //see https://stackoverflow.com/a/55416383
                val mimeTypes = arrayOf(
                    "application/*",  //"audio/*",
                    "font/*",  //"image/*",
                    "message/*",
                    "model/*",
                    "multipart/*",
                    "text/*"
                )
                putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
                //putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            }

            // Optionally, specify a URI for the file that should appear in the
            // system file picker when it loads.
            if (pickerInitialUri != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri)
            }
        }

        fragment.startActivityForResult(intent, requestCode)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun createFile(
        fragment: Fragment,
        file: File,
        defaultFileName: String,
        requestCode: Int,
        authority: String,
        pickerInitialUri: Uri? = null
    ) {
        val intent = Intent().apply {
            val uri = uriUtil.getFileProviderUri(authority, file)
            addCategory(Intent.CATEGORY_OPENABLE)
            action = Intent.ACTION_CREATE_DOCUMENT
            type = file.extension.mimeType()
            putExtra(Intent.EXTRA_TITLE, defaultFileName)
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            // Optionally, specify a URI for the directory that should be opened in
            // the system file picker before your app creates the document.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri)
            }
        }
        fragment.startActivityForResult(intent, requestCode)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun openDirectory(
        fragment: Fragment,
        pickerInitialUri: Uri? = null,
        requestCode: Int
    ) {
        // Choose a directory using the system's file picker.
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply {
            // Provide read access to files and sub-directories in the user-selected
            // directory.
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION

            //addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            //addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)

            // Optionally, specify a URI for the directory that should be opened in
            // the system file picker when it loads.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri)
            }
        }

        fragment.startActivityForResult(intent, requestCode)
    }
}