package com.example.teuxdeux.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.teuxdeux.MainActivity
import com.example.teuxdeux.Model.ToDoTask
import com.example.teuxdeux.NavigationActivity
import com.example.teuxdeux.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_create_to_do.*
import org.w3c.dom.Text

class CreateToDoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_to_do)

        val createToDoTask: Button = findViewById(R.id.create_to_do_button)
        createToDoTask.setOnClickListener {
            val task = findViewById<EditText>(R.id.add_to_do_textfield).text.toString()
            var importance = false
            val importance_check = findViewById<CheckBox>(R.id.importance_checkbox)
            if (importance_check.isChecked()) {
                importance = true
            }
            val typeList = findViewById<Spinner>(R.id.type_spinner)
            val type = typeList.selectedItem.toString()
            val deadline = findViewById<EditText>(R.id.add_deadline_textfield).text.toString()

            saveToDatabase(task, importance, type, deadline)
        }
    }


    private fun saveToDatabase(task: String, important: Boolean, type: String, deadline: String) {
        val db = Firebase.firestore
        val taskData: MutableMap<String, Any> = HashMap()

        taskData["task"] = task
        taskData["important"] = important
        taskData["type"] = type
        taskData["deadline"] = deadline
        taskData["completed"] = false

        db.collection("taskData")
            .add(taskData)
            .addOnSuccessListener {
                Toast.makeText(
                    this,
                    "Successfully added To-Do task into firebase",
                    Toast.LENGTH_LONG
                ).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to add To-Do task into firebase", Toast.LENGTH_LONG)
                    .show()
            }
    }
}
