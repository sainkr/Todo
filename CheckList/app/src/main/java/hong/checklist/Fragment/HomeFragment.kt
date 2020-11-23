package hong.checklist.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import hong.checklist.Listener.OnCheckListener
import kotlinx.android.synthetic.main.fragment_home.*
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.Log
import android.view.Window
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hong.checklist.*
import hong.checklist.Listener.MyButtonClickListener
import org.w3c.dom.Text

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class HomeFragment(context: Context) : Fragment(),
    OnCheckListener {

    lateinit var formatted : String
    var todoList = ArrayList<String>()
    var update_check = false
    var count = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val tv_date : TextView = view.findViewById(R.id.tv_date)
        val recyclerView_todo: RecyclerView = view.findViewById(R.id.recyclerView_todo)
        val et_today_todo : EditText = view.findViewById(R.id.et_today_todo)
        var tv_weather : TextView = view.findViewById(R.id.tv_weather)
        var tv_goal : TextView = view.findViewById(R.id.tv_goal)

        // Inflate the layout for this fragment
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val current =  LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")
            formatted = current.format(formatter)
            tv_date.setText(formatted)
        } else {
            tv_date.setText("2020년 7월 30일")
        }

        val manager = LinearLayoutManager(context)

        // recyclerview 역순으로 출력
        manager.reverseLayout = true
        manager.stackFromEnd = true
        recyclerView_todo.layoutManager = manager
        recyclerView_todo.setHasFixedSize(true)

        val swipe = object : MySwipeHelper(context, recyclerView_todo, 200){
            override fun instantiateMyButton(
                viewHolder: RecyclerView.ViewHolder,
                buffer: MutableList<MyButton>
            ) {
                buffer.add(
                    MyButton(context!!,
                        "삭제",
                        50,
                        0,
                        Color.parseColor("#FFFFFF"),
                        object : MyButtonClickListener {
                            override fun onClick(pos: Int) {
                                Log.d("클릭", "삭제")
                                todoList.removeAt(pos)
                                setRecyclerView(todoList)
                                et_today_todo.setText("")
                                update_check = false
                            }
                        })
                )
                buffer.add(
                    MyButton(context!!,
                        "수정",
                        50,
                        0,
                        Color.parseColor("#000000"),
                        object : MyButtonClickListener {
                            override fun onClick(pos: Int) {
                                Log.d("클릭", "수정")
                                et_today_todo.setText(todoList[pos])

                                // 키보드 올리기
                                val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                                imm.toggleSoftInput(
                                    InputMethodManager.SHOW_FORCED,
                                    InputMethodManager.HIDE_IMPLICIT_ONLY
                                )
                                update_check = true
                                count = pos
                            }
                        })
                )
            }

        }

        // edittext 완료 시 list 추가
        et_today_todo.setOnEditorActionListener{ textView, action, event ->
            var handled = false

            if (action == EditorInfo.IME_ACTION_DONE) {
                if(et_today_todo.text.toString().equals("")){
                    Toast.makeText(requireContext(), "오늘 할 일을 입력해주세요.",Toast.LENGTH_SHORT).show()
                }
                else{
                    if(update_check){
                        todoList.set(count,et_today_todo.text.toString())
                        update_check = false
                    }
                    else{
                        todoList.add(et_today_todo.text.toString())
                    }

                    et_today_todo.setText("")
                    setRecyclerView(todoList)
                }

                // 키보드 내리기
                val inputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(et_today_todo.windowToken, 0)
                handled = true
            }

            handled
        }

        // 날씨 선택
        tv_weather.setOnClickListener{
            val dialog = Dialog(context!!)

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)   //타이틀바 제거
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 백그라운드 컬러 투명 ?
            dialog.setContentView(R.layout.aletr_weather)     //다이얼로그에 사용할 xml 파일을 불러옴
            dialog.setCancelable(false)    // 다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함

            var btn_weather1 : Button = dialog.findViewById(R.id.btn_weather1)
            var btn_weather2 : Button = dialog.findViewById(R.id.btn_weather2)
            var btn_weather3 : Button = dialog.findViewById(R.id.btn_weather3)
            var btn_weather4 : Button = dialog.findViewById(R.id.btn_weather4)

            btn_weather1.setOnClickListener{
                tv_weather.setText("날씨 : 맑음")
                dialog.dismiss()
            }
            btn_weather2.setOnClickListener{
                tv_weather.setText("날씨 : 흐림")
                dialog.dismiss()
            }
            btn_weather3.setOnClickListener{
                tv_weather.setText("날씨 : 비")
                dialog.dismiss()
            }
            btn_weather4.setOnClickListener{
                tv_weather.setText("날씨 : 눈")
                dialog.dismiss()
            }

            dialog.show()
        }

        return view
    }

    fun setRecyclerView(todoList : ArrayList<String>){

        recyclerView_todo.adapter =
            TodoAdapter(context, todoList, this)

    }

    override fun onCheckListener(count: Int) {
        val count_all = recyclerView_todo.adapter?.getItemCount() ?: 0
        if(count == 0){
            tv_goal.setText("빨리 시작해 .. ~")
        }
        else{
            // val goal = ( (count * 100 ) / count_all)
            val goal = count_all - count
            if(goal == 0)
                tv_goal.setText("오늘할거 다했다 !")
            else
                tv_goal.setText("아직 $goal 개 남았다 !")
        }
    }
}