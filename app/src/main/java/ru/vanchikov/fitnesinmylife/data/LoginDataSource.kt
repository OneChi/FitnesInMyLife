package ru.vanchikov.fitnesinmylife.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.vanchikov.fitnesinmylife.data.model.LoggedInUser
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
@Dao
interface LoginDataSource {

    @Query("SELECT * from users_table ORDER BY email ASC")
    fun getAlphabetizedUsers(): LiveData<List<LoggedInUser>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: LoggedInUser)

    @Query("DELETE FROM users_table")
    suspend fun deleteAll()


}

