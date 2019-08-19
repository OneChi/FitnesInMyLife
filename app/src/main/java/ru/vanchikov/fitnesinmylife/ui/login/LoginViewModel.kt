package ru.vanchikov.fitnesinmylife.ui.login

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.vanchikov.fitnesinmylife.R
import ru.vanchikov.fitnesinmylife.data.LoginRepository
import ru.vanchikov.fitnesinmylife.data.Result
import ru.vanchikov.fitnesinmylife.data.UsersRoomDatabase

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private var loginRepository : LoginRepository

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    init {
        val loginDao = UsersRoomDatabase.getDatabase(application, viewModelScope).getUsersDao()
        loginRepository = LoginRepository(loginDao)
    }


    fun getLogin(user: String, pass: String){
        viewModelScope.launch { login(user,pass)}
    }

    suspend fun login(username: String, password: String) {
        // can be launched in a separate asynchronous job

         val result = loginRepository.login(username, password)

            if (result is Result.Success) {
                _loginResult.postValue(LoginResult(success = LoggedInUserView(user = result.data)))
            } else {
                _loginResult.postValue(LoginResult(error = R.string.login_failed))
            }
        }


    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
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
}
