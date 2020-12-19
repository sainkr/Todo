package hong.checklist.Fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import hong.checklist.Adapter.ChallengenameAdapter
import hong.checklist.ChallengeHostActivity
import hong.checklist.ChallengeMemberActivity
import hong.checklist.ChallengeAddActivity
import hong.checklist.DB.CheckListDatabase
import hong.checklist.DB.ProfileEntity
import hong.checklist.DB.TodoContents
import hong.checklist.Data.ChallengeContents
import hong.checklist.Listener.OnChallengeTouchListener
import hong.checklist.R
import kotlinx.android.synthetic.main.fragment_challenge.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray

@SuppressLint("StaticFieldLeak")
class ChallengeFragment : Fragment(), OnChallengeTouchListener {

    var REQUEST_CODE = 10

    lateinit var db : CheckListDatabase
    var profileList = listOf<ProfileEntity>()

    var my_id:String =""
    var challengeList = ArrayList<ChallengeContents>()

    val url_challengetodo = "http://192.168.35.76:8080/CheckList/ChallengeTodo.jsp"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_challenge, container, false)
        val tv_challengeplus: TextView = view.findViewById(R.id.tv_challengeplus)
        val recyclerView_challengename : RecyclerView = view.findViewById(R.id.recyclerView_challengename)

        db = CheckListDatabase.getInstance(requireContext())!!

        getProfile()

        val manager = LinearLayoutManager(context)
        recyclerView_challengename.layoutManager = manager
        recyclerView_challengename.setHasFixedSize(true)

        tv_challengeplus.setOnClickListener {
            if(my_id.equals("")){
                Toast.makeText(requireContext(), "로그인이 필요한 서비스입니다.", Toast.LENGTH_LONG).show()
            }
            else{
                val intent = Intent(requireContext(), ChallengeAddActivity::class.java)
                intent.putExtra("host_id",my_id)
                startActivityForResult(intent,REQUEST_CODE)
            }
        }
        return view
    }

    fun setRecyclerView(list : List<ChallengeContents>){
        recyclerView_challengename.adapter = ChallengenameAdapter(context, list,this)
    }

    override fun onChallengeTouchListener(position: Int) {
        if(challengeList.get(position).host == 1) // 방장이면
        {
            val intent = Intent(requireContext(), ChallengeHostActivity::class.java)
            intent.putExtra("code",challengeList.get(position).code)
            intent.putExtra("name",challengeList.get(position).name)
            intent.putExtra("my_id",my_id)
            startActivityForResult(intent,REQUEST_CODE)
        }
        else{
            val intent = Intent(requireContext(), ChallengeMemberActivity::class.java)
            intent.putExtra("code",challengeList.get(position).code)
            intent.putExtra("name",challengeList.get(position).name)
            intent.putExtra("my_id",my_id)
            startActivityForResult(intent,REQUEST_CODE)
        }
    }

    fun getProfile() {
        lifecycleScope.launch(Dispatchers.IO){
            profileList = db.profileDAO().getProfile()
            if (profileList.size > 0) {
                my_id = profileList.get(0).id
                getChallengeVolley(requireContext(), url_challengetodo, my_id)
            }
        }
    }

    private fun getChallengeVolley(
        context: Context,
        url: String,
        my_id : String
    ) {

        // 1. RequestQueue 생성 및 초기화
        var requestQueue = Volley.newRequestQueue(context)

        // 2. Request Obejct인 StringRequest 생성
        val request: StringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                if(response.equals("error")){


                }
                else if(response.equals("challengeNoting")){

                }
                else{
                    val jarray = JSONArray(response)
                    val size = jarray.length()

                    for (i in 0 until size) {
                        val jsonObject = jarray.getJSONObject(i)

                        val name = jsonObject.getString("name")
                        val code = jsonObject.getInt("code")
                        val host = jsonObject.getInt("host")

                        challengeList.add(ChallengeContents(name, code, host))
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
                params["type"] = "getChallenge"
                params["my_id"] = my_id
                params["code"] = ""
                params["num"] = ""

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

            challengeList.clear()
            getChallengeVolley(requireContext(), url_challengetodo, my_id)
        }
    }

}