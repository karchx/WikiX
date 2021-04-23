package github.karchx.wiki.tools

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.DisplayMetrics
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class DrawableManager {

    companion object {
        fun getDrawable(activity: Activity, url: String): Bitmap {
            val displayMetrics = DisplayMetrics()
            activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
            val height = displayMetrics.widthPixels / 3
            val width = displayMetrics.widthPixels / 2

            try {
                val connection: HttpURLConnection = URL(url).openConnection() as HttpURLConnection
                connection.connect()
                val input: InputStream = connection.inputStream
                return Bitmap.createScaledBitmap(
                    BitmapFactory.decodeStream(input),
                    width,
                    height,
                    true
                )
            } catch (ex: Exception) {
                val connection: HttpURLConnection =
                    URL("https://angliyskiyazik.ru/wp-content/uploads/2018/05/news.jpg").openConnection() as HttpURLConnection
                connection.connect()
                val input: InputStream = connection.inputStream
                return Bitmap.createScaledBitmap(
                    BitmapFactory.decodeStream(input),
                    width,
                    height,
                    true
                )
            }
        }
    }
}