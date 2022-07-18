package com.example.unsplashapp.data

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import com.example.unsplashapp.utils.ErrorState
import com.example.unsplashapp.utils.SuccessState
import com.example.unsplashapp.utils.ViewState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import retrofit2.HttpException
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.net.URL

class MediaManager @AssistedInject constructor(
    @Assisted("context") private val context: Context
) {
    @RequiresApi(Build.VERSION_CODES.Q)
    fun downloadPhoto(flagSdk: Boolean, url: String): ViewState {
        return if (flagSdk) {
            downloadPhotoUpperOrEqualsQ(url)
        } else {
            downloadPhotoLowerQ(url)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun downloadPhotoUpperOrEqualsQ(url: String): ViewState {
        val resolver = context.contentResolver
        val uri = prepareImageUri(resolver)
        return try {
            URL(url).openStream().use { input ->
                if (uri != null) {
                    resolver.openOutputStream(uri).use { output ->
                        if (output != null) {
                            input.copyTo(output)
                        }
                    }
                }
            }
            SuccessState
        } catch (e: FileNotFoundException) {
            ErrorState
        } catch (e: HttpException) {
            ErrorState
        } catch (e: Exception) {
            ErrorState
        }
    }

    private fun downloadPhotoLowerQ(url: String): ViewState {
        return try {
            val file = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .toString() + "/"
                        + System.currentTimeMillis().toString()
                        + FILE_FORMAT
            )
            URL(url).openStream().use { input ->
                FileOutputStream(file).use { output ->
                    input.copyTo(output)
                }
            }
            SuccessState
        } catch (e: FileNotFoundException) {
            ErrorState
        } catch (e: HttpException) {
            ErrorState
        } catch (e: Exception) {
            ErrorState
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun prepareImageUri(resolver: ContentResolver): Uri? {
        val contentValues = ContentValues().apply {
            put(
                MediaStore.MediaColumns.DISPLAY_NAME,
                System.currentTimeMillis().toString() + FILE_FORMAT
            )
            put(MediaStore.MediaColumns.MIME_TYPE, MIME_TYPE)
            put(MediaStore.MediaColumns.RELATIVE_PATH, RELATIVE_PATH)
        }
        return resolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )
    }

    @AssistedFactory
    interface Factory {
        fun create(@Assisted("context") context: Context): MediaManager
    }

    companion object {
        const val RELATIVE_PATH = "DCIM/TestPhotos"
        const val MIME_TYPE = "image/jpeg"
        const val FILE_FORMAT = ".jpg"
    }
}