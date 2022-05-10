package com.example.teuxdeux.fragments

import android.os.Build
import android.os.Bundle
import android.service.controls.ControlsProviderService
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.teuxdeux.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class SingleDayFragment : Fragment() {


    private val db = Firebase.firestore
    private var email: String? = null

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
        val view: View = inflater.inflate(R.layout.single_day_click, container, false)

        var day = "null"
        val bundle = this.arguments
        if (bundle != null) {
            bundle.getString("date").also {
                if (it != null) {
                    day = it
                }
            }
        }
        var y = 0
        var m = 0
        var d = 0

        var ye = ""
        var mo = ""
        var da = ""
        var c = 0

        while ( c < 2) {
            ye += day.get(c)
            c = c+1
        }
        y = ye.toInt()
        c = 2
        if(day.get(c) !='0' && day.get(c)!='1'){
            mo = "0"
            mo += day.get(c+1)
            c = 5}
        else{
            c = 3
            while (c < 4) {
                mo += day.get(c)
                c = c+1
            }
            c = 5}
        m = mo.toInt()
        while (c < day.length) {
            da += day.get(c)
            c = c+1
        }
        d = da.toInt()
        val dates = d.toString() + "-" + m.toString() + "-" + y.toString()

        val task = view.findViewById<EditText>(R.id.task) as TextView
        val date = view.findViewById<EditText>(R.id.task_deadline) as TextView
        val type = view.findViewById<EditText>(R.id.task_discription) as TextView

        db.collection("taskData")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {

                    val task1 = document.getString("task").toString()
                    val deadline1 = document.getString("deadline").toString()
                    val type1 = document.getString("type").toString()

                    if(dates == deadline1){

                        task.text = task1
                        date.text = deadline1
                        type.text = type1

                    }

                }
            }
            .addOnFailureListener { exception ->
                Log.w(ControlsProviderService.TAG , "Error getting documents." , exception)
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