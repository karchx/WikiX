package github.karchx.wiki.tools.search_engine

import org.jsoup.Jsoup

class HtmlFormatter {
    public fun html2str(html: String): String {
        val executedStr: String = Jsoup.parse(html).text()
        return executedStr
    }
}