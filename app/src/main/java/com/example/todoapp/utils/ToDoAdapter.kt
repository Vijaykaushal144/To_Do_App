package com.example.todoapp.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.databinding.EachTodoItemBinding

class ToDoAdapter( val list:MutableList<ToDoData>):
    RecyclerView.Adapter<ToDoAdapter.TodoViewHolder>() {

    private var listener:ToDoAdapterClickInterface?=null
    fun setListener(listener:ToDoAdapterClickInterface)
    {
            this.listener=listener
    }


    inner  class TodoViewHolder(val binding:EachTodoItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {

            val binding=EachTodoItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return TodoViewHolder(binding)
    }

    override fun getItemCount(): Int {

            return list.size
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
       with(holder)
       {
           with(list[position])
           {
                binding.todoTask.text=this.task


           binding.deleteTask.setOnClickListener{

               listener?.ondeleteTaskBtnClicked(this)

           }

               binding.editTask.setOnClickListener {
                    listener?.onEditTaskBtnClicked(this)
               }
           }


       }
    }
    interface ToDoAdapterClickInterface{
        fun ondeleteTaskBtnClicked(toDoData: ToDoData)
        fun onEditTaskBtnClicked(toDoData: ToDoData)
    }
}