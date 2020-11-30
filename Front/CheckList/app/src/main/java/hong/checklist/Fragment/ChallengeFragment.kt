package hong.checklist.Fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import hong.checklist.ChallengePlusActivity
import hong.checklist.R
import kotlinx.android.synthetic.main.fragment_profile.*


class ChallengeFragment : Fragment() {

    var REQUEST_CODE = 10

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

        tv_challengeplus.setOnClickListener {
            val intent = Intent(requireContext(), ChallengePlusActivity::class.java)
            startActivityForResult(intent,REQUEST_CODE)
        }
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_CODE){
            if(resultCode != Activity.RESULT_OK)
                return

            tv_name.setText(data?.extras?.getString("name"))
            tv_name.setTextColor(Color.parseColor("#000000"))
        }
    }

}