package com.github.jairrab.safutilities.lib.utils.fileproviderutils.helpers

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.github.jairrab.safutilities.model.MimeType

class PickFile {
    fun execute(
        fragment: Fragment,
        requestCode: Int,
        mimeType: MimeType,
        initialUri: Uri?
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            pickFile(fragment, requestCode, mimeType, initialUri)
        } else {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = mimeType.value
            fragment.startActivityForResult(intent, requestCode)
        }
    }

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
}