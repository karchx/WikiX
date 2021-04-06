/*
 * Copyright 2021 Alex Syrnikov <pioneer19@post.cz>
 * SPDX-License-Identifier: Apache-2.0
 */

package github.karchx.wiki.model

import androidx.lifecycle.MutableLiveData
import github.karchx.wiki.model.db.AppDao
import io.github.rybalkinsd.kohttp.ext.asString
import javax.inject.Inject

class Repository @Inject constructor(
    private val appDao: AppDao
) {
//    private val _text = MutableLiveData<String>()
//    val text: LiveData<String> = _text

    suspend fun fetchPage( data: MutableLiveData<String>, pageUrl: String ) {
            val body = NetUtils.fetchAsync(pageUrl).asString()
            body ?: return

            data.value = body

            //                { response ->
//                    if (response.isSuccessful) {
//                        val repos = jsonAdapter.fromJson(response.body()?.string() ?: "")
//                        liveData.postValue(
//                            ViewResponse(
//                                status = "Response status ${response.message()}",
//                                repositories = repos ?: emptyList()
//                            )
//                        )
//                       _response.postValue(body!!)
//                    } else {
//                       _response.postValue(
//                          "Error"
//                            ViewResponse(
//                                status = "Response status ${response.message()}",
//                                repositories = emptyList()
//                            )
//                       )
//                    }
//                }
    }
//    fun getUser(userId: String): Flow<User> {
//        refreshUser(userId)
//        // Returns a Flow object directly from the database.
//        return userDao.load(userId)
//    }
//
//    private suspend fun refreshUser(userId: String) {
//        // Check if user data was fetched recently.
//        val userExists = userDao.hasUser(FRESH_TIMEOUT)
//        if (!userExists) {
//            // Refreshes the data.
//            val response = webservice.getUser(userId)
//
//            // Check for errors here.
//
//            // Updates the database. Since `userDao.load()` returns an object of
//            // `Flow<User>`, a new `User` object is emitted every time there's a
//            // change in the `User`  table.
//            userDao.save(response.body()!!)
//        }
//    }
}