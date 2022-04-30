package com.example.teuxdeux.fragments

import android.app.Activity
import android.app.usage.UsageEvents
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.CalendarContract
import android.service.controls.ControlsProviderService
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.teuxdeux.Model.ToDoListAdapter
import com.example.teuxdeux.Model.ToDoTask
import com.example.teuxdeux.NavigationActivity
import com.example.teuxdeux.R
import com.example.teuxdeux.activities.ManageTaskActivity
import com.google.api.client.util.DateTime
import com.google.api.services.calendar.model.Event
import com.google.api.services.calendar.model.EventAttendee
import com.google.api.services.calendar.model.EventDateTime
import com.google.api.services.calendar.model.EventReminder
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_calendar.*
import java.util.*



class CalendarFragment : Fragment() {


    private val db = Firebase.firestore
    private var email: String? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var documentArrayList: ArrayList<ToDoTask>
    private lateinit var displayAdapter: ToDoListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            email = it.getString("email")
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateView(
        inflater: LayoutInflater , container: ViewGroup? ,
        savedInstanceState: Bundle?
    ): View? {

        val view: View =
            inflater.inflate(com.example.teuxdeux.R.layout.fragment_calendar , container , false)

        val calendarId = db.collection("username")
        val cal= cView

        db.collection("taskData")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {

                    val task = document.getString("task").toString()
                    val deadline = document.getString("deadline")
                    val type = document.getString("type").toString()
/*
                    val intent = Intent(Intent.ACTION_INSERT)
                        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, deadline)
                        .putExtra(CalendarContract.Events.TITLE, task)
                        .putExtra(CalendarContract.Events.DESCRIPTION, type)*/


                    val event = Event()
                        .setSummary(task)
                        .setDescription(type)
                    val startDateTime = DateTime(deadline)
                    val start = EventDateTime()
                        .setDateTime(startDateTime)
                        .setTimeZone("America/Los_Angeles")

                    event.start = start
                    event.endTimeUnspecified

                    calendarId.add(event)

                }
            }
            .addOnFailureListener { exception ->
                Log.w(ControlsProviderService.TAG , "Error getting documents." , exception)
            }


        recyclerView = view.findViewById(com.example.teuxdeux.R.id.recycler)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
        documentArrayList = arrayListOf()
        displayAdapter = ToDoListAdapter(documentArrayList)
        recyclerView.adapter = displayAdapter

        getListFromFirebase()

        displayAdapter.setOnItemClickListener(object : ToDoListAdapter.OnItemClickListener {
            override fun onItemClick(itemView: View? , position: Int) {
                val task = displayAdapter.taskList[position].task


                val intent = Intent(activity , ManageTaskActivity::class.java)
                intent.putExtra("task" , task)
                startActivity(intent)
            }

            override fun onCheckBoxClicked(itemView: View? , position: Int , isChecked: Boolean) {
                TODO("Not yet implemented")
            }
        })

        return view
    }

    private fun getListFromFirebase() {
        val db = Firebase.firestore
        if (db.collection("deadline") == Date()) {
            db.collection("taskData").orderBy("deadline" , Query.Direction.ASCENDING)
                .addSnapshotListener(object : EventListener<QuerySnapshot> {
                    override fun onEvent(
                        value: QuerySnapshot? ,
                        error: FirebaseFirestoreException?
                    ) {

                        if (error != null) {
                            Log.e("Firebase Error" , error.message.toString())
                            return
                        }
                    }

                })
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param email Email Address of user
         **/

        @JvmStatic
        fun newInstance(email: String) =
            CalendarFragment().apply {
                arguments = Bundle().apply {
                    putString("email", email)
                }
            }
    }
}



