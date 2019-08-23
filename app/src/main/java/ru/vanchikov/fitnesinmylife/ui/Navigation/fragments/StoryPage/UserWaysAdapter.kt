package ru.vanchikov.fitnesinmylife.ui.Navigation.fragments.StoryPage

import android.app.Activity
import android.app.Application
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import ru.vanchikov.fitnesinmylife.R
import ru.vanchikov.fitnesinmylife.data.ViewModels.NavigationViewModel
import ru.vanchikov.fitnesinmylife.data.model.LoggedInUser
import ru.vanchikov.fitnesinmylife.data.model.UserWays

class UserWaysAdapter(val activity: FragmentActivity, val listener: OnUserClickListener) : RecyclerView.Adapter<UserWaysAdapter.UserWayHolder>() {

    private var ways: List<UserWays> = ArrayList()
    val navigationViewModel : NavigationViewModel =  ViewModelProviders.of(activity).get(NavigationViewModel::class.java)

    override fun getItemCount(): Int = ways.size


    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): UserWayHolder {
        val itemView : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.way_list_item,parent,false)
        return UserWayHolder(itemView,activity)
    }

    override fun onBindViewHolder(@NonNull holder: UserWayHolder, position: Int) {
        val currWay : UserWays = ways.get(position)
        navigationViewModel.currentWayOnMap = currWay
        holder.textViewName.text = currWay.wayName
        holder.textViewInfo.text = currWay.wayTime.toString()

        // тут можно НАПРИМЕР положить какую-то инфу в tag вью и установить ClickListener
        // в данном случае мы кладём инфу в tag кнопки btnInspect. Можно было бы положить в само вью
        holder.btnInspect.tag = currWay
        holder.btnInspect.setOnClickListener {
            // достаём инфу из вью
            val item = it.tag as? UserWays ?: return@setOnClickListener
            // вызываем метод клика у интерфейса
            listener.onClickUserWay(item)
        }

    }

    fun setNotes(ways : List<UserWays>){
        this.ways = ways
        notifyDataSetChanged()

    }



    class UserWayHolder(itemView: View,activity: FragmentActivity) : RecyclerView.ViewHolder(itemView) {

         val textViewName: TextView
         val textViewInfo: TextView
         val btnInspect : Button

        init {
            textViewName = itemView.findViewById(R.id.textViewName)
            textViewInfo = itemView.findViewById(R.id.textViewInfo)
            btnInspect   = itemView.findViewById(R.id.inspect_btn)

            // Переделаем это в использование интерфейса
            /*val navigationViewModel : NavigationViewModel =  ViewModelProviders.of(activity).get(NavigationViewModel::class.java)

            btnInspect.setOnClickListener {


            Log.w("LOG_RECYCLE","${ navigationViewModel.currentWayOnMap?.wayName} was cklicked by ${navigationViewModel.userAccount?.userId}")
            Navigation.findNavController(activity,R.id.nav_host_fragment).navigate(R.id.mapPage)

            }*/
        }

    }

    //Можно создать интерфейс для обработки нажатий кнопок или других событий в адаптере
    interface OnUserClickListener{
        // например событие клика по итему вызовет этот метот и передаст в него id выбранного итема
        fun onClickUserWay(way: UserWays)
    }
}
