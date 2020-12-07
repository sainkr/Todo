package hong.checklist.Fragment

import android.annotation.SuppressLint
import android.app.Activity
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hong.checklist.Adapter.ChallengenameAdapter
import hong.checklist.Adapter.FriendAdapter
import hong.checklist.ChallengeHostActivity
import hong.checklist.ChallengeMemberActivity
import hong.checklist.ChallengePlusActivity
import hong.checklist.DB.ChallengeEntity
import hong.checklist.DB.CheckListDatabase
import hong.checklist.DB.FriendEntity
import hong.checklist.DB.ProfileEntity
import hong.checklist.Listener.OnChallengeTouchListener
import hong.checklist.R
import kotlinx.android.synthetic.main.fragment_challenge.*

@SuppressLint("StaticFieldLeak")
class ChallengeFragment : Fragment(), OnChallengeTouchListener {

    var REQUEST_CODE = 10

    lateinit var db : CheckListDatabase
    var challengeentityList = listOf<ChallengeEntity>()
    var profileList = listOf<ProfileEntity>()

    var my_id:String =""
    var challengeList = ArrayList<String>()

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
        getChallenge()

        val manager = LinearLayoutManager(context)
        recyclerView_challengename.layoutManager = manager
        recyclerView_challengename.setHasFixedSize(true)

        tv_challengeplus.setOnClickListener {
            if(my_id.equals("")){
                Toast.makeText(requireContext(), "로그인이 필요한 서비스입니다.", Toast.LENGTH_LONG).show()
            }
            else{
                val intent = Intent(requireContext(), ChallengePlusActivity::class.java)
                intent.putExtra("host_id",my_id)
                startActivityForResult(intent,REQUEST_CODE)
            }
        }
        return view
    }

    fun setRecyclerView(list : List<String>){
        recyclerView_challengename.adapter = ChallengenameAdapter(context, list,this)
    }

    override fun onChallengeTouchListener(position: Int) {
        if(challengeentityList.get(position).add == 1) // 방장이면
        {
            val intent = Intent(requireContext(), ChallengeHostActivity::class.java)
            intent.putExtra("code",challengeentityList.get(position).code)
            intent.putExtra("name",challengeentityList.get(position).name)
            intent.putExtra("my_id",my_id)
            // intent.putParcelableArrayListExtra("challengeeentityList",challengeentityList )
            startActivity(intent)
        }
        else{
            val intent = Intent(requireContext(), ChallengeMemberActivity::class.java)
            intent.putExtra("code",challengeentityList.get(position).code)
            intent.putExtra("name",challengeentityList.get(position).name)
            intent.putExtra("my_id",my_id)
            startActivity(intent)
        }
    }

    fun getProfile() {
        val getTask = object : AsyncTask<Unit, Unit, Unit>() {
            override fun doInBackground(vararg p0: Unit?) {
                profileList = db.profileDAO().getProfile()
            }

            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)
                if (profileList.size > 0) {
                    my_id = profileList.get(0).id
                }
            }
        }
        getTask.execute()
    }


    fun getChallenge(){
        val insertTask = object : AsyncTask<Unit, Unit, Unit>() {
            override fun doInBackground(vararg p0: Unit?) {
                challengeentityList = db.challengeDAO().getChallenge()
            }
            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)
               if(challengeentityList.size > 0){
                   for(i in 0 until challengeentityList.size ){
                       challengeList.add(challengeentityList.get(i).name)
                   }
                   setRecyclerView(challengeList)
               }
            }
        }

        insertTask.execute()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_CODE){
            if(resultCode != Activity.RESULT_OK)
                return
            getChallenge()
        }
    }

}