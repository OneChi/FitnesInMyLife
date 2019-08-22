package ru.vanchikov.fitnesinmylife.data.ViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import ru.vanchikov.fitnesinmylife.data.Repository.DataRepository
import ru.vanchikov.fitnesinmylife.data.Database.UsersRoomDatabase
import ru.vanchikov.fitnesinmylife.data.model.LoggedInUser
import ru.vanchikov.fitnesinmylife.data.model.UserWays
import ru.vanchikov.fitnesinmylife.data.model.WayFix

class NavigationViewModel(application: Application) : AndroidViewModel(application) {

    private var dataRepository : DataRepository

    var currentWayOnMap : UserWays? = null



    var userAccount : LoggedInUser? = null



    init {
        val loginDao = UsersRoomDatabase.getDatabase(
            application,
            viewModelScope
        ).getUsersDao()
        dataRepository = DataRepository(loginDao)
    }


    fun getAllUsers() : LiveData<List<LoggedInUser>> {
        return dataRepository.getAllUsers()
    }

    fun deleteUserByUserId(userId : String) : Int
    {
        return dataRepository.deleteUserByUserId(userId)
    }
    suspend fun getUserTokenByLP(userlogin : String, userpassword : String) : LoggedInUser
    {
        return dataRepository.getUserTokenByLP(userlogin, userpassword)
    }
    suspend fun insertUser(user: LoggedInUser)
    {
        return dataRepository.insertUser(user)
    }
    suspend fun deleteAllUsers()
    {
        return dataRepository.deleteAllUsers()
    }
    fun allUserWaysByUserId(userId: String): LiveData<List<UserWays>>
    {
        return dataRepository.allUserWaysByUserId(userId)
    }
    fun allWayFixByWayId(wayId: Int) : LiveData<List<WayFix>>
    {
        return dataRepository.allWayFixByWayId(wayId)
    }
    suspend fun insertWayFix(fix : WayFix)
    {
        dataRepository.insertWayFix(fix)
    }
    suspend fun deleteFix(fix: WayFix) : Int
    {
        return dataRepository.deleteFix(fix)
    }
    fun deleteFixByFixId(fixId : Double) : Int
    {
        return dataRepository.deleteFixByFixId(fixId)
    }

    fun getAllFixes() : LiveData<List<WayFix>>
    {
        return dataRepository.getAllFixes()
    }
    suspend fun insertWay(way: UserWays)
    {
        return dataRepository.insertWay(way)
    }
    fun deleteWayById(wayId : Long) : Int
    {
        return dataRepository.deleteWayById(wayId)
    }
    fun getAllWays(): LiveData<List<UserWays>>
    {
        return dataRepository.getAllWays()
    }

    fun getWayById(wayId: Long) : UserWays
    {
        return dataRepository.getWayById(wayId)
    }


}
