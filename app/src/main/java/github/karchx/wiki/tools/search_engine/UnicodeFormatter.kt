package github.karchx.wiki.tools.search_engine

class UnicodeFormatter {
    fun decode(unicodeStr: String): String {
        val byteArray = unicodeStr.toByteArray()
        return String(byteArray, Charsets.UTF_8)
    }
}