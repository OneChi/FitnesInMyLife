package ru.vanchikov.fitnesinmylife.ui.Navigation.fragments.StoryPage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import ru.vanchikov.fitnesinmylife.R
import ru.vanchikov.fitnesinmylife.data.model.UserWays

class UserWaysAdapter : RecyclerView.Adapter<UserWaysAdapter.UserWayHolder>() {

    private var ways: List<UserWays> = ArrayList()


    override fun getItemCount(): Int = ways.size


    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): UserWayHolder {
        val itemView : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.way_list_item,parent,false)
        return UserWayHolder(itemView)
    }

    override fun onBindViewHolder(@NonNull holder: UserWayHolder, position: Int) {
        val currWay : UserWays = ways.get(position)
        holder.textViewName.text = currWay.wayName
        holder.textViewInfo.text = currWay.wayTime.toString()

    }

    fun setNotes(ways : List<UserWays>){
        this.ways = ways
        notifyDataSetChanged()

    }



    class UserWayHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

         val textViewName: TextView
         val textViewInfo: TextView
         val btnInspect : Button

        init {
            textViewName = itemView.findViewById(R.id.textViewName)
            textViewInfo = itemView.findViewById(R.id.textViewInfo)
            btnInspect   = itemView.findViewById(R.id.inspect_btn)
        }

    }
}
