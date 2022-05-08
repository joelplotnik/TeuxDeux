package com.example.teuxdeux.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.controls.ControlsProviderService.TAG
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.teuxdeux.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val db = Firebase.firestore

        findViewById<Button>(R.id.submit).setOnClickListener {
            val user_email = findViewById<EditText>(R.id.signup_email).text.toString()
            val user_password = findViewById<EditText>(R.id.signup_password).text.toString()
            val user_confirm_password = findViewById<EditText>(R.id.confirm_password).text.toString()


            if(user_password != user_confirm_password){
                Toast.makeText(applicationContext,"Passwords Do Not Match!!!!", Toast.LENGTH_SHORT).show()
            } else if (!user_email.isEmailValid()) {
                Toast.makeText(applicationContext,"Invalid Email!!!!", Toast.LENGTH_SHORT).show()
            } else {
                db.collection("users")
                    .whereEqualTo("email", user_email)
                    .get()
                    .addOnSuccessListener { result ->
                        if (result.isEmpty()) {
                            val user = hashMapOf(
                                "email" to user_email,
                                "password" to user_password
                            )
                            db.collection("users")
                                .add(user)
                                .addOnSuccessListener { documentReference ->
                                    Toast.makeText(applicationContext,"User Has Been Added!!!!", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(this, NavigationActivity::class.java)
                                    intent.putExtra("user_email", user_email)
                                    startActivity(intent)
                                }
                                .addOnFailureListener { e ->
                                    Log.w(TAG, "Error adding document", e)
                                }
                        } else {
                            Toast.makeText(applicationContext,"Email already in use!!!!", Toast.LENGTH_SHORT).show()
                        }

                    }.addOnFailureListener { exception ->
                        Log.w(TAG, "Error getting documents.", exception)
                    }
            }
        }
    }

    fun String.isEmailValid(): Boolean {
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }
}