package github.karchx.wiki.tools.news_engine

class Config() {
    private val newsTokens = arrayListOf(
        "cdc76a766b28428bb5b2cadda6dcf481", "f862a7da7a22494095ee55c26bacb198",
        "0a861b624d9d422d81d35bca7810c064", "9e0a3dedfbe7415fbea5eb45966b6e4d",
        "37ffac5c18014cdfbab0980ee3561c95", "36bf3df59e6642e1962ad469c773fac4",
        "1125dd489be348b09bc31477ad3fe793", "0f94762a568b4a77a23c68a92c1fb9dc",
        "e0050f6bc0874b92930b9aa2ce401a64", "dc7cbd42f4064c91a839e6ec2b883aab",
        "cb9ce39ddd7e4f678c02b0e010bcd366", "cbaf348f386f43c085132c410015ac2c",
        "e8c6e3b087a74db6a6212dc9c321f59b"
    )

    fun getNewsToken(tokenIndex: Int): String {
        return newsTokens[tokenIndex]
    }
}