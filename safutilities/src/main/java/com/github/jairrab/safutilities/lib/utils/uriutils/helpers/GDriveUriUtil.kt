package com.github.jairrab.safutilities.lib.utils.uriutils.helpers

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import kotlin.math.min

internal class GDriveUriUtil(
    private val contentResolverUtil: ContentResolverUtil,
    private val context: Context
) {
    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun getPath(uri: Uri): String? {
        return withContext(Dispatchers.Default) {
            contentResolverUtil.getCursor(uri)?.use { cursor ->
                /*
                 * Get the column indexes of the data in the Cursor,
                 * move to the first row in the Cursor, get the data,
                 * and display it.
                * */
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)

                cursor.moveToFirst()

                val name = cursor.getString(nameIndex)
                val size = cursor.getLong(sizeIndex).toString()
                val file = File(context.cacheDir, name)

                try {
                    val inputStream = context.contentResolver.openInputStream(uri)
                    val outputStream = FileOutputStream(file)
                    val maxBufferSize = 1 * 1024 * 1024
                    val bytesAvailable = inputStream!!.available()
                    val bufferSize = min(bytesAvailable, maxBufferSize)
                    val buffers = ByteArray(bufferSize)

                    var read: Int
                    while (inputStream.read(buffers).also { read = it } != -1) {
                        outputStream.write(buffers, 0, read)
                    }

                    Log.e("File Size", "Size " + file.length())
                    inputStream.close()
                    outputStream.close()
                    Log.e("File Path", "Path " + file.path)
                    Log.e("File Size", "Size " + file.length())

                } catch (e: Exception) {
                    Log.e("Exception", e.message ?: "")
                } finally {
                    cursor.close()
                }

                file.path
            }
        }
    }
}
