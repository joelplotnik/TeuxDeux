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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ToDoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ToDoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var documentArrayList : ArrayList<ToDoTask>
    private lateinit var displayAdapter : ToDoListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
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
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ToDoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ToDoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun getListFromFirebase() {
        val db = Firebase.firestore
        db.collection("taskData").orderBy("deadline", Query.Direction.ASCENDING)
            .addSnapshotListener(object: EventListener<QuerySnapshot> {
                override fun onEvent(
                    value: QuerySnapshot?,
                    error: FirebaseFirestoreException?
                ) {
                    if(error != null) {
                        Log.e("Firebase Error", error.message.toString())
                        return
                    }
                    for(dc: DocumentChange in value?.documentChanges!!) {
                        if(dc.type == DocumentChange.Type.ADDED) {
                            documentArrayList.add(dc.document.toObject(ToDoTask::class.java))
                        }
                    }
                    displayAdapter.notifyDataSetChanged()
                }
            })
    }
}