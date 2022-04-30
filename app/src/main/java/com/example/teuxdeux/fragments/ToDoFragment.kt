package com.example.teuxdeux.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.teuxdeux.Model.ToDoListAdapter
import com.example.teuxdeux.Model.ToDoTask
import com.example.teuxdeux.R
import com.example.teuxdeux.activities.CreateToDoActivity
import com.example.teuxdeux.activities.ManageTaskActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


/**
 * A simple [Fragment] subclass.
 * Use the [ToDoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ToDoFragment : Fragment() {
    private var email: String? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var documentArrayList : ArrayList<ToDoTask>
    private lateinit var displayAdapter : ToDoListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            email = it.getString("email")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_to_do, container, false)
        val fab: FloatingActionButton = view.findViewById(R.id.add_task_button)
        fab.setOnClickListener {
            activity?.let {
                val intent = Intent(it, CreateToDoActivity::class.java)
                intent.putExtra("user_email", email)
                it.startActivity(intent)
            }
        }

        recyclerView = view.findViewById(R.id.recycler_view_tasks)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
        documentArrayList = arrayListOf()
        displayAdapter = ToDoListAdapter(documentArrayList)
        recyclerView.adapter = displayAdapter

        getListFromFirebase()

        displayAdapter.setOnItemClickListener(object : ToDoListAdapter.OnItemClickListener {
            override fun onItemClick(itemView: View?, position: Int) {
                val task = displayAdapter.taskList[position].task
                val type = displayAdapter.taskList[position].type
                val importance = displayAdapter.taskList[position].important
                val completed = displayAdapter.taskList[position].completed

                val intent = Intent(activity, ManageTaskActivity::class.java)
                intent.putExtra("task", task)
                intent.putExtra("type", type)
                intent.putExtra("important", importance)
                intent.putExtra("completed", completed)
                intent.putExtra("user_email", email)
                startActivity(intent)
            }

            override fun onCheckBoxClicked(itemView: View?, position: Int, isChecked: Boolean) {
                TODO("Not yet implemented")
            }
        })


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
            ToDoFragment().apply {
                arguments = Bundle().apply {
                    putString("email", email)
                }
            }
    }

    private fun getListFromFirebase() {
        val db = Firebase.firestore
        db.collection("taskData")
            .whereEqualTo("email", email)
            .orderBy("deadline", Query.Direction.ASCENDING)
            .addSnapshotListener(object: EventListener<QuerySnapshot> {
                override fun onEvent(
                    value: QuerySnapshot?,
                    error: FirebaseFirestoreException?
                ) {
                    if(error != null) {
                        Log.e("Firebase Error", error.message.toString())
                        return
                    }
                    documentArrayList.clear()
                    documentArrayList.addAll(value!!.toObjects(ToDoTask::class.java))
                    displayAdapter.notifyDataSetChanged()
                }
            })
    }
}