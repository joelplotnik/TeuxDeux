package com.example.teuxdeux.fragments

import android.app.Activity
import android.app.usage.UsageEvents
import android.content.Intent
import android.icu.util.Calendar
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
import ru.cleverpumpkin.calendar.CalendarDate
import ru.cleverpumpkin.calendar.CalendarView
import java.util.*
import java.util.function.ToIntFunction


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

        val calendarId = email
        val view: View =
            inflater.inflate(com.example.teuxdeux.R.layout.fragment_calendar , container , false)

        val calendarView = view.findViewById<CalendarView>(R.id.calendar_view)
        val calendar = Calendar.getInstance()

        val month = 4
        // Initial date
        calendar.set(2022 , 5 , 1)
        val initialDate = CalendarDate(calendar.time)

// Minimum available date
        calendar.set(2022 , 6 , 1)
        val minDate = CalendarDate(calendar.time)

// Maximum available date
        calendar.set(2022 , 7 , 15)
        val maxDate = CalendarDate(calendar.time)

// The first day of week
        val firstDayOfWeek = java.util.Calendar.MONDAY


// Set up calendar with all available parameters
        calendarView.setupCalendar(
            initialDate = initialDate ,
            minDate = minDate ,
            maxDate = maxDate ,
            selectionMode = CalendarView.SelectionMode.NONE ,
            firstDayOfWeek = firstDayOfWeek ,
            showYearSelectionView = true
        )


        // Set up calendar with SelectionMode.SINGLE
       calendarView.setupCalendar(selectionMode = CalendarView.SelectionMode.MULTIPLE)

        val selectedDates: MutableList<CalendarDate> = calendarView.selectedDates.toMutableList()

        db.collection("taskData")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {

                    val task = document.getString("task").toString()
                    val deadline = document.getString("deadline").toString()
                    val type = document.getString("type").toString()

                    Log.d("string" , deadline)
                   if(deadline.isNotEmpty()) {
                       Log.d("loop","in")
                       var y = 0
                       var m = 0
                       var d = 0

                        var ye = ""
                        var mo = ""
                        var da = ""
                        var c = 0

                        while ( c < 4) {
                            ye += deadline.get(c)
                            c = c+1
                        }
                        y = ye.toInt()
                        c = 5
                       if(deadline.get(c) !='0' && deadline.get(c)!='1'){
                        mo = "0"
                        mo += deadline.get(c)
                       c = 7}
                       else{
                        while (c <= 6) {
                            mo += deadline.get(c)
                            c = c+1
                        }
                        c = 8}
                        m = mo.toInt()
                       m = m-1
                       Log.d("month", m.toString())

                        while (c < deadline.length) {
                            da += deadline.get(c)
                            c = c+1
                        }
                        d = da.toInt()
                        Log.d("year" , y.toString())
                       // Log.d("month" , m.toString())
                        Log.d("day" , d.toString())

                        calendar.set(y , m , d)
                        val dates = CalendarDate(calendar.time)
                       selectedDates += dates
                       calendarView.updateSelectedDates(selectedDates)


/*
                        val event = Event()
                            .setSummary(task)
                            .setDescription(type)
                        val startDateTime = DateTime(deadline)
                        val start = EventDateTime()
                            .setDateTime(startDateTime)
                            .setTimeZone("America/Los_Angeles")

                        event.start = start
                        event.endTimeUnspecified*/

                        //calendarId.add(event)
                    }

                }
            }
            .addOnFailureListener { exception ->
                Log.w(ControlsProviderService.TAG , "Error getting documents." , exception)
            }

        calendarView.onDateClickListener = { date ->

            // Do something ...
            // for example get list of selected dates
            val selectedDates = calendarView.selectedDates
        }




        return view
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



