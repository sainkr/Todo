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

    fun setTodo(todo : TodoEntity){
        val insertTask = object : AsyncTask<Unit, Unit, Unit>() {
            override fun doInBackground(vararg p0: Unit?) {
                Log.d("아놔",todo.date)
                db.todoDAO().insert(todo)
            }
        }

        insertTask.execute()
    }

    fun getAllTodos(todoEntity : TodoEntity){
        val getTask = object : AsyncTask<Unit, Unit, Unit>(){
            override fun doInBackground(vararg p0: Unit?) {
                todoentityList = db.todoDAO().getContent(todoEntity.date)
            }
            // insert 한 후에
            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)
                if(todoentityList.size > 0){
                    Log.d("진입1","1")
                    val list1 = todoentityList[0].contentList
                    var todoList = ArrayList<TodoContents>()
                    for(i in 0 until list1!!.size)
                        todoList.add(list1.get(i))
                    val list2 =todoEntity.contentList
                    for(i in 0 until list2!!.size)
                        todoList.add(list2.get(i))
                    val contentList : List<TodoContents> = todoList
                    setTodo(TodoEntity(todoEntity.date, contentList ,todoentityList[0].weather,0))
                }
                else{
                    Log.d("진입2","2")
                    setTodo(todoEntity)
                }
            }
        }

        getTask.execute()
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
                        setTodo(TodoEntity(date, todoentityList[0].contentList ,weather,0))
                    }
                }
                else{
                    var todoList = ArrayList<TodoContents>()
                    val contentList : List<TodoContents> = todoList
                    setTodo(TodoEntity(date, contentList, weather ,0))
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
                    var todoList = ArrayList<TodoContents>()

                    for (i in 1 until size) {

                        val jsonObject = jarray.getJSONObject(0)
                        val date = jsonObject.getString("date")
                        val content = jsonObject.getString("content")
                        val check = jsonObject.getInt("check")

                        if(date0.equals(date)){
                            todoList.add(TodoContents(content,check))
                        }else{
                            val contentList : List<TodoContents> = todoList
                            todoList.clear()
                            getAllTodos(TodoEntity(date0, contentList, -1, 0))
                            date0 = date
                        }

                        Log.d("내용",date+" "+content+" "+check)


                    }
                    getWeatherVolley( this, url_login, id)
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