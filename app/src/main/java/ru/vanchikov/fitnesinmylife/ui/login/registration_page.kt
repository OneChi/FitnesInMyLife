package ru.vanchikov.fitnesinmylife.ui.login


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.registration_fragment.*
import kotlinx.coroutines.launch
import ru.vanchikov.fitnesinmylife.R
import ru.vanchikov.fitnesinmylife.data.ViewModels.LoginViewModel
import ru.vanchikov.fitnesinmylife.data.model.LoggedInUser

/**
 * A simple [Fragment] subclass.
 */
class registration_page : Fragment(), View.OnClickListener {
    private lateinit var loginViewModel : LoginViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.registration_fragment, container, false)
    }
    private lateinit var  username : EditText
    private lateinit var  userEmail : EditText
    private lateinit var  password : EditText

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         username = view.findViewById<EditText>(R.id.reg_username)
         userEmail = view.findViewById<EditText>(R.id.reg_useremail)
         password = view.findViewById<EditText>(R.id.reg_password)
        val registration_btn = view.findViewById<Button>(R.id.reg_newUser_register)
        val backtologin_btn = view.findViewById<Button>(R.id.reg_back_btn)
        registration_btn.setOnClickListener(this)
        backtologin_btn.setOnClickListener(this)
        loginViewModel = ViewModelProviders.of(activity!!)
            .get(LoginViewModel::class.java)

    }

    //todo : сделать так чтобы не возвращало обратно на регистрацию при уходе с нее и на логин при переходе на нее
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.reg_newUser_register ->{
                //todo : сделать проверку на то что пользователь уже добавлен и ожидание окончания добавления пользователя в бд
                val newUser = LoggedInUser(userId = 0,displayName =  "${username.text}", password = "${password.text}",email = "${userEmail.text}")
                loginViewModel.viewModelScope.launch { loginViewModel.dataRepository.insertUser(newUser) }
                Navigation.findNavController(requireActivity(), R.id.nav_login_host_fragment).navigate(R.id.action_registration_to_login_fragment)
            }
            R.id.reg_back_btn ->{
                Navigation.findNavController(requireActivity(), R.id.nav_login_host_fragment).navigate(R.id.login_fragment)
            }
        }

    }
}
