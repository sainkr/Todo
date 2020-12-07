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
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import hong.checklist.Adapter.FriendAdapter
import hong.checklist.Adapter.TodoAdapter
import hong.checklist.AddFriendActivity
import hong.checklist.DB.*
import hong.checklist.LoginActivity
import hong.checklist.R
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_profile.*
import org.json.JSONArray
import org.json.JSONException


@SuppressLint("StaticFieldLeak")
class ProfileFragment : Fragment(){

    // 놓친 부분
    // 1. 친구 닉네임 동기화.
    // 2. 친구 삭제

    lateinit var db : CheckListDatabase
    var profileList = listOf<ProfileEntity>()
    var friendentityList = listOf<FriendEntity>()

    var login_success = false
    var REQUEST_CODE = 10
    val url_request = "http://192.168.35.76:8080/CheckList/Friend.jsp"

    var friendList = ArrayList<String>()


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

        return view
    }

    fun setRecyclerView(list : List<String>){
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
                    // friendVolley(requireContext(),url_request, profileList[0].id )
                    getFriend()
                }
            }
        }

        getTask.execute()
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
                        friendList.add(friendentityList.get(i).name)
                    setRecyclerView(friendList)
                }
            }
        }

        getTask.execute()
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

