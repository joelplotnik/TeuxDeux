package com.example.teuxdeux.Model

import android.view.LayoutInflater

import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import com.example.teuxdeux.R
import kotlinx.android.synthetic.main.to_do_task_item.view.*

class ToDoListAdapter(val taskList : List<ToDoTask>) : RecyclerView.Adapter<ToDoListAdapter.ViewHolder>(), View.OnClickListener {

    private lateinit var listener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.to_do_task_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ToDoListAdapter.ViewHolder, position: Int) {
        val task : ToDoTask = taskList[position]
        holder.itemView.apply {
            task_completion.isChecked = task.completed == true
            to_do_task.text = task.task
            to_do_task.paint.isStrikeThruText = task.completed == true
            task_type.text = task.type
            task_importance.isInvisible = task.important == false
            task_deadline.text = task.deadline
        }
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                val position = absoluteAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(itemView, position)
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(itemView: View?, position: Int)
        fun onCheckBoxClicked(itemView: View?, position: Int, isChecked: Boolean)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }
}