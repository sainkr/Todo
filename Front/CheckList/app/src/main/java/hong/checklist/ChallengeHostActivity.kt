package hong.checklist

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import hong.checklist.Adapter.ChallengeTodoAdapter
import hong.checklist.DB.*
import hong.checklist.Listener.MyButtonClickListener
import hong.checklist.Listener.OnCheckListener
import kotlinx.android.synthetic.main.activity_challengehost.*

@SuppressLint("StaticFieldLeak")
class ChallengeHostActivity: AppCompatActivity(), OnCheckListener {

    lateinit var db : CheckListDatabase
    var challengeentityList = listOf<ChallengeEntity>()
    var challengeList = ArrayList<ChallengeContents>()

    var update_check = false
    var update_position = 0

    var code: Int =0
    var name: String =""

    val url_challenge = "http://192.168.35.135:8080/CheckList/ChallengeContent.jsp"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_challengehost)

        db = CheckListDatabase.getInstance(this)!!

        var intent = intent
        code = intent.extras?.getInt("code")!!
        name = intent.extras?.getString("name")!!

        val manager = LinearLayoutManager(this)
        manager.reverseLayout = true
        manager.stackFromEnd = true
        recyclerView_challengetodo.layoutManager = manager
        recyclerView_challengetodo.setHasFixedSize(true)


        getChallenge(code)


        tv_challengename.setText(name)


        // 스와이프해서 수정, 삭제
        val swipe = object : MySwipeHelper(this, recyclerView_challengetodo, 200){
            override fun instantiateMyButton(
                viewHolder: RecyclerView.ViewHolder,
                buffer: MutableList<MyButton>
            ) {
                buffer.add(
                    MyButton(context!!,
                        "삭제",
                        50,
                        0,
                        Color.parseColor("#FFFFFF"),
                        object : MyButtonClickListener {
                            override fun onClick(pos: Int) {

                                // 서버 저장
                                setChallengeTodoVolley(context, url_challenge, "deleteContent", code.toString(), challengeList.get(pos).num.toString() , "")

                                challengeList.removeAt(pos)
                                setRecyclerView(challengeList)
                                et_challengtodo.setText("")
                                update_check = false

                                //DB 저장
                                saveDB(code, name)

                            }
                        })
                )
                buffer.add(
                    MyButton(context!!,
                        "수정",
                        50,
                        0,
                        Color.parseColor("#000000"),
                        object : MyButtonClickListener {
                            override fun onClick(pos: Int) {

                                et_challengtodo.setText(challengeList[pos].content)

                                // 키보드 올리기
                                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                                imm.toggleSoftInput(
                                    InputMethodManager.SHOW_FORCED,
                                    InputMethodManager.HIDE_IMPLICIT_ONLY
                                )
                                update_check = true
                                update_position = pos

                            }
                        })
                )
            }

        }

        // edittext 완료 시 list 추가
        et_challengtodo.setOnEditorActionListener{ textView, action, event ->
            var handled = false

            if (action == EditorInfo.IME_ACTION_DONE) {
                if(et_challengtodo.text.toString().equals("")){
                    Toast.makeText(this, "챌린지를 입력해주세요.", Toast.LENGTH_SHORT).show()
                }
                else{
                    if(update_check){ // 수정하고 확인 눌렀을 때
                        challengeList.set(update_position,ChallengeContents(challengeList.get(update_position).num, et_challengtodo.text.toString(),challengeList.get(update_position).check))
                        update_check = false

                        // 서버 저장
                        setChallengeTodoVolley(this, url_challenge, "updateContent", code.toString(), challengeList.get(update_position).num.toString() , et_challengtodo.text.toString())

                    }
                    else{
                        if(challengeList.size == 0){
                            challengeList.add(ChallengeContents(1,et_challengtodo.text.toString(),0))
                            setChallengeTodoVolley(this, url_challenge, "addContent", code.toString(), "1", et_challengtodo.text.toString())
                        }
                        else{
                            challengeList.add(ChallengeContents(challengeList.get(challengeList.size-1).num + 1,et_challengtodo.text.toString(),0))

                            setChallengeTodoVolley(this, url_challenge, "addContent", code.toString(), (challengeList.get(challengeList.size-1).num + 1).toString(), et_challengtodo.text.toString())
                        }

                    }

                    et_challengtodo.setText("")
                    setRecyclerView(challengeList)

                    //DB 저장
                    saveDB(code, name)
                }

                // 키보드 내리기
                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(et_challengtodo.windowToken, 0)
                handled = true
            }

            handled
        }


    }

    fun setRecyclerView(list : List<ChallengeContents>){
        recyclerView_challengetodo.adapter = ChallengeTodoAdapter(this, list,this)
    }

    override fun onCheckListener(position: Int, check: Int) {
        challengeList.set(position,ChallengeContents(challengeList.get(position).num,challengeList.get(position).content,check))
        saveDB(code, name)
    }

    fun setChallenge(challenge : ChallengeEntity){
        val insertTask = object : AsyncTask<Unit, Unit, Unit>() {
            override fun doInBackground(vararg p0: Unit?) {
                db.challengeDAO().insert(challenge)
            }
        }
        insertTask.execute()
    }

    fun getChallenge(num : Int){
        val insertTask = object : AsyncTask<Unit, Unit, Unit>() {
            override fun doInBackground(vararg p0: Unit?) {
                challengeentityList = db.challengeDAO().getChallenge(num)
            }
            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)
                if(challengeentityList.size > 0){
                    val list = challengeentityList.get(0).contents

                    if(list!= null){
                        for(i in list.indices) {
                            challengeList.add(ChallengeContents(list[i].num, list[i].content , list[i].check))
                        }
                        setRecyclerView(challengeList)
                    }

                }
            }
        }

        insertTask.execute()
    }

    fun saveDB(code : Int, name : String){
        // DB 저장
        val contentList : List<ChallengeContents> = challengeList

        val challenge = ChallengeEntity(code,name,1,contentList)
        setChallenge(challenge)
    }

    private fun setChallengeTodoVolley(
        context: Context,
        url: String,
        type: String,
        code: String,
        num: String,
        content: String
    ) {

        // 1. RequestQueue 생성 및 초기화
        var requestQueue = Volley.newRequestQueue(this)

        // 2. Request Obejct인 StringRequest 생성
        val request: StringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                if(response.equals("error")){

                }
                else{

                }
            },
            Response.ErrorListener { error ->
                Log.d("통신 실패",error.toString())
            }
        ) {
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["type"] = type
                params["code"] = code
                params["num"] = num
                params["content"] = content

                return params
            }
        }
        // 3) 생성한 StringRequest를 RequestQueue에 추가
        requestQueue.add(request)
    }
}