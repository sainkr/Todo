package hong.checklist.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hong.checklist.R
import kotlinx.android.synthetic.main.item_day.view.*

class CalendarAdapter(val context: Context, val list : ArrayList<String>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val EMPTY_TYPE = 0
    val DAY_TYPE = 1


    // 정의 해주는
    inner class EmptyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

    }

    // 정의 해주는
    inner class DayViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val tv_day = itemView.tv_day
        // val tv_goalcheck = itemView.tv_goalcheck

    }

    override fun getItemViewType(position: Int): Int {
        val item = list.get(position)
        if(item.equals("0")){
            return EMPTY_TYPE
        }
        else{
            return DAY_TYPE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == EMPTY_TYPE){
            val itemView = LayoutInflater.from(context).inflate(R.layout.item_day_empty,parent,false)
            return EmptyViewHolder(itemView)
        }
        else {
            val itemView = LayoutInflater.from(context).inflate(R.layout.item_day,parent,false)
            return DayViewHolder(itemView)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position);

        when(viewType){
            EMPTY_TYPE -> {}
            DAY_TYPE -> {
                val day = list[position]
                (holder as DayViewHolder).tv_day.text = day
            }
        }
    }

}