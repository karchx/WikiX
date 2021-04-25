package github.karchx.wiki.tools.search_engine

import java.util.*

class MessageBuilder {
    companion object {
        fun getFoundResponseMessage(userLang: String, userRequest: String): String {
            var message: String = when (userLang) {
                "ru" -> {
                    "Найдено по запросу:\n"
                }
                else -> {
                    "Found on request:\n"
                }
            }
            message += userRequest.capitalize(Locale.ROOT)
            return message
        }
    }
}
