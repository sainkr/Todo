package hong.checklist.Fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import hong.checklist.*
import hong.checklist.Adapter.FriendAdapter
import hong.checklist.DB.*
import hong.checklist.Data.FriendContents
import hong.checklist.Listener.MyButtonClickListener
import kotlinx.android.synthetic.main.fragment_profile.*
import org.json.JSONArray


@SuppressLint("StaticFieldLeak")
class ProfileFragment : Fragment(){

    // 놓친 부분
    // 1. 친구 닉네임 동기화.
    // 2. 친구 삭제

    lateinit var db : CheckListDatabase
    var profileList = listOf<ProfileEntity>()

    var login_success = false
    var REQUEST_CODE = 10
    val url_friend = "http://192.168.35.76:8080/CheckList/Friend.jsp"

    var friendList = ArrayList<FriendContents>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        var tv_name : TextView= view.findViewById(R.id.tv_name)
        var tv_freindadd : TextView = view.findViewById(R.id.tv_profile_freindadd)
        val recyclerView_profile_freindlist: RecyclerView = view.findViewById(R.id.recyclerView_profile_freindlist)
        db = CheckListDatabase.getInstance(requireContext())!!

        val manager = LinearLayoutManager(context)
        recyclerView_profile_freindlist.layoutManager = manager
        recyclerView_profile_freindlist.setHasFixedSize(true)

        getProfile()

        tv_name.setOnClickListener{
            Log.d("로그인",login_success.toString())

            if(!login_success){
                val intent = Intent(requireContext(), LoginActivity::class.java)
                startActivityForResult(intent,REQUEST_CODE)
            }
        }

        tv_freindadd.setOnClickListener{
            val intent = Intent(requireContext(),AddFriendActivity::class.java)
            intent.putExtra("my_id",profileList[0].id)
            startActivity(intent)
        }

        // 스와이프해서 수정, 삭제
        val swipe = object : MySwipeHelper(context, recyclerView_profile_freindlist, 200){
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
                                friendVolley(requireContext(), "deleteFriend" ,url_friend, profileList[0].id ,friendList.get(pos).id)
                                friendList.removeAt(pos)
                                setRecyclerView(friendList)
                            }
                        })
                )
            }
        }

        return view
    }

    fun setRecyclerView(list : List<FriendContents>){
        recyclerView_profile_freindlist.adapter = FriendAdapter(context, list)
    }

    fun getProfile(){
        val getTask = object : AsyncTask<Unit, Unit, Unit>() {
            override fun doInBackground(vararg p0: Unit?) {
                profileList = db.profileDAO().getProfile()
            }
            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)
                if(profileList.size > 0){
                    login_success = true
                    tv_name.setText(profileList[0].name)
                    tv_name.setTextColor(Color.parseColor("#000000"))
                    friendVolley(requireContext(), "getFriend" ,url_friend, profileList[0].id ,"")
                }
            }
        }

        getTask.execute()
    }

    private fun friendVolley(
        context: Context,
        type : String,
        url: String,
        my_id: String,
        friend_id: String
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
                else if(response.equals("deleteFriendSuccess")){

                }
                else{
                    val jarray = JSONArray(response)
                    val size = jarray.length()

                    for (i in 0 until size) {
                        val jsonObject = jarray.getJSONObject(i)
                        val id = jsonObject.getString("id")
                        val name = jsonObject.getString("name")

                        friendList.add( FriendContents(id, name))
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
                params["type"] = type
                params["my_id"] = my_id
                params["friend_id"] = friend_id

                return params
            }
        }
        // 3) 생성한 StringRequest를 RequestQueue에 추가
        requestQueue.add(request)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_CODE){
            if(resultCode != Activity.RESULT_OK)
                return

            getProfile()
        }
    }

}

