package hong.checklist.Fragment

import android.annotation.SuppressLint
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
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.AsyncTask
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
import hong.checklist.Adapter.TodoAdapter
import hong.checklist.DB.CheckListDatabase
import hong.checklist.DB.TodoContents
import hong.checklist.DB.TodoEntity
import hong.checklist.Listener.MyButtonClickListener

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

@SuppressLint("StaticFieldLeak")
class HomeFragment(context: Context) : Fragment(), OnCheckListener {

    lateinit var db : CheckListDatabase
    var todoentityList = listOf<TodoEntity>()

    lateinit var date : String
    var todoList = ArrayList<TodoContents>()

    var update_check = false
    var update_position = 0
    var weather = -1
    var check_count = 0

    var weatherarr = arrayOf("날씨 : 맑음","날씨 : 흐림","날씨 : 비","날씨 : 눈")

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

        db = CheckListDatabase.getInstance(requireContext())!!

        val calendar = Calendar.getInstance() // 오늘 날짜
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)+1
        val day = calendar.get(Calendar.DATE)

        date = "${year}년 ${month}월 ${day}일"
        tv_date.setText(date)

        // recyclerview 역순으로 출력
        val manager = LinearLayoutManager(context)
        manager.reverseLayout = true
        manager.stackFromEnd = true
        recyclerView_todo.layoutManager = manager
        recyclerView_todo.setHasFixedSize(true)

        // 맨 처음 불러오기
        getAllTodos(date)

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
                tv_weather.setText(weatherarr[0])
                weather = 0
                saveDB(0)
                dialog.dismiss()
            }
            btn_weather2.setOnClickListener{
                tv_weather.setText(weatherarr[1])
                weather = 1
                saveDB(0)
                dialog.dismiss()
            }
            btn_weather3.setOnClickListener{
                tv_weather.setText(weatherarr[2])
                weather = 2
                saveDB(0)
                dialog.dismiss()
            }
            btn_weather4.setOnClickListener{
                tv_weather.setText(weatherarr[3])
                weather = 3
                saveDB(0)
                dialog.dismiss()
            }

            dialog.show()
        }

        // 스와이프해서 수정, 삭제
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

                                todoList.removeAt(pos)
                                setRecyclerView(todoList)
                                et_today_todo.setText("")
                                update_check = false

                                //DB 저장
                                saveDB(0)
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

                                et_today_todo.setText(todoList[pos].content)

                                // 키보드 올리기
                                val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                                imm.toggleSoftInput(
                                    InputMethodManager.SHOW_FORCED,
                                    InputMethodManager.HIDE_IMPLICIT_ONLY
                                )
                                update_check = true
                                update_position = pos

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
                    if(update_check){ // 수정하고 확인 눌렀을 때
                        todoList.set(update_position,TodoContents(et_today_todo.text.toString(),todoList.get(update_position).check))
                        update_check = false
                    }
                    else{
                        todoList.add(TodoContents(et_today_todo.text.toString(),0))
                    }

                    et_today_todo.setText("")
                    setRecyclerView(todoList)

                    //DB 저장
                    saveDB(0)
                }

                // 키보드 내리기
                val inputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(et_today_todo.windowToken, 0)
                handled = true
            }

            handled
        }
        return view
    }

    fun setRecyclerView(todoList : List<TodoContents>){
        recyclerView_todo.adapter =
            TodoAdapter(context, todoList, this)
    }

    override fun onCheckListener(position: Int,check : Int) {
        todoList.set(position,TodoContents(todoList.get(position).content,check))
        var count = 0
        for(i in 0 until todoList.size){
            if(todoList.get(i).check == 1)
                count++;
            else
                break
        }

        if(todoList.size == count)
            saveDB(1)
        else
            saveDB(0)

    }

    fun setTodo(todo : TodoEntity){
        val insertTask = object : AsyncTask<Unit, Unit, Unit>() {
            override fun doInBackground(vararg p0: Unit?) {
                db.todoDAO().insert(todo)
            }
        }

        insertTask.execute()
    }

    fun getAllTodos(today: String){
        val getTask = object : AsyncTask<Unit, Unit, Unit>(){
            override fun doInBackground(vararg p0: Unit?) {
                todoentityList = db.todoDAO().getContent(today)
            }
            // insert 한 후에
            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)
                if(todoentityList.size > 0){
                    val list = todoentityList[0].contentList
                    weather = todoentityList[0].weather

                    // 날씨 설정
                    if(weather > -1)
                        tv_weather.setText(weatherarr[weather])

                    if(list!= null){
                        var count = list.size - 1
                        for(i in 0..count) {
                            todoList.add(TodoContents(list[i].content, list[i].check))
                            if(list[i].check == 1)
                                check_count++
                        }
                        setRecyclerView(todoList)
                    }
                }
            }
        }

        getTask.execute()
    }

    fun saveDB(goal : Int){
        // DB 저장
        val contentList : List<TodoContents> = todoList
        val todo = TodoEntity(date, contentList ,weather,goal)
        setTodo(todo)
    }

}