package ru.vanchikov.fitnesinmylife.data

import androidx.lifecycle.LiveData
import ru.vanchikov.fitnesinmylife.data.model.LoggedInUser
import java.io.IOException

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

    fun login(username: String, password: String): Result<LoggedInUser> {
        // handle login

        var result : Result<LoggedInUser>

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