package hong.checklist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_addfrined.*
import kotlinx.android.synthetic.main.activity_login.*

class AddFriendActivity : AppCompatActivity() {

    val url_request = "http://192.168.35.135:8080/CheckList/Request.jsp"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addfrined)

        val intent = intent
        val my_id = intent.getStringExtra("my_id")

        btn_friendadd.setOnClickListener {
            if(et_frined_id.text.toString().equals(""))
                Toast.makeText(this, "다시 입력해주세요", Toast.LENGTH_LONG).show()
            else{
                Log.d("진입","!")
                requestfrinedVolley(this,url_request,my_id,et_frined_id.text.toString())
            }
        }
    }

    private fun requestfrinedVolley(
        context: Context,
        url: String,
        my_id: String,
        friend_id: String
    ) {

        // 1. RequestQueue 생성 및 초기화
        var requestQueue = Volley.newRequestQueue(this)

        // 2. Request Obejct인 StringRequest 생성
        val request: StringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                Log.d("진입","3")
                when(response){
                    "requestFail" -> {Toast.makeText(context, "존재하지 않는 ID 입니다.", Toast.LENGTH_LONG).show()}
                    "requestSuccess" -> { Toast.makeText(this,"친구신청 성공",Toast.LENGTH_LONG).show()
                        finish()}
                }
            },
            Response.ErrorListener { error ->
                Log.d("통신 에러",error.toString())
            }
        ) {
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                Log.d("진입","2")
                params["my_id"] = my_id
                params["frined_id"] = friend_id

                return params
            }
        }
        // 3) 생성한 StringRequest를 RequestQueue에 추가
        requestQueue.add(request)
    }
}