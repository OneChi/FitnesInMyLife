package ru.vanchikov.fitnesinmylife.ui.login


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import ru.vanchikov.fitnesinmylife.R
import ru.vanchikov.fitnesinmylife.data.UserAccount
import ru.vanchikov.fitnesinmylife.data.ViewModels.LoginViewModel
import ru.vanchikov.fitnesinmylife.ui.Navigation.NavigationActivity

/**
 * A simple [Fragment] subclass.
 */
class login_page : Fragment(), View.OnClickListener {

    lateinit var loginViewModel : LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.login_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = view.findViewById<EditText>(R.id.username)
        val password = view.findViewById<EditText>(R.id.password)
        val login = view.findViewById<Button>(R.id.login)
        val loading = view.findViewById<ProgressBar>(R.id.loading)
        val registration  = view.findViewById<Button>(R.id.registration_btn)
        registration.setOnClickListener(this)
        // TODO: DELETE AFTER TESTING
        username.setText("a@b.c")
        password.setText("qqqqqq")


        loginViewModel = ViewModelProviders.of(activity!!)
            .get(LoginViewModel::class.java)

        loginViewModel.loginFormState.observe(this, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        loginViewModel.loginResult.observe(this, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.VISIBLE
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
                loading.visibility = View.INVISIBLE
            }
            if (loginResult.success != null) {
                updateUiWithUser(loginResult.success)
                //Complete and destroy login activity once successful
                val intent: Intent = Intent(this.context, NavigationActivity::class.java)
                UserAccount.user = loginResult.success.user
                startActivity(intent)
                activity!!.finish()
            }
            activity!!.setResult(Activity.RESULT_OK)


        })

        username.afterTextChanged {
            loginViewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }


        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.getLogin(
                            username.text.toString(),
                            password.text.toString()
                        )
                }
                false
            }

            login.setOnClickListener {
                loading.visibility = View.VISIBLE
                loginViewModel.getLogin(username.text.toString(), password.text.toString()) //login(username.text.toString(), password.text.toString())
            }
            //TODO : DELETE THIS
            //loginViewModel.getLogin(username.text.toString(), password.text.toString())
        }
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome)
        val displayName = model.user.displayName + "!"
        // TODO : initiate successful logged in experience
        Toast.makeText(
            activity!!.applicationContext,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(activity!!.applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }

    override fun onClick(v: View?) {
        when (v?.id){
            R.id.registration_btn ->{
                Navigation.findNavController(requireActivity(), R.id.nav_login_host_fragment).navigate(R.id.action_login_fragment_to_registration)
            }
        }

    }
}
