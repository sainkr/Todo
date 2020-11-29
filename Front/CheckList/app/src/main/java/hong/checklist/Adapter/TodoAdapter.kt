package hong.checklist.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hong.checklist.DB.TodoContents
import hong.checklist.Listener.OnCheckListener
import hong.checklist.R
import kotlinx.android.synthetic.main.item_checklist.view.*

class TodoAdapter(val context : Context?, var list : List<TodoContents>, var onCheckListener: OnCheckListener) : RecyclerView.Adapter<TodoAdapter.MyViewHolder>(){

    var count = 0;

    // 정의 해주는
    inner class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val contents = itemView.tv_contents
        val img_ck = itemView.iv_check
        val root = itemView.root_todo

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_checklist,parent,false)

        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val todo = list[position].content

        holder.contents.text = todo

        if(list[position].check == 1)
            holder.img_ck.setImageResource(R.drawable.check)

        holder.root.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                var check = 0
                when(list[position].check){
                    0 -> {
                           holder.img_ck.setImageResource(R.drawable.check)
                           check = 1
                    }
                    1 -> {holder.img_ck.setImageResource(R.drawable.normal) }
                }

                onCheckListener.onCheckListener(position,check)
            }
        })
    }
}