package ru.vanchikov.fitnesinmylife.data.Repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.Query
import ru.vanchikov.fitnesinmylife.data.model.Result
import ru.vanchikov.fitnesinmylife.data.DAO.UsersWaysDao
import ru.vanchikov.fitnesinmylife.data.model.LoggedInUser
import ru.vanchikov.fitnesinmylife.data.model.UserWays
import ru.vanchikov.fitnesinmylife.data.model.WayFix
import java.io.IOException

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class DataRepository(val dataSource: UsersWaysDao) {


    //TODO: добавить функции из дао

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

        var result: Result<LoggedInUser>

        try {
            // TODO: handle loggedInUser authentication
            //val NewLoggedInUser = LoggedInUser("Alex","Onechi","qqqqqq", "a@b.c")

            val token = dataSource.getUserToken(username, password)
            Log.w("LOG_REPOS", "login = ${token.userId} and pass ${token.password}")
            if (token != null) {

                result = Result.Success(token)
            } else
                throw Exception("bad pass or login")

        } catch (e: Throwable) {
            result = Result.Error(IOException("Error logging in", e))
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


    fun getAllUsers(): LiveData<List<LoggedInUser>> {
        return dataSource.getAlphabetizedUsers()
    }

    fun deleteUserByUserId(userId : String) : Int
    {
        return dataSource.deleteUser(userId)
    }
    suspend fun getUserTokenByLP(userlogin : String, userpassword : String) : LoggedInUser
    {
        return dataSource.getUserToken(userlogin, userpassword)
    }
    suspend fun insertUser(user: LoggedInUser)
    {
        dataSource.insert(user)
    }
    suspend fun deleteAllUsers()
    {
        return dataSource.deleteAll()
    }
    fun allUserWaysByUserId(userId: String): LiveData<List<UserWays>>
    {
        return dataSource.allUserWaysByUserId(userId)
    }
    fun allWayFixByWayId(wayId: Long) : LiveData<List<WayFix>>
    {
        return dataSource.allWayFixByWayId(wayId)
    }
    suspend fun insertWayFix(fix : WayFix)
    {
        dataSource.insertFix(fix)
    }
    suspend fun deleteFix(fix: WayFix) : Int
    {
        return dataSource.deleteFix(fix)
    }
    fun deleteFixByFixId(fixId : Double) : Int
    {
        return dataSource.deleteByFixId(fixId)
    }

    fun getAllFixes() : LiveData<List<WayFix>>
    {
        return dataSource.getAllFixes()
    }
    suspend fun insertWay(way: UserWays)
    {
        return dataSource.insertWay(way)
    }
    fun deleteWayById(wayId : Long) : Int
    {
        return dataSource.deleteByWayId(wayId)
    }
    fun getAllWays(): LiveData<List<UserWays>>
    {
        return dataSource.getAllWays()
    }


    fun getWayById(wayId: Long) : UserWays{
        return dataSource.getWayById(wayId)
    }


    /*
    suspend  insertWayFix(fix : WayFix)
    suspend  deleteFix(fix: WayFix) : Int
    suspend  insertWay(way: UserWays)
    suspend  getUserTokenByLP(userlogin : String, userpassword : String) : LoggedInUser
    suspend  insertUser(user: LoggedInUser)
    suspend  deleteAllUsers()

    getAllUsers(): LiveData<List<LoggedInUser>>

    deleteUserByUserId(userId : String) : Int

    allUserWaysByUserId(userId: String): LiveData<Array<UserWays>>

    allWayFixByWayId(wayId: Int) : LiveData<Array<WayFix>>

    deleteFixByFixId(fixId : Double) : Int

    getAllFixes() : LiveData<List<WayFix>>

    deleteWayById(wayId : Int) : Int

    getAllWays(): LiveData<List<UserWays>>

     */




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


/*
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
    }*/