package ru.vanchikov.fitnesinmylife.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import ru.vanchikov.fitnesinmylife.data.model.LoggedInUser
import java.io.IOException
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository(val dataSource: LoginDataSource) {


    val allUsers: LiveData<List<LoggedInUser>> = dataSource.getAlphabetizedUsers()

    // in-memory cache of the loggedInUser object
    var user: LoggedInUser? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore

        user = null
    }

    fun logout() {
        user = null
        //dataSource.logout()
    }

    suspend fun login(username: String, password: String): Result<LoggedInUser> {
        // handle login

        var result : Result<LoggedInUser>

        try {
            // TODO: handle loggedInUser authentication
            //val NewLoggedInUser = LoggedInUser("Alex","Onechi","qqqqqq", "a@b.c")

            val token =  dataSource.getUserToken(username, password)
            Log.w("LOG_REPOS","login = ${token.userId} and pass ${token.password}")
            if (token != null){

                result = Result.Success(token)
            }
            else
                throw Exception("bad pass or login")

        } catch (e: Throwable) {
            result =  Result.Error(IOException("Error logging in", e))
        }



        if (result is Result.Success) {
            setLoggedInUser(result.data)
        }

        return result
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }


    @Throws(InterruptedException::class)
    fun <T> LiveData<T>.waitForValue(): T {
        val data = arrayOfNulls<Any>(1)
        val latch = CountDownLatch(1)
        val observer = object : Observer<T> {
            override fun onChanged(o: T?) {
                data[0] = o
                latch.countDown()
                this@waitForValue.removeObserver(this)
            }
        }
        this.observeForever(observer)
        latch.await(2, TimeUnit.SECONDS)

        return data[0] as T
    }
}

/*
     fun login(username: String, password: String): Result<LoggedInUser> {
        val result :
        try {
            // TODO: handle loggedInUser authentication

           val NewLoggedInUser = LoggedInUser("Alex","Onechi","qqqqqq", "a@b.c")

            if (((username == NewLoggedInUser.email)or (username == NewLoggedInUser.userId)) and (password == NewLoggedInUser.password)){
            //val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), "Jane Doe")
                 result =  Result.Success(NewLoggedInUser)
            }
            else
                throw Exception("bad pass or login")

        } catch (e: Throwable) {
             result =  Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
        //null
    }
*
* */