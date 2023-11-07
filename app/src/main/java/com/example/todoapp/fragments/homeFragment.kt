package com.example.todoapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentHomeBinding
import com.example.todoapp.utils.ToDoAdapter
import com.example.todoapp.utils.ToDoData
import com.google.android.material.tabs.TabLayout.TabGravity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase


class homeFragment : Fragment(), AddToDopopupFragment.DialogNextBtnClickListener,
    ToDoAdapter.ToDoAdapterClickInterface {
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseref: DatabaseReference
    private lateinit var navControl: NavController
    private lateinit var binding: FragmentHomeBinding
    private var popUpfragment: AddToDopopupFragment? = null
    private lateinit var adapter: ToDoAdapter
    private lateinit var mList: MutableList<ToDoData>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        getDataFromFirebase()
        registerEvents()
    }

    private fun registerEvents() {
        binding.addbtnHome.setOnClickListener {
            if (popUpfragment != null)
                childFragmentManager.beginTransaction().remove(popUpfragment!!).commit()
            popUpfragment = AddToDopopupFragment()
            popUpfragment!!.setListener(this)
            popUpfragment!!.show(
                childFragmentManager,
                AddToDopopupFragment.TAG
            )
        }

        binding.logout.setOnClickListener {
           //navControl=Navigation.findNavController()
            auth.signOut()
            navControl.navigate(R.id.action_homeFragment_to_signUpFragment)


        }
    }

    private fun init(view: View) {
        navControl = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
        databaseref = FirebaseDatabase.getInstance()
            .reference.child("Tasks").child(auth.currentUser?.uid.toString())

        binding.recyclerview.setHasFixedSize(true)
        binding.recyclerview.layoutManager = LinearLayoutManager(context)
        mList = mutableListOf()
        adapter = ToDoAdapter(mList)
        adapter.setListener(this)

        binding.recyclerview.adapter = adapter

    }

    private fun getDataFromFirebase() {
        databaseref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                mList.clear()
                for (taskSnapshot in snapshot.children) {
                    val todoTask = taskSnapshot.key?.let {
                        ToDoData(it, taskSnapshot.value.toString())
                    }

                    if (todoTask != null) {
                        mList.add(todoTask)
                    }
                }
                adapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onSaveTask(todo: String, todoEt: TextInputEditText) {
        databaseref.push().setValue(todo).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(context, "Task Save succesfully", Toast.LENGTH_SHORT).show()


            } else {
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
            todoEt.text = null
            popUpfragment!!.dismiss()
        }
    }

    override fun onUpdateTask(toDoData: ToDoData, todoEt: TextInputEditText) {

        val map = HashMap<String, Any>()
        map[toDoData.taskId] = toDoData.task
        databaseref.updateChildren(map).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(context, "TODO Saved Successfullly", Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
            }

            todoEt.text=null
            popUpfragment!!.dismiss()
        }
    }

    override fun ondeleteTaskBtnClicked(toDoData: ToDoData) {
        databaseref.child(toDoData.taskId).removeValue().addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(context, "Deleted Successfully ", Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()

            }
        }

    }

    override fun onEditTaskBtnClicked(toDoData: ToDoData) {

        if (popUpfragment != null)
            childFragmentManager.beginTransaction().remove(popUpfragment!!).commit()

        popUpfragment = AddToDopopupFragment.newInstance(toDoData.taskId, toDoData.task)
        popUpfragment!!.setListener(this)
        popUpfragment!!.show(childFragmentManager, AddToDopopupFragment.TAG)

    }


}