package ru.vanchikov.fitnesinmylife.ui.Navigation.fragments


import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import ru.vanchikov.fitnesinmylife.R
import ru.vanchikov.fitnesinmylife.data.DataViewModel


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class UserPage : Fragment() {

    var dataViewModel : DataViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        //dataViewModel = ViewModelProviders.of(this.activity).get(DataViewModel::class.java)
        return inflater.inflate(R.layout.fragment_user_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //val dataViewModel = activity?.let { ViewModelProviders.of(it).get(DataViewModel::class.java)}



        val username = view.findViewById<TextView>(R.id.userpagename)
        val userlogin = view.findViewById<TextView>(R.id.userpagelogin)

       // Toast.makeText(this.context,dataViewModel?.user?.displayName ,Toast.LENGTH_LONG).show()

        dataViewModel?.userProfile?.observe(this, Observer {
            username.text = it.displayName
            userlogin.text = it.email
        })

    }
}
