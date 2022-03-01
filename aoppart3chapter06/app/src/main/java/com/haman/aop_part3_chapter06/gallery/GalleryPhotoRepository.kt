package com.haman.aop_part3_chapter06.gallery

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class GalleryPhotoRepository(
    val context: Context
) {

    suspend fun getAllPhoto(): MutableList<GalleryPhoto> = withContext(Dispatchers.IO) {
        val galleryPhotoList = mutableListOf<GalleryPhoto>()
        val uriExternal: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val query: Cursor?
        val projection = arrayOf(
            MediaStore.Images.ImageColumns.DISPLAY_NAME,
            MediaStore.Images.ImageColumns.SIZE,
            MediaStore.Images.ImageColumns.DATE_TAKEN,
            MediaStore.Images.ImageColumns.DATE_ADDED,
            MediaStore.Images.ImageColumns._ID
        )
        val resolver = context.contentResolver
        query = resolver.query(
            uriExternal,
            projection,
            null,
            null,
            "${MediaStore.Images.ImageColumns.DATE_ADDED} DESC"
        )
        query?.use { cursor ->
            val idColumn  = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns._ID)
            val nameColumn  = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DISPLAY_NAME)
            val sizeColumn  = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.SIZE)
            val dateColumn  = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATE_TAKEN)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val size = cursor.getString(sizeColumn)
                val date = cursor.getString(dateColumn)
                val contentUri = ContentUris.withAppendedId(uriExternal, id)

                galleryPhotoList.add(
                    GalleryPhoto(
                        id = id,
                        name = name,
                        size = size.toInt(),
                        date = date ?: "",
                        uri = contentUri
                    )
                )
            }
        }

        galleryPhotoList
    }
}