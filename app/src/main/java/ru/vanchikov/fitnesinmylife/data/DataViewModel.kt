package ru.vanchikov.fitnesinmylife.data

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.vanchikov.fitnesinmylife.R
import ru.vanchikov.fitnesinmylife.data.model.LoggedInUser
import ru.vanchikov.fitnesinmylife.data.model.UserWays
import ru.vanchikov.fitnesinmylife.data.model.Ways
import ru.vanchikov.fitnesinmylife.ui.login.LoggedInUserView
import ru.vanchikov.fitnesinmylife.ui.login.LoginFormState
import ru.vanchikov.fitnesinmylife.ui.login.LoginResult

class DataViewModel(application: Application) : AndroidViewModel(application) {

    private var dataRepository : DataRepository

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    private val _userProfile = MutableLiveData<LoggedInUser>()
    val userProfile: LiveData<LoggedInUser> = _userProfile



    init {
        val loginDao = UsersRoomDatabase.getDatabase(application, viewModelScope).getUsersDao()
        dataRepository = DataRepository(loginDao)
    }



    fun getLogin(user: String, pass: String){
        viewModelScope.launch { login(user,pass)}
    }

    suspend fun login(username: String, password: String) {
        // can be launched in a separate asynchronous job

         val result = dataRepository.login(username, password)

            if (result is Result.Success) {
                _loginResult.postValue(
                    LoginResult(
                        success = LoggedInUserView(
                            user = result.data
                        )
                    )
                )

                // сохранение в юзер профиль переменную
                _userProfile.postValue(result.data)

            } else {
                _loginResult.postValue(LoginResult(error = R.string.login_failed))
            }
        }


    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value =
                LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value =
                LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
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
    fun allWayFixByWayId(wayId: Int) : LiveData<List<Ways>>
    {
        return dataRepository.allWayFixByWayId(wayId)
    }
    suspend fun insertWayFix(fix : Ways)
    {
        dataRepository.insertWayFix(fix)
    }
    suspend fun deleteFix(fix: Ways) : Int
    {
        return dataRepository.deleteFix(fix)
    }
    fun deleteFixByFixId(fixId : Double) : Int
    {
        return dataRepository.deleteFixByFixId(fixId)
    }

    fun getAllFixes() : LiveData<List<Ways>>
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



}
