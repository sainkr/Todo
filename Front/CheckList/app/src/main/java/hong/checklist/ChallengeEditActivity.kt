package hong.checklist

import android.annotation.SuppressLint
import android.app.Activity
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
import hong.checklist.DB.*
import hong.checklist.Data.ChallengeFriendContents
import hong.checklist.Listener.OnCheckListener
import kotlinx.android.synthetic.main.activity_challengeedit.*

import org.json.JSONArray

@SuppressLint("StaticFieldLeak")
class ChallengeEditActivity : AppCompatActivity(), OnCheckListener {

    lateinit var db : CheckListDatabase

    var friendList = ArrayList<ChallengeFriendContents>()
    var memberList = ArrayList<String>()

    val url_friend = "http://192.168.35.76:8080/CheckList/Friend.jsp"
    val url_challenge = "http://192.168.35.76:8080/CheckList/ChallengeAdd.jsp"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_challengeedit)

        db = CheckListDatabase.getInstance(this)!!

        val manager = LinearLayoutManager(this)
        recyclerView_challengeplus.layoutManager = manager
        recyclerView_challengeplus.setHasFixedSize(true)

        var intent = intent
        var code = intent.extras?.getInt("code")!!
        var name = intent.extras?.getString("name")!!
        var id = intent.extras?.getString("id")!!
        memberList = intent.getStringArrayListExtra("list")!!

        for(i in 0 until memberList.size){
            if(memberList.get(i).equals(id)) {
                memberList.removeAt(i)
                break
            }
        }

        friendVolley(this, url_friend, id)

        et_challengename.setText(name)

        tv_back.setOnClickListener {
           finish()
        }

        tv_ok.setOnClickListener {
            var name = et_challengename.text.toString()
            if(name.equals("")){
                Toast.makeText(this, "모임명을 입력해주세요", Toast.LENGTH_LONG).show()
            }
            else{
                var add_member : String = ""
                var delete_member : String = ""

                var list = ArrayList<String>()

                var count = 0
                for( i in 0 until friendList.size){
                    if(friendList.get(i).check == 1){
                        list.add(friendList.get(i).id)
                        count++
                    }
                }

                // delete 멤버
                for(i in 0 until memberList.size){
                    var flag = false
                    for(j in 0 until list.size){
                        if(list.get(j).equals(memberList.get(i))){
                            list.removeAt(j)
                            flag = true
                            break
                        }
                    }
                    if(!flag){
                        delete_member+= "${memberList.get(i)} "
                    }
                }

                // add 멤버
                for(j in 0 until list.size){
                    add_member += "${list.get(j)} "
                }

                if(count == 0)
                    Toast.makeText(this, "모임원을 선택해주세요", Toast.LENGTH_LONG).show()
                else {
                    setVolley( this, url_challenge, name, id, code.toString(), add_member, delete_member)
                }
            }
        }

        tv_challengedelete.setOnClickListener {
            deleteVolley( this, url_challenge, id, code.toString())
        }

    }

    fun setRecyclerView(friendlist : List<ChallengeFriendContents>){
        recyclerView_challengeplus.adapter = ChallengeFriendAdapter(this, friendlist, this)
    }

    override fun onCheckListener(position: Int, check: Int) {
        friendList.set(position,
            ChallengeFriendContents(
                friendList.get(position).name,
                check,
                friendList.get(position).id
            )
        )
        setRecyclerView(friendList)
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
                        val id = jsonObject.getString("id")
                        val name = jsonObject.getString("name")

                        var flag = false
                        for(j in 0 until memberList.size) {
                            if (id.equals(memberList.get(j))) {
                                Log.d("멤버",memberList.get(j))
                                friendList.add(ChallengeFriendContents(name, 1, id))
                                flag = true
                                break
                            }
                        }
                        if(!flag)
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

    private fun setVolley(
        context: Context,
        url: String,
        name: String,
        host_id: String,
        code : String,
        add_member: String,
        delete_member : String
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
                    intent.putExtra("name",name)
                    intent.putExtra("type","set")
                    finish()
                }
            },
            Response.ErrorListener { error ->
                Log.d("통신 실패",error.toString())
            }
        ) {
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["type"] = "setChallenge"
                params["name"] = name
                params["host_id"] = host_id
                params["code"] = code
                params["add_member"] = add_member
                params["delete_member"] = delete_member

                return params
            }
        }
        // 3) 생성한 StringRequest를 RequestQueue에 추가
        requestQueue.add(request)
    }

    private fun deleteVolley(
        context: Context,
        url: String,
        host_id: String,
        code : String
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
                    intent.putExtra("type","drop")
                    finish()
                }
            },
            Response.ErrorListener { error ->
                Log.d("통신 실패",error.toString())
            }
        ) {
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["type"] = "deleteChallenge"
                params["name"] = ""
                params["host_id"] = host_id
                params["code"] = code
                params["add_member"] = ""
                params["delete_member"] = ""

                return params
            }
        }
        // 3) 생성한 StringRequest를 RequestQueue에 추가
        requestQueue.add(request)
    }

}
