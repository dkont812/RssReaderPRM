package com.example.rssreaderprm.Activities

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.example.rssreaderprm.R
import com.example.rssreaderprm.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_edit_profile.*

class EditProfileActivity : BaseActivity(2) {
    private val TAG = "EditProfileActivity"
    private lateinit var mUser: User
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabase: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        Log.d(TAG, "onCreate: ")
        setupBottomNavigation()



        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference
        mDatabase.child("users").child(mAuth.currentUser!!.uid)
            .addListenerForSingleValueEvent(ValueEventListenerAdapter {
                mUser = it.getValue(User::class.java)!!
                name_input.setText(mUser.name, TextView.BufferType.EDITABLE)
                username_input.setText(mUser.username, TextView.BufferType.EDITABLE)
                email_input.setText(mUser.email, TextView.BufferType.NORMAL)
            })
        textSignOut.setOnClickListener {
            mAuth.signOut()

        }
        save_image.setOnClickListener { updateProfile() }

    }

    private fun updateProfile() {
        val user = User(
            name = name_input.text.toString(),
            username = username_input.text.toString(),
            email = email_input.text.toString()
        )
        val error = validate(user)
        if (error == null) {
            if (user.email == mUser.email) {
                val updatesMap = mutableMapOf<String, Any>()
                if (user.name != mUser.name) updatesMap["name"] = user.name
                if (user.username != mUser.name) updatesMap["username"] = user.username
                mDatabase.child("users").child(mAuth.currentUser!!.uid).updateChildren(updatesMap)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(this, "profile saved", Toast.LENGTH_SHORT)
                            finish()
                        } else {
                            Toast.makeText(this, it.exception!!.message!!, Toast.LENGTH_SHORT)

                        }
                    }
            }
        } else {
            Toast.makeText(this, "error", Toast.LENGTH_SHORT)

        }
    }

    private fun validate(user: User): String? =
        when {
            user.name.isEmpty() -> "Please enter name"
            user.username.isEmpty() -> "Please enter name"
            else -> null
        }


}
