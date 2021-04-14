/*
 * Copyright 2021 Alex Syrnikov <pioneer19@post.cz>
 * SPDX-License-Identifier: Apache-2.0
 */

package github.karchx.wiki.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import github.karchx.wiki.model.db.AppDao
import io.github.rybalkinsd.kohttp.ext.asString
import org.json.JSONObject
import javax.inject.Inject

class Repository @Inject constructor(
    private val appDao: AppDao
) {
//    private val _text = MutableLiveData<String>()
//    val text: LiveData<String> = _text

    suspend fun fetchPage( data: MutableLiveData<String>, pageUrl: String ) {
        try {
            val body = NetUtils.fetchAsync(pageUrl).asString()
            body ?: return

            val json = JSONObject( body )
            data.value = json.getJSONObject( "parse" )
                    .getJSONObject("text")
                    .getString("*")
        } catch ( ex : Exception )
        {
            Log.i( "fetchPage", "error: ${ex.message}" )
        }
    }
}