package ru.vanchikov.fitnesinmylife.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import ru.vanchikov.fitnesinmylife.data.model.UserWays
import ru.vanchikov.fitnesinmylife.data.model.Ways


@Dao
interface WaysDao {

    @Insert
    fun insertFix(fix : Ways)

    @Delete
    fun deleteFix(fix : Ways)

    @Query("DELETE FROM ways_table WHERE fixId = :fixId")
    fun deleteByFixId(fixId : Double) : Int

    @Query("SELECT * from ways_table ORDER BY fixId ASC")
    fun getAllFixes(): LiveData<List<Ways>>

    @Insert
    fun insertWay(way: UserWays)

    @Query("DELETE FROM users_ways_table WHERE wayId = :wayId")
    fun deleteByWayId(wayId: Int) : Int


    @Query("SELECT * from users_ways_table ORDER BY wayId ASC")
    fun getAllWays(): LiveData<List<UserWays>>

}