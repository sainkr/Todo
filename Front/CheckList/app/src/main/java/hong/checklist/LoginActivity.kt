package hong.checklist

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import hong.checklist.DB.*
import hong.checklist.Data.FriendContents
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_home.*
import org.json.JSONArray
import org.json.JSONException

@SuppressLint("StaticFieldLeak")
class LoginActivity : AppCompatActivity() {

    val url_login = "http://192.168.35.76:8080/CheckList/Login.jsp"
    lateinit var db : CheckListDatabase
    var todoentityList = listOf<TodoEntity>()
    var todoList = ArrayList<TodoContents>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        db = CheckListDatabase.getInstance(this)!!

        tv_findpassword.setOnClickListener{

        }

        btn_login.setOnClickListener{
            var id = et_login_id.text.toString()
            var password = et_login_password.text.toString()

            if(id.equals(""))
                Toast.makeText(this,"아이디를 입력해주세요",Toast.LENGTH_LONG).show()
            else if(password.equals(""))
                Toast.makeText(this,"비밀번호를 입력해주세요",Toast.LENGTH_LONG).show()
            else{
                loginVolley(this,url_login,id,password)
            }

        }

        btn_join.setOnClickListener {
            val intent = Intent(this, JoinActivity::class.java)
            startActivity(intent)
        }
    }

    fun insertProfile(profile: ProfileEntity) {
        val insertTask = object : AsyncTask<Unit, Unit, Unit>() {
            override fun doInBackground(vararg p0: Unit?) {
                db.profileDAO().insert(profile)

            }
        }
        insertTask.execute()
    }

    fun setTodo(date : String , contentList : List<TodoContents>, weather:Int){
        val insertTodoTask = object : AsyncTask<Unit, Unit, Unit>() {
            override fun doInBackground(vararg p0: Unit?) {
                for(i in 0 until contentList.size){
                    Log.d("출력",contentList.get(i).content)
                }
                db.todoDAO().insert(TodoEntity(date,contentList,weather,0))
            }
        }

        insertTodoTask.execute()
    }

    fun getWeather(date: String, weather : Int){
        val getTask = object : AsyncTask<Unit, Unit, Unit>(){
            override fun doInBackground(vararg p0: Unit?) {
                todoentityList = db.todoDAO().getContent(date)
            }
            // insert 한 후에
            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)
                if(todoentityList.size > 0){
                    if(todoentityList[0].weather == -1){
                        todoentityList[0].contentList?.let { setTodo(date, it,weather) }
                    }
                }
                else{
                    var todoList = ArrayList<TodoContents>()
                    val contentList : List<TodoContents> = todoList
                    setTodo(date, contentList, weather)
                }
            }
        }

        getTask.execute()
    }

    private fun loginVolley(
        context: Context,
                url: String,
                id: String,
                password: String
                ) {

                // 1. RequestQueue 생성 및 초기화
                var requestQueue = Volley.newRequestQueue(this)

                // 2. Request Obejct인 StringRequest 생성
                val request: StringRequest = object : StringRequest(
                    Method.POST, url,
                    Response.Listener { response ->
                        if(response.equals("loginFail")){
                            Toast.makeText(context, "로그인 실패..", Toast.LENGTH_LONG).show()
                        }
                        else if(response.equals("error")){

                        }
                        else{
                            Toast.makeText(this,"로그인 성공",Toast.LENGTH_LONG).show()

                            insertProfile(ProfileEntity(id, password, response)) // 내부 db 저장
                            getTodoVolley(this, url_login, id)

                            val intentR = intent
                            setResult(RESULT_OK,intentR); //결과를 저장
                            finish()

                        }
            },
            Response.ErrorListener { error ->
                Log.d("통신 에러",error.toString())
            }
        ) {
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["type"] = "login"
                params["id"] = id
                params["password"] = password
                params["name"] = ""

                return params
            }
        }
        // 3) 생성한 StringRequest를 RequestQueue에 추가
        requestQueue.add(request)
    }

    private fun getTodoVolley(
        context: Context,
        url: String,
        id: String
    ) {

        // 1. RequestQueue 생성 및 초기화
        var requestQueue = Volley.newRequestQueue(context)

        // 2. Request Obejct인 StringRequest 생성
        val request: StringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                if(response.equals("error")){

                }
                else if(response.equals("todoNoting")){

                }
                else{
                    val jarray = JSONArray(response)
                    val size = jarray.length()

                    val jsonObject = jarray.getJSONObject(0)
                    var date0 = jsonObject.getString("date")
                    val content0 = jsonObject.getString("content")
                    val check0 = jsonObject.getInt("check")

                    todoList.add(TodoContents(content0,check0))

                    for (i in 1 until size) {

                        val jsonObject = jarray.getJSONObject(i)
                        val date = jsonObject.getString("date")
                        val content = jsonObject.getString("content")
                        val check = jsonObject.getInt("check")
                        val weather = jsonObject.getInt("weather")

                        Log.d("사이즈", todoList.size.toString())
                        if(date0.equals(date)){
                            Log.d("내용1",date+" "+content+" "+check)
                            todoList.add(TodoContents(content,check))
                            if(i == size - 1){
                                val contentList : List<TodoContents> = todoList

                                setTodo(date0, contentList , weather)
                                todoList.clear()
                            }
                        }else{
                            Log.d("내용2",date+" "+content+" "+check)
                            val contentList : List<TodoContents> = todoList
                            for(i in 0 until contentList.size){
                                Log.d("출력",contentList.get(i).content)
                            }
                            setTodo(date0, contentList , weather)
                            todoList.clear()
                            todoList.add(TodoContents(content,check))
                            date0 = date
                        }

                    }
                    // getWeatherVolley( this, url_login, id)
                }
            },
            Response.ErrorListener { error ->
                Log.d("통신 에러",error.toString())
            }
        ) {
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["type"] = "getTodo"
                params["id"] = id
                params["password"] = ""
                params["name"] = ""

                return params
            }
        }
        // 3) 생성한 StringRequest를 RequestQueue에 추가
        requestQueue.add(request)
    }

    private fun getWeatherVolley(
        context: Context,
        url: String,
        id: String
    ) {
        // 1. RequestQueue 생성 및 초기화
        var requestQueue = Volley.newRequestQueue(context)

        // 2. Request Obejct인 StringRequest 생성
        val request: StringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                if(response.equals("error")){

                }
                else if(response.equals("weatherNoting")){

                }
                else{
                    val jarray = JSONArray(response)
                    val size = jarray.length()

                    for (i in 0 until size) {
                        val jsonObject = jarray.getJSONObject(i)
                        val date = jsonObject.getString("date")
                        val weather = jsonObject.getInt("weather")
                        Log.d("내용",date+" "+weather)
                        getWeather(date, weather)
                    }
                }
            },
            Response.ErrorListener { error ->
                Log.d("통신 에러",error.toString())
            }
        ) {
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["type"] = "getWeather"
                params["id"] = id
                params["password"] = ""
                params["name"] = ""

                return params
            }
        }
        // 3) 생성한 StringRequest를 RequestQueue에 추가
        requestQueue.add(request)
    }

}