package com.ruderarajput.earningapp.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ruderarajput.earningapp.Fragmemts.HistoryFragment
import com.ruderarajput.earningapp.Fragmemts.HomeFragment
import com.ruderarajput.earningapp.Fragmemts.ProfileFragment
import com.ruderarajput.earningapp.Fragmemts.SpinFragment
import com.ruderarajput.earningapp.R

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        loadFragment(HomeFragment())

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navi)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> loadFragment(HomeFragment())
                R.id.spinFragment -> loadFragment(SpinFragment())
                R.id.historyFragment -> loadFragment(HistoryFragment())
                R.id.profileFragment -> loadFragment(ProfileFragment())
            }
            true
        }

    }
    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}