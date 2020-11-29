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
import hong.checklist.DB.CheckListDatabase
import hong.checklist.DB.ProfileEntity
import hong.checklist.DB.TodoContents
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_home.*

@SuppressLint("StaticFieldLeak")
class LoginActivity : AppCompatActivity() {

    val url_login = "http://192.168.35.135:8080/CheckList/Login.jsp"
    lateinit var db : CheckListDatabase

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
                    val intentR = intent
                    intentR.putExtra("name" , response);
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
}