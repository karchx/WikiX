/*
 * Copyright 2021 Alex Syrnikov <pioneer19@post.cz>
 * SPDX-License-Identifier: Apache-2.0
 */

package github.karchx.wiki.model

import io.github.rybalkinsd.kohttp.dsl.async.httpGetAsync
import io.github.rybalkinsd.kohttp.ext.url
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Response

class NetUtils {
    companion object {
        suspend fun fetchAsync(urlString: String): Response {
            return withContext(Dispatchers.IO) {
                httpGetAsync() {
                    url(urlString)
//                                    param {
//                                        "q" to "iphone"
//                                        "safe" to "off"
//                                    }
//                    header {
//                    "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"
//                    "Accept-Encoding" to "gzip"
//                    "Accept-Language" to "en-US,en;q=0.5"
//                    "Connection" to "keep-alive"
//                    "Host" to URL(urlString).host
//                    "Upgrade-Insecure-Requests" to "1"
//                    "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; rv:68.0) Gecko/20100101 Firefox/68.0"
                    //                    "username" to "rybalkinsd"
                    //                    "security-policy" to json {
                    //                        "base-uri" to "none"
                    //                        "expect-ct" to json {
                    //                            "max-age" to 2592000
                    //                            "report-uri" to "foo.com/bar"
                    //                        }
                    //                        "script-src" to listOf("github.com", "github.io")
                    //                    }
                    //
                    //                    cookie {
                    //                        "user_session" to "toFycNV"
                    //                        "expires" to "Fri, 21 Dec 2018 09:29:55 -0000"
                    //                    }
//                    }
                }.await()
            }
        }
    }
}
