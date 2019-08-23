package ru.vanchikov.fitnesinmylife.ui.Navigation.fragments.StoryPage.InsertWayPage

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import ru.vanchikov.fitnesinmylife.data.Database.UsersRoomDatabase
import ru.vanchikov.fitnesinmylife.data.Repository.DataRepository
import ru.vanchikov.fitnesinmylife.data.model.LoggedInUser
import ru.vanchikov.fitnesinmylife.data.model.UserWays

/**
 * Created on 22.08.2019
 */
class InsertPageViewModel(application: Application) : AndroidViewModel(application) {


    private var dataRepository : DataRepository

    var userAccount : LoggedInUser? = null

    init {
        val loginDao = UsersRoomDatabase.getDatabase(
            application,
            viewModelScope
        ).getUsersDao()
        dataRepository = DataRepository(loginDao)
    }

    fun allUserWaysByUserId(userId: String): LiveData<List<UserWays>>
    {
        return dataRepository.allUserWaysByUserId(userId)
    }
}