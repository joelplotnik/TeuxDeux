package com.example.teuxdeux

import android.content.Intent
import android.os.Bundle
import android.service.controls.ControlsProviderService.TAG
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)




        val db = Firebase.firestore




        findViewById<Button>(R.id.login).setOnClickListener {
            val user_email = findViewById<EditText>(R.id.email).text.toString()
            val user_password = findViewById<EditText>(R.id.password).text.toString()
            var is_found = false
            db.collection("users")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        var email = document.getString("email").toString()
                        if(user_email == email){
                            is_found = true
                            var password = document.getString("password").toString()
                            if(password == user_password){
                                startActivity(Intent(this, NavigationActivity::class.java))
                            }
                            else{
                                Toast.makeText(applicationContext,"Incorrect Password", Toast.LENGTH_SHORT).show()

                            }
                        }
                    }
                    if(is_found == false){
                        Toast.makeText(applicationContext,"User Does Not Exist!!!!", Toast.LENGTH_SHORT).show()

                    }
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents.", exception)
                }
        }

        findViewById<Button>(R.id.signup).setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }
}