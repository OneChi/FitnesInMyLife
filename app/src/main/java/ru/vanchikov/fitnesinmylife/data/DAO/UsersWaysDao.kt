package ru.vanchikov.fitnesinmylife.data.DAO

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.vanchikov.fitnesinmylife.data.model.LoggedInUser
import ru.vanchikov.fitnesinmylife.data.model.UserWays
import ru.vanchikov.fitnesinmylife.data.model.WayFix

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
@Dao
interface UsersWaysDao {

    @Query("SELECT * from users_table ORDER BY email ASC")
    fun getAlphabetizedUsers(): LiveData<List<LoggedInUser>>

    @Query("DELETE from users_table WHERE userId = :userId ")
    fun deleteUser(userId: String) : Int

    @Query("SELECT * from users_table WHERE ((email = :userlogin ) OR (userId = :userlogin)) AND (password = :userpassword)")
    suspend fun getUserToken(userlogin : String, userpassword : String) : LoggedInUser

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: LoggedInUser)

    @Query("DELETE FROM users_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM users_ways_table where userId = :userId")
    fun allUserWaysByUserId(userId : String) : LiveData<List<UserWays>>

    @Query("SELECT * FROM wayfix_table where wayId = :wayId")
    fun allWayFixByWayId(wayId: Int) : LiveData<List<WayFix>>

    @Query("SELECT * FROM users_ways_table where wayId = :wayId")
    fun getWayById(wayId: Long) : UserWays

    @Insert
    suspend fun insertFix(fix : WayFix)

    @Delete
    suspend fun deleteFix(fix : WayFix) : Int

    @Query("DELETE FROM wayfix_table WHERE fixId = :fixId")
    fun deleteByFixId(fixId : Double) : Int

    @Query("SELECT * from wayfix_table ORDER BY fixId ASC")
    fun getAllFixes(): LiveData<List<WayFix>>

    @Insert
    suspend fun insertWay(way: UserWays)

    @Query("DELETE FROM users_ways_table WHERE wayId = :wayId")
    fun deleteByWayId(wayId: Long) : Int


    @Query("SELECT * from users_ways_table ORDER BY wayId ASC")
    fun getAllWays(): LiveData<List<UserWays>>

}

