package ru.vanchikov.fitnesinmylife.ui.Navigation.fragments.UserPage


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.vanchikov.fitnesinmylife.R
import ru.vanchikov.fitnesinmylife.data.ViewModels.LoginViewModel
import ru.vanchikov.fitnesinmylife.data.ViewModels.NavigationViewModel
import ru.vanchikov.fitnesinmylife.data.model.UserWays


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class UserPage : Fragment() {

    var loginViewModel : LoginViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        //loginViewModel = ViewModelProviders.of(this.activity).get(LoginViewModel::class.java)
        return inflater.inflate(R.layout.fragment_user_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //val loginViewModel = activity?.let { ViewModelProviders.of(it).get(LoginViewModel::class.java)}



        val username = view.findViewById<TextView>(R.id.userpagename)
        val userlogin = view.findViewById<TextView>(R.id.userpagelogin)
        val navigationViewModel = activity?.let { ViewModelProviders.of(it).get(NavigationViewModel::class.java)}

        /*
        var way : UserWays? = null
        navigationViewModel?.viewModelScope?.launch {
            way = navigationViewModel?.getWayById(0)
        }*/


        username.text = navigationViewModel?.userAccount?.displayName
        userlogin.text = navigationViewModel?.userAccount?.email


       // Toast.makeText(this.context,loginViewModel?.user?.displayName ,Toast.LENGTH_LONG).show()
/*
        loginViewModel?.userProfile?.observe(this, Observer {
            username.text = it.displayName
            userlogin.text = it.email
        })
*/
    }
}
