package ru.vanchikov.fitnesinmylife.ui.Navigation.fragments.StoryPage


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import ru.vanchikov.fitnesinmylife.R
import ru.vanchikov.fitnesinmylife.data.ViewModels.MapPageViewModel
import ru.vanchikov.fitnesinmylife.data.ViewModels.NavigationViewModel
import ru.vanchikov.fitnesinmylife.data.model.LoggedInUser
import ru.vanchikov.fitnesinmylife.data.model.UserWays
import ru.vanchikov.fitnesinmylife.data.model.WayFix
import ru.vanchikov.fitnesinmylife.util.makeToastLong

//import javax.swing.UIManager.put


class StoryPage : Fragment(), View.OnClickListener {

    // константы лучше хранить так. Может быть полезно если она не будут private и к ним можно будет образаться из других классов
    companion object {
        private val LOG_TAG = "STORY_FRAGMENT"
    }
    private var navigationViewModel : NavigationViewModel? = null
    private lateinit var names: Array<String>
    private lateinit var position: Array<Int>

    // Думаю лучше делать свой ViewModel для каждого окна (в данном случае fragment)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_story_page, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val floatButton = view.findViewById<FloatingActionButton>(R.id.addFbWay)
        floatButton.setOnClickListener(this)

        navigationViewModel =
            activity?.let { ViewModelProviders.of(it).get(NavigationViewModel::class.java) }


        val recyclerView: RecyclerView = view.findViewById(R.id.lvWays)
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.setHasFixedSize(true)

 
        val waysAdapter = UserWaysAdapter(activity!!, object : UserWaysAdapter.OnUserClickListener {
            override fun onClickUserWay(way: UserWays) {

                              onClickWay(way)
            }
        })
        recyclerView.adapter = waysAdapter


        navigationViewModel?.allUserWaysByUserId(navigationViewModel?.userAccount!!.userId)
            ?.observe(this, Observer { waysAdapter.setNotes(it) })


    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.addFbWay -> {
                Snackbar.make(v, "Floating button Add", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()


                val NewLoggedInUser2 = LoggedInUser("Djosh","Onetwo","qqqqqq", "b@b.c")
                val NewLoggedInUser3 = LoggedInUser("Kerdan","Onethree","qqqqqq", "c@b.c")
                val NewLoggedInUser4 = LoggedInUser("Okes","Onefour","qqqqqq", "d@b.c")
                val way1 = UserWays(1,"Alex",242, "toHome",134134)
                val way2 = UserWays(2,"Alex",321, "toWork",21414)
                val way3 = UserWays(3,"Alex",542, "toSchool",43143)
                val way4 = UserWays(4,"Alex",341,"toShop",31513)
                val fix1 = WayFix(1,1.0,2.0,4.0,12312312,12f,"GPS",0)
                val fix2 = WayFix(1,4.0,3.0,6.0,22312312,142f,"NETWORK",0)

                navigationViewModel?.viewModelScope!!.launch {
                    Log.d("MYDATABASE","INITDBstart2")
                    navigationViewModel?.insertUser(NewLoggedInUser2)
                    navigationViewModel?.insertUser(NewLoggedInUser3)
                    navigationViewModel?.insertUser(NewLoggedInUser4)
                    navigationViewModel?.insertWay(way1)
                    navigationViewModel?.insertWay(way2)
                    navigationViewModel?.insertWay(way3)
                    navigationViewModel?.insertWay(way4)

                    navigationViewModel?.insertWayFix(fix1)
                    navigationViewModel?.insertWayFix(fix2)
                    Log.d("MYDATABASE","INITDBend2")
                }
            }
            else -> {

            }
        }
    }


    private fun onClickWay(way: UserWays){
        //makeToastLong("${way.wayId}")
        navigationViewModel?.currentWayOnMap= way
        navigationViewModel?.currentWayLoadState = true
        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(R.id.mapPage)
    }

}


/*
// создаем адаптер
val adapter = ArrayAdapter<String>(view.context, android.R.layout.simple_list_item_1, names)

// присваиваем адаптер списку
lvMain.setAdapter(adapter)

lvMain.onItemClickListener = OnItemClickListener { parent, view, position, id ->
    Log.d(LOG_TAG, "itemClick: position = $position, id = $id")
}

lvMain.onItemSelectedListener = object : OnItemSelectedListener {
    override fun onItemSelected(
        parent: AdapterView<*>, view: View,
        position: Int, id: Long
    ) {
        Log.d(LOG_TAG, "itemSelect: position = $position, id = $id")
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        Log.d(LOG_TAG, "itemSelect: nothing")
    }
}
*/
