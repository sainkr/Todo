package hong.checklist

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import hong.checklist.Adapter.ChallengeFriendAdapter
import hong.checklist.Data.ChallengeFriendContents
import hong.checklist.Data.FriendContents
import hong.checklist.Listener.OnCheckListener
import kotlinx.android.synthetic.main.activity_challengeplus.*
import org.json.JSONArray

@SuppressLint("StaticFieldLeak")
class ChallengeAddActivity : AppCompatActivity(), OnCheckListener {

    // 인원제한 100명까지
    // 로그인 안했으면 로그인 페이지로

    var friendList = ArrayList<ChallengeFriendContents>()
    var check_list = ArrayList<Int>()

    val url_challenge = "http://192.168.35.76:8080/CheckList/ChallengeAdd.jsp"
    val url_friend = "http://192.168.35.76:8080/CheckList/Friend.jsp"

    var host_id =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_challengeplus)

        var intent = intent
        host_id = intent.getStringExtra("host_id")

        val manager = LinearLayoutManager(this)
        recyclerView_challengeplus.layoutManager = manager
        recyclerView_challengeplus.setHasFixedSize(true)

        friendVolley(this, url_friend, host_id)

        tv_back.setOnClickListener {
           finish()
        }

        tv_ok.setOnClickListener {
            var name = et_challengename.text.toString()
            if(name.equals("")){
                Toast.makeText(this, "모임명을 입력해주세요", Toast.LENGTH_LONG).show()
            }
            else{
                var member : String = "$host_id"
                for(i in 0 until check_list.size){
                    member += " ${friendList.get(check_list.get(i)).id}"
                }

                if(check_list.size == 0)
                    Toast.makeText(this, "모임원을 선택해주세요", Toast.LENGTH_LONG).show()
                else {
                    addVolley(this, url_challenge, name, host_id, member)
                }
            }

        }
    }

    fun setRecyclerView(friendlist : List<ChallengeFriendContents>){
        recyclerView_challengeplus.adapter = ChallengeFriendAdapter(this, friendlist, this)
    }

    override fun onCheckListener(position: Int, check: Int) {
        if(check == 1){
            check_list.add(position)
        }
        else{
            for(i in 0 until check_list.size){
                if(check_list.get(i) == position){
                    check_list.remove(i)
                    break
                }
            }
        }

        friendList.set(position,
            ChallengeFriendContents(
                friendList.get(position).name,
                check,
                friendList.get(position).id
            )
        )
        setRecyclerView(friendList)
    }

    private fun addVolley(
        context: Context,
        url: String,
        name: String,
        host_id: String,
        member: String
    ) {

        // 1. RequestQueue 생성 및 초기화
        var requestQueue = Volley.newRequestQueue(this)

        // 2. Request Obejct인 StringRequest 생성
        val request: StringRequest = object : StringRequest(Method.POST, url,
            Response.Listener { response ->
                if(response.equals("error")){

                }
                else{
                    val intentR = intent
                    setResult(RESULT_OK,intentR); //결과를 저장
                    finish()
                }
            },
            Response.ErrorListener { error ->
                Log.d("통신 실패",error.toString())
            }
        ) {
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["type"] = "addChallenge"
                params["name"] = name
                params["host_id"] = host_id
                params["code"] = ""
                params["add_member"] = member
                params["delete_member"] = ""

                return params
            }
        }
        // 3) 생성한 StringRequest를 RequestQueue에 추가
        requestQueue.add(request)
    }

    private fun friendVolley(
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
                if(response.equals("requstNoting")){

                }
                else if(response.equals("error")){

                }
                else{
                    val jarray = JSONArray(response)
                    val size = jarray.length()

                    for (i in 0 until size) {
                        val jsonObject = jarray.getJSONObject(i)
                        val id = jsonObject.getString("friend_id")
                        val name = jsonObject.getString("name")

                        friendList.add(ChallengeFriendContents(name, 0, id))
                    }

                    setRecyclerView(friendList)
                }
            },
            Response.ErrorListener { error ->
                Log.d("통신 에러",error.toString())
            }
        ) {
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["type"] = "getFriend"
                params["my_id"] = id
                params["friend_id"] = ""

                return params
            }
        }
        // 3) 생성한 StringRequest를 RequestQueue에 추가
        requestQueue.add(request)
    }

}
