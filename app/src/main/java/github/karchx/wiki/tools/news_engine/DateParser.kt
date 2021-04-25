package github.karchx.wiki.tools.news_engine

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.ZonedDateTime

class DateParser {
    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        fun parseDate(inputDate: String): String {
            return ZonedDateTime.parse(inputDate).toString()
                .replace("T", "  ")
                .replace("Z", "")
                .replace("-", "/")
        }
    }
}
