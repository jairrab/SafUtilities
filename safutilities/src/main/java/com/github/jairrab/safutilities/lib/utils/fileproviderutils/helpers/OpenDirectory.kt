package com.github.jairrab.safutilities.lib.utils.fileproviderutils.helpers

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment

class OpenDirectory {
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun execute(
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