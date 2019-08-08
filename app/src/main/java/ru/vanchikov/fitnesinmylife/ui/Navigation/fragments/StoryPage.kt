package ru.vanchikov.fitnesinmylife.ui.Navigation.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import ru.vanchikov.fitnesinmylife.R


class StoryPage : Fragment(), View.OnClickListener {

    private lateinit var  names : Array<String>
    private lateinit var position: Array<Int>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_story_page, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // TODO: DELETE AFTER TEST'S
        names = arrayOf("дом", "работа", "отдых", "магазин", "красота", "здоровье")
        position = arrayOf(223,535,656,433,974,232)

        val fab: FloatingActionButton = view.findViewById(R.id.addFbWay)
        fab.setOnClickListener (this)

        // находим список
        val lvMain = view.findViewById(R.id.lvWays) as ListView
        lvMain.setChoiceMode(ListView.FOCUSABLES_TOUCH_MODE)


        // создаем адаптер
        val adapter = ArrayAdapter<String>(view.context, android.R.layout.simple_list_item_1, names)

        // присваиваем адаптер списку
        lvMain.setAdapter(adapter)



    }

    override fun onClick(v: View?) {
        when (v?.id){
            R.id.addFbWay -> {
                Snackbar.make(v, "Floating button Add", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }
            else -> {

            }
        }
    }
}
