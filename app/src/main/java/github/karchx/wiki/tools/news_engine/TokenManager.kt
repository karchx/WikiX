package github.karchx.wiki.tools.news_engine

class TokenManager(private var tokenIndex: Int) {

    private val config = Config()

    fun getNewToken(): String {
        var token = ""

        try {
            token = config.getNewsToken(tokenIndex+1)
            tokenIndex += 1
        }

        // All indexes changed. Start with 0 index again
        catch (ex: IndexOutOfBoundsException) {
            tokenIndex = 0
            getNewToken()
        }

        return token
    }

    fun getToken(): String {
        return config.getNewsToken(tokenIndex)
    }
}
