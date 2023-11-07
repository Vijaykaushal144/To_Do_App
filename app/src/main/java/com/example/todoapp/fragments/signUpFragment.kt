package com.example.todoapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentSignUpBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth


class signUpFragment : Fragment() {

private lateinit var binding:FragmentSignUpBinding
private lateinit var auth:FirebaseAuth
private lateinit var navcontrol:NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentSignUpBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        registerEvents()
    }

    private fun init(view:View)
    {
        navcontrol=Navigation.findNavController(view)
        auth=FirebaseAuth.getInstance()

    }

    private fun registerEvents()
    {

        binding.txtsignin.setOnClickListener {
            navcontrol.navigate(R.id.action_signUpFragment_to_signInFragment)
        }
        binding.btnnext.setOnClickListener{
            val email=binding.txtemail.text.toString()
            val password=binding.txtpassword.text.toString()
            val verifypass=binding.repassword.text.toString()

            if(email.isNotEmpty() && password.isNotEmpty() && verifypass.isNotEmpty())
            {
                if(password==verifypass)
                {
                    binding.progressBar.visibility=View.VISIBLE

                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                        OnCompleteListener {
                            if(it.isSuccessful)
                            {
                                Toast.makeText(context,"Successfully registered",Toast.LENGTH_SHORT).show()
                            navcontrol.navigate(R.id.action_signUpFragment_to_homeFragment)

                            }else
                            {
                                Toast.makeText(context,it.exception?.message,Toast.LENGTH_SHORT).show()
                            }
                            binding.progressBar.visibility=View.GONE
                        })
                }
                else{
                    Toast.makeText(context,"Password Does Not Match",Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(context,"Empty Fields Are Not Allowed",Toast.LENGTH_SHORT).show()
            }

        }
    }


}