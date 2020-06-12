package com.example.aqsadmin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.bottom_app_bar.*

class MainActivity : AppCompatActivity() {

    lateinit var active_auction: active_auctions
    lateinit var manage_auction  : manage_auction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.btm_nav)
        active_auction = active_auctions()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_layout, active_auction)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.Home -> {
                    active_auction = active_auctions()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.frame_layout, active_auction)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()

                }
                R.id.manage_auction -> {
                    manage_auction = manage_auction()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.frame_layout, manage_auction)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }
            }
            true
        }

    }
}


