package com.example.teuxdeux

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
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

        val email = intent.getStringExtra("user_email").toString()
        Log.d("USER_EMAIL", email)
        val homeFragment = HomeFragment.newInstance(email)
        val todoFragment = ToDoFragment.newInstance(email)
        val calendarFragment = CalendarFragment.newInstance(email)
        val settingsFragment = SettingsFragment.newInstance(email)

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
                    permissionRequest()
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

    private fun permissionRequest(){
        var permissionList = mutableListOf<String>()
        if (!(ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED)){
            permissionList.add(Manifest.permission.READ_CALENDAR)
        }
        if (!(ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED)){
            permissionList.add(Manifest.permission.WRITE_CALENDAR)
        }
        if (permissionList.isNotEmpty()){
            //Log.d("XYZ", "${permissionList.toTypedArray()}")
            ActivityCompat.requestPermissions(this, permissionList.toTypedArray(),100)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            100 -> {
                for (index in grantResults.indices){
                    if (grantResults[index] == PackageManager.PERMISSION_GRANTED){
                        Log.d("XYZ", "Your ${permissions[index]} successfully granted")
                    }
                }
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