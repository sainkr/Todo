package hong.checklist

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import hong.checklist.DB.CheckListDatabase
import hong.checklist.DB.ProfileEntity
import kotlinx.android.synthetic.main.activity_join.*
import org.json.JSONObject

@SuppressLint("StaticFieldLeak")
class JoinActivity : AppCompatActivity(){

    lateinit var db : CheckListDatabase
    lateinit var requestQueue : RequestQueue

    val url_login = "http://192.168.35.135:3306/CheckList/Login.jsp"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        db = CheckListDatabase.getInstance(this)!!

        // 1. RequestQueue 생성 및 초기화
        requestQueue = Volley.newRequestQueue(this)

        btn_join.setOnClickListener {
            val name = et_join_name.text.toString()
            val id = et_join_id.text.toString()
            val password = et_join_password.text.toString()

            insertProfile(ProfileEntity(id, password, name)) // 내부 db 저장
            joinVolley(this,url_login,id,password,name) // 서버 db 저장

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

    private fun joinVolley(
        context: Context,
        url: String,
        id: String,
        password: String,
        name: String
    ) {
        // https://developer.android.com/training/volley/simple GET 방법

        // 2. Request Obejct인 StringRequest 생성
        val request: StringRequest = object : StringRequest(Method.POST, url,
            Response.Listener { response ->
                when(response){
                    "joinFail" -> {Toast.makeText(context, "이미 존재하는 이메일입니다. 다시 입력해주세요.", Toast.LENGTH_LONG).show()}
                }
            },
            Response.ErrorListener { error ->
                Log.d("통신 실패",error.toString())
            }
        ) {
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["type"] = "join"
                params["id"] = id
                params["password"] = password
                params["name"] = name // 로그인하는 휴대폰번호 정보
                // params["uID"] = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
                return params
            }
        }
        // 3) 생성한 StringRequest를 RequestQueue에 추가
        requestQueue.add(request)
    }

}
