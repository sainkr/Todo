package hong.checklist.Fragment

import android.annotation.SuppressLint
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
import hong.checklist.AddFriendActivity
import hong.checklist.DB.CheckListDatabase
import hong.checklist.DB.ProfileEntity
import hong.checklist.LoginActivity
import hong.checklist.R
import kotlinx.android.synthetic.main.fragment_profile.*


@SuppressLint("StaticFieldLeak")
class ProfileFragment : Fragment(){

    lateinit var db : CheckListDatabase
    var profileList = listOf<ProfileEntity>()

    var login_success = false


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

        db = CheckListDatabase.getInstance(requireContext())!!

        getProfile()

        tv_name.setOnClickListener{
            Log.d("로그인",login_success.toString())
            Log.d("이름",profileList[0].name)
            if(!login_success){
                val intent = Intent(requireContext(), LoginActivity::class.java)
                startActivity(intent)
            }
        }

        tv_freindadd.setOnClickListener{
            val intent = Intent(requireContext(),AddFriendActivity::class.java)
            intent.putExtra("my_id",profileList[0].id)
            startActivity(intent)
        }

        return view
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
                }
            }
        }

        getTask.execute()
    }

}
