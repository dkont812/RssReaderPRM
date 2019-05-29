package com.example.rssreaderprm.Activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import com.example.rssreaderprm.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_log_in.*
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener

class LoginActivity : AppCompatActivity(), KeyboardVisibilityEventListener, TextWatcher, View.OnClickListener {
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
        loginButton.isEnabled = false
        emailInput.addTextChangedListener(this)
        passwordInput.addTextChangedListener(this)
        KeyboardVisibilityEvent.setEventListener(this, this)
        loginButton.setOnClickListener(this)

        mAuth = FirebaseAuth.getInstance()
        create_account_text.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.loginButton -> {
                val email = emailInput.text.toString()
                val pass = passwordInput.text.toString()
                if (validate(email, pass)) {
                    mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()

                        }
                    }

                } else {
                    Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT)
                }

            }
            R.id.create_account_text -> {
                startActivity(Intent(this, RegisterActivity::class.java))
            }
        }

    }

    override fun onVisibilityChanged(isKeyboardOpen: Boolean) {
        if (isKeyboardOpen) {
            scroll_view.scrollTo(0, scroll_view.bottom)
            create_account_text.visibility = View.GONE
        } else {
            scroll_view.scrollTo(0, scroll_view.top)
            create_account_text.visibility = View.VISIBLE
        }
    }

    override fun afterTextChanged(s: Editable?) {
        loginButton.isEnabled = validate(emailInput.text.toString(), passwordInput.text.toString())

    }

    private fun validate(email: String, pass: String) =
        email.isNotEmpty() &&
                pass.isNotEmpty()

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
}
