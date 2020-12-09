package hong.checklist

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import hong.checklist.DB.CheckListDatabase
import hong.checklist.DB.ProfileEntity
import hong.checklist.DB.TodoContents
import hong.checklist.DB.TodoEntity
import kotlinx.android.synthetic.main.activity_option.*
import kotlinx.android.synthetic.main.fragment_home.*
import org.json.JSONException

@SuppressLint("StaticFieldLeak")
class OptionActivity : AppCompatActivity() {

    val url_logout = "http://192.168.35.76:8080/CheckList/Logout.jsp"

    var todoentityList = listOf<TodoEntity>()
    lateinit var db : CheckListDatabase
    var my_id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_option)

        db = CheckListDatabase.getInstance(this)!!
        val intent = intent
        my_id = intent.getStringExtra("my_id")

        tv_logout.setOnClickListener {
            if(my_id.equals("none")){
                Toast.makeText(this,"로그인 해주세요",Toast.LENGTH_LONG).show()
            }
            else{
                val dialog = Dialog(this)

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)   //타이틀바 제거
                dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 백그라운드 컬러 투명 ?
                dialog.setContentView(R.layout.aletr_logout)     //다이얼로그에 사용할 xml 파일을 불러옴
                dialog.setCancelable(false)    // 다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함

                var btn_back : Button = dialog.findViewById(R.id.btn_logoutback)
                var btn_ok : Button = dialog.findViewById(R.id.btn_logoutok)

                btn_back.setOnClickListener{

                    dialog.dismiss()
                }
                btn_ok.setOnClickListener{
                    deleteProfile() // 프로필 지우고
                    deleteVolley( url_logout, my_id) // 서버 db todo, weather 지움
                    dialog.dismiss()
                }

                dialog.show()
            }
        }
    }

    fun deleteProfile() {
        val insertTask = object : AsyncTask<Unit, Unit, Unit>() {
            override fun doInBackground(vararg p0: Unit?) {
                db.profileDAO().deleteProfile()
            }
        }

        insertTask.execute()
    }

    fun deleteTodo() {
        val insertTask = object : AsyncTask<Unit, Unit, Unit>() {
            override fun doInBackground(vararg p0: Unit?) {
                db.todoDAO().deleteTodo()
            }
        }

        insertTask.execute()
    }


    fun getAllTodos(){
        val getTask = object : AsyncTask<Unit, Unit, Unit>(){
            override fun doInBackground(vararg p0: Unit?) {
                todoentityList = db.todoDAO().getContent()
            }
            // insert 한 후에
            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)
                if(todoentityList.size > 0){
                    for(i in 0 until todoentityList.size){
                        val date = todoentityList[i].date
                        val list = todoentityList[i].contentList
                        val weather = todoentityList[i].weather
                        if(list!= null){
                            for(j in 0 until list.size ){
                                Log.d("저장",date+" "+list.get(j).content)
                                saveVolley( url_logout, "saveTodo", my_id, date, list.get(j).content, list.get(j).check.toString(),"")
                            }
                        }

                        saveVolley( url_logout,"saveWeather", my_id, date, "", "", weather.toString())
                    }

                    deleteTodo() // todo 지우고
                }
            }
        }

        getTask.execute()
    }

    private fun saveVolley(
        url: String,
        type : String,
        id: String,
        date: String,
        content: String,
        check: String,
        weather: String
        ) {
            // 1. RequestQueue 생성 및 초기화
            var requestQueue = Volley.newRequestQueue(this)

            // 2. Request Obejct인 StringRequest 생성
            val request: StringRequest = object : StringRequest(
                Method.POST, url,
                Response.Listener { response ->
            },
            Response.ErrorListener { error ->
                Log.d("통신 에러",error.toString())
            }
        ) {
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["type"] = type
                params["id"] = id
                params["date"] = date
                params["content"] = content
                params["check"] = check
                params["weather"] = weather

                return params
            }
        }
        // 3) 생성한 StringRequest를 RequestQueue에 추가
        requestQueue.add(request)
    }

    private fun deleteVolley(
        url: String,
        id: String
    ) {

        // 1. RequestQueue 생성 및 초기화
        var requestQueue = Volley.newRequestQueue(this)

        // 2. Request Obejct인 StringRequest 생성
        val request: StringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                getAllTodos() // 다 불러와서 저장
            },
            Response.ErrorListener { error ->
                Log.d("통신 에러",error.toString())
            }
        ) {
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["type"] = "delete"
                params["id"] = id
                params["date"] = ""
                params["content"] = ""
                params["check"] = ""
                params["weather"] = ""

                return params
            }
        }
        // 3) 생성한 StringRequest를 RequestQueue에 추가
        requestQueue.add(request)
    }
}