package hong.checklist.Fragment

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
import androidx.recyclerview.widget.RecyclerView

class CalenderFragment(context: Context) : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calender, container, false)
        val recyclerView_calendar: RecyclerView = view.findViewById(R.id.recyclerView_calendar)
        val calendar = Calendar.getInstance() // 오늘 날짜
        var calendarList = ArrayList<String>();

        calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),1) // 1일로 날짜 설정

        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) -1  //해당 월에 시작하는 요일 -1 을 하면 빈칸을 구할 수 있겠죠 ?
        val max = calendar.getActualMaximum(Calendar.DAY_OF_MONTH) // 해당 월에 마지막 요일

        for (j in 1 until dayOfWeek) {
            calendarList.add("0");  //비어있는 일자 타입
        }
        for (j in 1..max) {
            calendarList.add(j.toString()); //일자 타입
        }

        recyclerView_calendar.adapter =
            CalendarAdapter(context!!, calendarList)
        recyclerView_calendar.layoutManager = StaggeredGridLayoutManager(7, StaggeredGridLayoutManager.VERTICAL)

        return view
    }
}

