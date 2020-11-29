package hong.checklist.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hong.checklist.R
import kotlinx.android.synthetic.main.item_friend.view.*

class FriendAdapter(val context : Context?, var list : List<String>) : RecyclerView.Adapter<FriendAdapter.MyViewHolder>(){

    // 정의 해주는
    inner class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val name = itemView.tv_freindname

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_friend,parent,false)

        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val name = list[position]

        holder.name.text = name
    }
}