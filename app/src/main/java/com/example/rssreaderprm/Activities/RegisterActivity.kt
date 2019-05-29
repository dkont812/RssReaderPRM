package com.example.rssreaderprm.Activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.rssreaderprm.R
import com.example.rssreaderprm.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_register_email.*
import kotlinx.android.synthetic.main.fragment_register_name_pass.*

class RegisterActivity : AppCompatActivity(), EmailFragment.Listener,
    NamePassFragment.Listener {
    private val TAG = "RegisterActivity"
    private var mEmail: String? = null
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabase: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().add(
                R.id.frame_layout,
                EmailFragment()
            ).commit()
        }

    }

    override fun onNext(email: String) {
        if (email.isNotEmpty()) {
            mEmail = email
            mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener {
                if(it.isSuccessful){
                    if(it.result!!.signInMethods?.isEmpty()!=false){
                        supportFragmentManager.beginTransaction().replace(
                            R.id.frame_layout,
                            NamePassFragment()
                        )
                            .addToBackStack(null)
                            .commit()
                    } else {
                        Toast.makeText(this, "email already exist", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, it.exception!!.message, Toast.LENGTH_SHORT).show()
                }
            }

        } else {
            Toast.makeText(this, "Email empty", Toast.LENGTH_LONG).show()
        }

    }


    override fun onRegister(fullName: String, password: String) {
        if (fullName.isNotEmpty() && password.isNotEmpty()) {
            val email = mEmail
            if (email != null) {
                mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val username = mkUsername(fullName)
                            val user = User(name = fullName, username = username, email = email)
                            mDatabase.child("users").child(it.result!!.user.uid).setValue(user).addOnCompleteListener {
                                if (it.isSuccessful) {
                                    startActivity(Intent(this, MainActivity::class.java))
                                    finish()

                                } else {
                                    Log.e(TAG, "onRegister: failed to create profile", it.exception)
                                }
                            }
                        } else {
                            Log.e(TAG, "onRegister: failed to create profile", it.exception)
                        }
                    }
            } else {
                Log.e(TAG, "onRegister:  email is null")
                Toast.makeText(this, "Please enter email!", Toast.LENGTH_SHORT)
                supportFragmentManager.popBackStack()
            }


        } else {
            Toast.makeText(this, "Please provide full name and password", Toast.LENGTH_SHORT)

        }
    }

    private fun mkUsername(fullName: String) =
        fullName.toLowerCase().replace(" ", ".")


}

class EmailFragment : Fragment() {
    private lateinit var mListener: Listener

    interface Listener {
        fun onNext(email: String)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register_email, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        next_button.setOnClickListener {
            val email = new_email.text.toString()
            mListener.onNext(email)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mListener = context as Listener

    }
}

class NamePassFragment : Fragment() {
    private lateinit var mListener: Listener

    interface Listener {
        fun onRegister(fullName: String, password: String)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register_name_pass, container, false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        register_button.setOnClickListener {
            val fullName = new_name.text.toString()
            val pass = new_pass.text.toString()
            mListener.onRegister(fullName, pass)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mListener = context as Listener

    }
}

