package com.example.teuxdeux

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.teuxdeux.fragments.CalendarFragment
import com.example.teuxdeux.fragments.HomeFragment
import com.example.teuxdeux.fragments.SettingsFragment
import com.example.teuxdeux.fragments.ToDoFragment
import kotlinx.android.synthetic.main.activity_navigation.*

class NavigationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)

        val homeFragment = HomeFragment()
        val todoFragment = ToDoFragment()
        val calendarFragment = CalendarFragment()
        val settingsFragment = SettingsFragment()

        changeFragment(homeFragment)

        bottom_nav.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.ic_home -> {
                    changeFragment(homeFragment)
                    true
                }
                R.id.ic_notes -> {
                    changeFragment(todoFragment)
                    true
                }
                R.id.ic_calendar -> {
                    changeFragment(calendarFragment)
                    true
                }
                R.id.ic_settings -> {
                    changeFragment(settingsFragment)
                    true
                }
                else -> false
            }
        }

    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fContainer,fragment)
            commit()
        }
    }
}