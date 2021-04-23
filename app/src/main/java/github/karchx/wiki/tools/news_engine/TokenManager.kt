package github.karchx.wiki.tools.news_engine

class TokenManager {

    private val config = Config()

    fun getToken(_tokenIndex: Int): String {
        var tokenIndex = _tokenIndex
        var token = ""

        try {
            token = config.getNewsToken(tokenIndex)
        }

        // All indexes changed. Start with 0 index again
        catch (ex: IndexOutOfBoundsException) {
            tokenIndex = 0
            getToken(tokenIndex)
        }

        // Api error
        catch (ex: Exception) {
            tokenIndex += 1
            getToken(tokenIndex)
        }

        return token
    }
}