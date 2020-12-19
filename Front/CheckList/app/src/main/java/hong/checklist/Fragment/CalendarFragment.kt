package hong.checklist.Fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import hong.checklist.Adapter.CalendarAdapter
import hong.checklist.R
import java.util.*
import kotlin.collections.ArrayList
import android.content.Context
import android.content.Intent
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hong.checklist.DB.CheckListDatabase
import hong.checklist.DB.TodoEntity
import hong.checklist.DateActivity
import hong.checklist.Listener.OnDateClickListener
import kotlinx.android.synthetic.main.fragment_calendar.*

@SuppressLint("StaticFieldLeak")
class CalendarFragment() : Fragment(), OnDateClickListener {

    lateinit var db : CheckListDatabase
    var todoentityList = listOf<TodoEntity>()
    var calendarList = ArrayList<String>()
    val calendar = Calendar.getInstance() // 오늘 날짜

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)
        val recyclerView_calendar: RecyclerView = view.findViewById(R.id.recyclerView_calendar)
        var tv_calendar: TextView = view.findViewById(R.id.tv_calendar)
        val tv_pmonth: TextView = view.findViewById(R.id.tv_pmonth)
        val tv_nmonth: TextView = view.findViewById(R.id.tv_nmonth)

        db = CheckListDatabase.getInstance(requireContext())!!

        tv_calendar.setText(setCalendar(0, recyclerView_calendar))

        tv_pmonth.setOnClickListener {
            tv_calendar.setText(setCalendar(-1, recyclerView_calendar))
        }

        tv_nmonth.setOnClickListener {
            tv_calendar.setText(setCalendar(1, recyclerView_calendar))
        }

        return view
    }

    fun setCalendar(count : Int, recyclerView: RecyclerView): String{
        calendarList.clear()

        var year = calendar.get(Calendar.YEAR)
        var month = calendar.get(Calendar.MONTH)

        if(month + 1 + count > 12){
            year += 1
            month = 0
        }
        else if(month + 1 + count < 1){
            year -= 1
            month = 11
        }
        else{
            month += count
        }

        calendar.set(year,month,1) // 1일로 날짜 설정

        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) -1
        val max = calendar.getActualMaximum(Calendar.DAY_OF_MONTH) // 해당 월에 마지막 요일


        for (j in 1..dayOfWeek) {
            calendarList.add("0");  //비어있는 일자 타입
        }
        for (j in 1..max) {
            calendarList.add(j.toString()); //일자 타입
        }
        setRecyclerView(recyclerView)
        return " ${year}년 ${month + 1}월"
    }

    fun setRecyclerView(recyclerView: RecyclerView){
        recyclerView.adapter =
            CalendarAdapter(context!!, calendarList, this)
        recyclerView.layoutManager = StaggeredGridLayoutManager(7, StaggeredGridLayoutManager.VERTICAL)

    }

    override fun OnDateClickListener(day: String) {
        val calendar = Calendar.getInstance() // 오늘 날짜
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)+1

        val intent = Intent(requireContext(), DateActivity::class.java)
        val date = "${year}년 ${month}월 ${day}일"
        intent.putExtra("date",date)
        startActivity(intent)
    }

}

