package hong.checklist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hong.checklist.Listener.OnCheckListener
import kotlinx.android.synthetic.main.item_checklist.view.*

class TodoAdapter(val context : Context?, var list : ArrayList<String>, var onCheckListener: OnCheckListener) : RecyclerView.Adapter<TodoAdapter.MyViewHolder>(){

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
        val todo = list[position]
        var img = false

        holder.contents.text = todo

        holder.root.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                if(!img){
                    holder.img_ck.setImageResource(R.drawable.check);
                    count += 1
                    img = true;
                }
                else{
                    holder.img_ck.setImageResource(R.drawable.normal);
                    if(count == 1)
                        count = 0;
                    else
                        count -= 1
                    img = false;
                }

                onCheckListener.onCheckListener(count)

            }
        })
    }
}

