/*
 * Copyright 2021 Andrey Karchevsky <karch.andrus@gmail.com>
 * SPDX-License-Identifier: Apache-2.0
 */

package github.karchx.wiki.tools.search_engine

class UnicodeFormatter {
    fun decode(unicodeStr: String): String {
        val byteArray = unicodeStr.toByteArray()
        return String(byteArray, Charsets.UTF_8)
    }
}