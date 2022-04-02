package com.example.teuxdeux

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.controls.ControlsProviderService.TAG
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val db = Firebase.firestore

        findViewById<Button>(R.id.submit).setOnClickListener {
            var is_found = false
            val user_email = findViewById<EditText>(R.id.signup_email).text.toString()
            val user_password = findViewById<EditText>(R.id.signup_password).text.toString()
            val user_confirm_password = findViewById<EditText>(R.id.confirm_password).text.toString()
            db.collection("users")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        //Log.d(TAG, "${document.id} => ${document.data}")
                        var email = document.getString("email").toString()
                        if(email == user_email){
                            is_found = true
                        }
                        if(is_found == true){
                            Toast.makeText(applicationContext,"Email Is Already In Use!!!!", Toast.LENGTH_SHORT).show()

                        }
                        else {
                            if(user_password != user_confirm_password){
                                Toast.makeText(applicationContext,"Passwords Do Not Match!!!!", Toast.LENGTH_SHORT).show()
                            }
                            else{
                                val user = hashMapOf(
                                    "email" to user_email,
                                    "password" to user_password
                                )
                                db.collection("users")
                                    .add(user)
                                    .addOnSuccessListener { documentReference ->
                                        Toast.makeText(applicationContext,"User Has Been Added!!!!", Toast.LENGTH_SHORT).show()
                                        startActivity(Intent(this, NavigationActivity::class.java))

                                    }
                                    .addOnFailureListener { e ->
                                        Log.w(TAG, "Error adding document", e)
                                    }

                            }



                        }

                    }

                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents.", exception)
                }
        }
    }
}