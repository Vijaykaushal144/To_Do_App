package com.example.todoapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentAddToDopopupBinding
import com.example.todoapp.utils.ToDoData
import com.google.android.material.textfield.TextInputEditText

class AddToDopopupFragment : DialogFragment() {

    private lateinit var binding: FragmentAddToDopopupBinding
    private lateinit var listener: DialogNextBtnClickListener
    private  var toDoData: ToDoData? = null


    fun setListener(listener: DialogNextBtnClickListener) {
        this.listener = listener
    }


    companion object {
        const val TAG = "ADDToDoPopUpFragment"

        @JvmStatic
        fun newInstance(taskId: String, task: String) = AddToDopopupFragment().apply {
            arguments = Bundle().apply {
                putString("taskId", taskId)
                putString("task", task)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddToDopopupBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if (arguments != null) {
            toDoData = ToDoData(
                arguments?.getString("taskId").toString(), arguments?.getString("task").toString()
            )

            binding.todoEt.setText(toDoData?.task)
        }
        registerEvents()
    }

    private fun registerEvents() {
        binding.todoNextBtn.setOnClickListener {
            val todoTask = binding.todoEt.text.toString()
            if (todoTask.isNotEmpty()) {

                if(toDoData==null)
                {
                    listener.onSaveTask(todoTask, binding.todoEt)

                }
                else
                {
                    toDoData?.task=todoTask

                    listener.onUpdateTask(toDoData!!,binding.todoEt)
                }
            } else {
                Toast.makeText(context, "Please enter some task", Toast.LENGTH_SHORT).show()
            }
        }
        binding.todoClose.setOnClickListener {
            dismiss()
        }
    }

    interface DialogNextBtnClickListener {
        fun onSaveTask(todo: String, todoEt: TextInputEditText)
        fun onUpdateTask(toDoData: ToDoData,todoEt: TextInputEditText)
    }
}