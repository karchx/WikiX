package github.karchx.wiki.tools

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL


class DrawableManager {

    companion object {
        fun getDrawable(url: String): Bitmap {
            try {
                val connection: HttpURLConnection = URL(url).openConnection() as HttpURLConnection
                connection.connect()
                val input: InputStream = connection.inputStream
                return BitmapFactory.decodeStream(input)
            }
            catch (ex: Exception) {
                val connection: HttpURLConnection = URL("https://angliyskiyazik.ru/wp-content/uploads/2018/05/news.jpg").openConnection() as HttpURLConnection
                connection.connect()
                val input: InputStream = connection.inputStream
                return BitmapFactory.decodeStream(input)
            }
        }
    }
}