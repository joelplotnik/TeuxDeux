package com.example.teuxdeux.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.teuxdeux.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ManageTaskActivity : AppCompatActivity() {
    private var email: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remove_task)

        val task = intent.getStringExtra("task")
        val type = intent.getStringExtra("type")
        val importance = intent.getBooleanExtra("important", false)
        val completed = intent.getBooleanExtra("completed", false)
        email = intent.getStringExtra("user_email").toString()

        val editToDoTask: Button = findViewById(R.id.edit_to_do_button)
        editToDoTask.setOnClickListener {
            if (task != null && type != null) {
                removeFromDatabase(task, importance, type, completed)
            }

            val task = findViewById<EditText>(R.id.add_to_do_textfield).text.toString()
            var importance = false
            val importanceCheck = findViewById<CheckBox>(R.id.importance_checkbox)
            if (importanceCheck.isChecked()) {
                importance = true
            }
            val typeList = findViewById<Spinner>(R.id.type_spinner)
            val type = typeList.selectedItem.toString()
            val deadline = findViewById<EditText>(R.id.add_deadline_textfield).text.toString()

            saveToDatabase(task, importance, type, deadline)
        }

        val removeToDoTask = findViewById<Button>(R.id.remove_to_do_button)
        removeToDoTask.setOnClickListener {
            if (task != null && type != null) {
                removeFromDatabase(task, importance, type, completed)
            }
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
                    "Successfully edited To-Do task",
                    Toast.LENGTH_LONG
                ).show()
                this.finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to edit To-Do task", Toast.LENGTH_LONG)
                    .show()
            }
    }

    private fun removeFromDatabase(task: String, important: Boolean, type: String, completed: Boolean) {
        val db = Firebase.firestore
        db.collection("taskData")
            .whereEqualTo("email", email.toString())
            .whereEqualTo("task", task)
            .whereEqualTo("type", type)
            .whereEqualTo("important", important)
            .whereEqualTo("completed", completed)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    db.collection("taskData").document(document.id)
                        .delete()
                        .addOnSuccessListener {
                            Toast.makeText(this, "Successfully removed To-Do task", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Failed to remove To-Do task", Toast.LENGTH_SHORT).show()
                        }
                }
                this.finish()
            }
            .addOnFailureListener {
                val dialogBuilder = AlertDialog.Builder(this)
                dialogBuilder.setMessage("Unable to get data from firebase")
                    .setCancelable(true)
                val alert = dialogBuilder.create()
                alert.show()
            }
    }
}