package hong.checklist

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import hong.checklist.Fragment.CalendarFragment
import hong.checklist.Fragment.ChallengeFragment
import hong.checklist.Fragment.HomeFragment
import hong.checklist.Fragment.ProfileFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener{


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottom_navigation.setOnNavigationItemSelectedListener(this)

       val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_layout, HomeFragment())
        transaction.commit()

        bottom_navigation.itemIconTintList = null

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.home->{
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.frame_layout, HomeFragment())
                transaction.commit()
                return true
            }

            R.id.calendar->{
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.frame_layout, CalendarFragment())
                transaction.commit()
                return true
            }

            R.id.challenge->{
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.frame_layout, ChallengeFragment())
                transaction.commit()
                return true
            }

            R.id.profile->{
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.frame_layout, ProfileFragment())
                transaction.commit()
                return true
            }

        }

        return false
    }

}