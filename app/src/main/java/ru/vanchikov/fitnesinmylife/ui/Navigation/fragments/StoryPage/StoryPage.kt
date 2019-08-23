package ru.vanchikov.fitnesinmylife.ui.Navigation.fragments.StoryPage


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import ru.vanchikov.fitnesinmylife.R
import ru.vanchikov.fitnesinmylife.data.ViewModels.NavigationViewModel
import ru.vanchikov.fitnesinmylife.util.makeToastLong

//import javax.swing.UIManager.put


class StoryPage : Fragment(), View.OnClickListener {

    // константы лучше хранить так. Может быть полезно если она не будут private и к ним можно будет образаться из других классов
    companion object {
        private val LOG_TAG = "STORY_FRAGMENT"
    }

    private lateinit var names: Array<String>
    private lateinit var position: Array<Int>

    // Думаю лучше делать свой ViewModel для каждого окна (в данном случае fragment)
    lateinit var storyPageViewModel: StoryPageViewModel

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
        val navigationViewModel =
            activity?.let { ViewModelProviders.of(it).get(NavigationViewModel::class.java) }

        storyPageViewModel = ViewModelProviders.of(this).get(StoryPageViewModel::class.java)

        val recyclerView: RecyclerView = view.findViewById(R.id.lvWays)
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.setHasFixedSize(true)

        // Когда создаём адаптер, передаём ему то, что будет раелизовывать наш интерфейс OnUserClickListener
        // тут можно сделать напрямую, как написано ниже, через object : UserWaysAdapter.OnUserClickListener {}
        // ИЛИ можно реализовать этот интерфейс во фрагменте или во ViewModel или в других места, где будет удобнее и лучше
        val waysAdapter = UserWaysAdapter(activity!!, object : UserWaysAdapter.OnUserClickListener {
            override fun onClickUserWay(id: String) {
                // здесь уже можно реализовать логику обрабокти нажатия на элемент списка
                onClickWay(id)
            }
        })
        recyclerView.adapter = waysAdapter

        //TODO: заменить на свой viewModel - storyPageViewModel !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! :)
        navigationViewModel?.allUserWaysByUserId(navigationViewModel?.userAccount!!.userId)
            ?.observe(this, Observer { waysAdapter.setNotes(it) })


    }

    override fun onClick(v: View?) {
        when (v?.id) {
            ru.vanchikov.fitnesinmylife.R.id.addFbWay -> {
                Snackbar.make(v, "Floating button Add", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }
            else -> {

            }
        }
    }


    private fun onClickWay(id: String){
        // в итоге у нас есть id элемента по которому был клик.
        // можно передавать не только id. тут всё зависит от потребностей.
        makeToastLong(id)

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


/*
        // названия телефонов (элементов)
        val phonesHTC = arrayOf("Sensation", "Desire", "Wildfire", "Hero")
        val phonesSams = arrayOf("Galaxy S II", "Galaxy Nexus", "Wave")
        val phonesLG = arrayOf("Optimus", "Optimus Link", "Optimus Black", "Optimus One")

        // коллекция для групп
        val groupData: ArrayList<Map<String, String>>

        // коллекция для элементов одной группы
        var childDataItem: ArrayList<Map<String, String>>

        // общая коллекция для коллекций элементов
        val childData:   ArrayList<ArrayList<Map<String, String>>>
        // в итоге получится childData = ArrayList<childDataItem>

        // список атрибутов группы или элемента
        var m: Map<String, String>


        val fab: FloatingActionButton = view.findViewById(ru.vanchikov.fitnesinmylife.R.id.addFbWay)
        fab.setOnClickListener (this)

        // находим список
        val lvMain = view.findViewById(ru.vanchikov.fitnesinmylife.R.id.lvWays) as ExpandableListView
        //lvMain.setChoiceMode(ListView.FOCUSABLES_TOUCH_MODE)

        // заполняем коллекцию групп из массива с названиями групп
        groupData = ArrayList()
        for (group in groups) {
            // заполняем список атрибутов для каждой группы
            m = HashMap()
            m.put("groupName", group) // имя компании
            groupData.add(m)
        }

        //val userWaysArrayLD = loginViewModel?.allUserWaysByUserId(UserAccount.user.userId)
        //var userWaysArray : ArrayList<UserWays>
       // userWaysArrayLD?.observe(this, Observer { ways -> userWaysArray = ways })



        // список атрибутов групп для чтения
        val groupFrom = arrayOf("groupName")
        // список ID view-элементов, в которые будет помещены атрибуты групп
        val groupTo = intArrayOf(android.R.id.text1)



        // val allUserWays = loginViewModel?.loginRepository,
        // создаем коллекцию для коллекций элементов
        childData = ArrayList()

        // создаем коллекцию элементов для первой группы
        childDataItem = ArrayList()
        // заполняем список атрибутов для каждого элемента
        for (phone in phonesHTC) {
            m = HashMap()
            m.put("phoneName", phone) // название телефона
            childDataItem.add(m)
        }


        // добавляем в коллекцию коллекций
        childData.add(childDataItem)

        // создаем коллекцию элементов для второй группы
        childDataItem = ArrayList()
        for (phone in phonesSams) {
            m = HashMap()
            m.put("phoneName", phone)
            childDataItem.add(m)
        }
        childData.add(childDataItem)

        // создаем коллекцию элементов для третьей группы
        childDataItem = ArrayList()
        for (phone in phonesLG) {
            m = HashMap()
            m.put("phoneName", phone)
            childDataItem.add(m)
        }
        childData.add(childDataItem)


        //navigationViewModel.allUserWaysByUserId(navigationViewModel.userAccount.userId)


        // список атрибутов элементов для чтения
        val childFrom = arrayOf("phoneName")
        // список ID view-элементов, в которые будет помещены атрибуты элементов
        val childTo = intArrayOf(android.R.id.text1)

        val adapter = SimpleExpandableListAdapter(
            view.context,
            groupData,
            android.R.layout.simple_expandable_list_item_1,
            groupFrom,
            groupTo,
            childData,
            android.R.layout.simple_list_item_1,
            childFrom,
            childTo
        )


        lvMain.setAdapter(adapter)
*/