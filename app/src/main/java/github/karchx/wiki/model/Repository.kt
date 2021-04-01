/*
 * Copyright 2021 Alex Syrnikov <pioneer19@post.cz>
 * SPDX-License-Identifier: Apache-2.0
 */

package github.karchx.wiki.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import github.karchx.wiki.model.db.AppDao
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class Repository @Inject constructor(
//    private val webservice: Webservice,
//    // Simple in-memory cache. Details omitted for brevity.
//    private val executor: Executor,
    private val appDao: AppDao
) {
    private val _text = MutableLiveData<String>()
    val text: LiveData<String> = _text

    init {
        MainScope().launch {
//           _text.value = userRepository.getUser(userId)
            delay( 5000 )
            _text.value = "Got value"
        }
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