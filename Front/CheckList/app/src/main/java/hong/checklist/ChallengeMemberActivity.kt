package hong.checklist

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import hong.checklist.Adapter.ChallengeTodoAdapter
import hong.checklist.Adapter.FriendAdapter
import hong.checklist.Adapter.MemberAdapter
import hong.checklist.DB.*
import hong.checklist.Listener.OnCheckListener
import kotlinx.android.synthetic.main.activity_challengemember.*
import org.json.JSONArray
import org.json.JSONException

@SuppressLint("StaticFieldLeak")
class ChallengeMemberActivity: AppCompatActivity(), OnCheckListener {

    lateinit var db : CheckListDatabase
    var challengeList = ArrayList<ChallengeContents>()
    var successList = ArrayList<String>()
    var failList = ArrayList<String>()

    var code: Int =0
    var name: String =""
    var my_id: String =""

    val url_challengetodo = "http://192.168.35.76:8080/CheckList/ChallengeTodo.jsp"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_challengemember)

        db = CheckListDatabase.getInstance(this)!!

        var intent = intent
        code = intent.extras?.getInt("code")!!
        name = intent.extras?.getString("name")!!
        my_id = intent.extras?.getString("my_id")!!

        val manager = LinearLayoutManager(this)
        manager.reverseLayout = true
        manager.stackFromEnd = true
        recyclerView_challengetodo.layoutManager = manager
        recyclerView_challengetodo.setHasFixedSize(true)

        recyclerView_successmember.layoutManager = LinearLayoutManager(this)
        recyclerView_successmember.setHasFixedSize(true)

        recyclerView_failmember.layoutManager = LinearLayoutManager(this)
        recyclerView_failmember.setHasFixedSize(true)

        getChallengeTodoVolley(this, url_challengetodo, my_id, code.toString())
        getChallengeMember(this, url_challengetodo, my_id, code.toString())

        tv_challengename.setText(name)


    }

    fun setRecyclerView(list : List<ChallengeContents>){
        recyclerView_challengetodo.adapter = ChallengeTodoAdapter(this, list,this)
    }

    fun setRecyclerView(recylclerview : RecyclerView, list : List<String>){
        recylclerview.adapter = MemberAdapter(this, list)
    }

    override fun onCheckListener(position: Int, check: Int) {
        challengeList.set(position,ChallengeContents(challengeList.get(position).num,challengeList.get(position).content,check))
        setCheckChallengeTodoVolley(this, url_challengetodo, my_id, code.toString(), challengeList.get(position).num.toString());
    }


    private fun getChallengeTodoVolley(
        context: Context,
        url: String,
        my_id : String,
        code : String
    ) {

        // 1. RequestQueue 생성 및 초기화
        var requestQueue = Volley.newRequestQueue(this)

        // 2. Request Obejct인 StringRequest 생성
        val request: StringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                if(response.equals("error")){

                }
                else if(response.equals("challengeNoting")){

                }
                else{
                    try {
                        val jarray = JSONArray(response)
                        val size = jarray.length()
                        for (i in 0 until size) {
                            val jsonObject = jarray.getJSONObject(i)
                            val num = jsonObject.getString("num");
                            val content = jsonObject.getString("content");
                            val check = jsonObject.getString("check");
                            challengeList.add(ChallengeContents(num.toInt(),content, check.toInt()))
                        }
                    } catch (e : JSONException) {
                        e.printStackTrace()
                    }

                    setRecyclerView(challengeList)
                }
            },
            Response.ErrorListener { error ->
                Log.d("통신 실패",error.toString())
            }
        ) {
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["type"] = "getChallengetodo"
                params["my_id"] = my_id
                params["code"] = code
                params["num"] = ""
                return params
            }
        }
        // 3) 생성한 StringRequest를 RequestQueue에 추가
        requestQueue.add(request)
    }

    private fun getChallengeMember(
        context: Context,
        url: String,
        my_id : String,
        code : String
    ) {

        // 1. RequestQueue 생성 및 초기화
        var requestQueue = Volley.newRequestQueue(this)

        // 2. Request Obejct인 StringRequest 생성
        val request: StringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                if(response.equals("error")){

                }
                else if(response.equals("challengeNoting")){

                }
                else{
                    try {
                        val jarray = JSONArray(response)
                        val size = jarray.length()
                        for (i in 0 until size) {
                            val jsonObject = jarray.getJSONObject(i)
                            val member_id = jsonObject.getString("member_id");
                            val name = jsonObject.getString("name");
                            val success = jsonObject.getInt("success");



                            if(success == 1){
                                successList.add(name)
                            }
                            else{
                                Log.d("이름",name)
                                failList.add(name)
                            }

                        }
                    } catch (e : JSONException) {
                        e.printStackTrace()
                    }

                    setRecyclerView(recyclerView_successmember ,successList)
                    setRecyclerView(recyclerView_failmember ,failList)
                }
            },
            Response.ErrorListener { error ->
                Log.d("통신 실패",error.toString())
            }
        ) {
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["type"] = "getMember"
                params["my_id"] = ""
                params["code"] = code
                params["num"] = ""
                return params
            }
        }
        // 3) 생성한 StringRequest를 RequestQueue에 추가
        requestQueue.add(request)
    }

    private fun setCheckChallengeTodoVolley(
        context: Context,
        url: String,
        my_id : String,
        code : String,
        num: String
    ) {

        // 1. RequestQueue 생성 및 초기화
        var requestQueue = Volley.newRequestQueue(this)

        // 2. Request Obejct인 StringRequest 생성
        val request: StringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                if(response.equals("error")){

                }
                else if(response.equals("setCheckSuccess")){
                    successList.clear()
                    failList.clear()
                    getChallengeMember(this, url_challengetodo, my_id, code.toString())
                }
            },
            Response.ErrorListener { error ->
                Log.d("통신 실패",error.toString())
            }
        ) {
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["type"] = "setChallengetodo"
                params["my_id"] = my_id
                params["code"] = code
                params["num"] = num

                return params
            }
        }
        // 3) 생성한 StringRequest를 RequestQueue에 추가
        requestQueue.add(request)
    }


}