package hong.checklist

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import hong.checklist.Adapter.ChallengeFriendAdapter
import hong.checklist.Adapter.TodoAdapter
import hong.checklist.DB.*
import hong.checklist.Listener.OnCheckListener
import kotlinx.android.synthetic.main.activity_challengeplus.*
import kotlinx.android.synthetic.main.fragment_profile.*

@SuppressLint("StaticFieldLeak")
class ChallengePlusActivity : AppCompatActivity(), OnCheckListener {

    // 인원제한 100명까지
    // 로그인 안했으면 로그인 페이지로

    lateinit var db : CheckListDatabase
    var friendentityList = listOf<FriendEntity>()
    var friendList = ArrayList<ChallengeFriendContents>()
    var check_list = ArrayList<Int>()

    val url_challenge = "http://192.168.35.135:8080/CheckList/Challenge.jsp"

    var host_id =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_challengeplus)

        var intent = intent
        host_id = intent.getStringExtra("host_id")

        db = CheckListDatabase.getInstance(this)!!

        val manager = LinearLayoutManager(this)
        recyclerView_challengeplus.layoutManager = manager
        recyclerView_challengeplus.setHasFixedSize(true)

        getFriend()

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
                    member += " $friendList.get(check_list.get(i)).id"
                }

                if(check_list.size == 0)
                    Toast.makeText(this, "모임원을 선택해주세요", Toast.LENGTH_LONG).show()
                else {
                    addVolley(this, url_challenge, name,host_id, member)
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

        friendList.set(position,ChallengeFriendContents(friendList.get(position).name,check,friendList.get(position).id))
        setRecyclerView(friendList)
    }

    fun getFriend(){
        val getTask = object : AsyncTask<Unit, Unit, Unit>() {
            override fun doInBackground(vararg p0: Unit?) {
                friendentityList = db.friendDAO().getFriend()
            }
            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)
                if(friendentityList.size > 0){
                    for(i in 0 until friendentityList.size)
                        friendList.add(ChallengeFriendContents(friendentityList.get(i).name, 0,friendentityList.get(i).code))
                    setRecyclerView(friendList)
                }
            }
        }

        getTask.execute()
    }

    fun addChallenge(challenge : ChallengeEntity){
        val insertTask = object : AsyncTask<Unit, Unit, Unit>() {
            override fun doInBackground(vararg p0: Unit?) {
                db.challengeDAO().insert(challenge)
            }
            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)
                val intentR = intent
                setResult(RESULT_OK,intentR); //결과를 저장
                finish()
            }
        }

        insertTask.execute()
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
                    var list = ArrayList<ChallengeContents>()

                    addChallenge(ChallengeEntity(response.toInt(),name,1))
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
                params["member"] = member

                return params
            }
        }
        // 3) 생성한 StringRequest를 RequestQueue에 추가
        requestQueue.add(request)
    }

}
