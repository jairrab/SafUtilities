package com.github.jairrab.safutilities.lib.utils.fileutils.helpers

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.github.jairrab.safutilities.SafUtilities

class PickDirectory {
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun execute(
        fragment: Fragment,
        requestCode: Int,
        pickerInitialUri: Uri? = null
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

            val s1 = "requestCode:$requestCode"
            val s2 = "pickerInitialUri:$pickerInitialUri"
            Log.v(SafUtilities.LOG_TAG, "Opening save picker- $s1 | $s2")
        }

        fragment.startActivityForResult(intent, requestCode)
    }
}