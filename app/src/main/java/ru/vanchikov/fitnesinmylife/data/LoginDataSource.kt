package ru.vanchikov.fitnesinmylife.data

import android.widget.Toast
import ru.vanchikov.fitnesinmylife.data.model.LoggedInUser
import java.io.IOException
import java.lang.Exception

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    fun login(username: String, password: String): Result<LoggedInUser> {
        try {
            // TODO: handle loggedInUser authentication

           val NewLoggedInUser = LoggedInUser("Alex","Onechi","qqqqqq", "a@b.c")

            if (((username == NewLoggedInUser.email)or (username == NewLoggedInUser.userId)) and (password == NewLoggedInUser.password)){
            //val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), "Jane Doe")
                UserAccount.user = NewLoggedInUser
                return Result.Success(NewLoggedInUser)
            }
            else
                throw Exception("bad pass or login")

        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
        //null
    }
}

