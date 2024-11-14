package br.com.ilstudio.dermatologyapp.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64

object Convert {
    fun base64ToBitmap(base64: String): Bitmap? {
        val decodedString = Base64.decode(base64, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    }
}
