package com.example.rssreaderprm.Activities

import android.app.ProgressDialog
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.example.rssreaderprm.Adapter.FeedAdapter
import com.example.rssreaderprm.Common.HTTPDataHanlder
import com.example.rssreaderprm.R
import com.example.rssreaderprm.RssModel.RSSObject
import com.example.rssreaderprm.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_home_page.*

class MainActivity : BaseActivity(0) {
    private val TAG = "LogInActivity"
    private lateinit var mAuth: FirebaseAuth
    private val RSS_link = "https://www.tvn24.pl/najnowsze.xml"
    private val RSS_to_JSON_API = " https://api.rss2json.com/v1/api.json?rss_url="


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        setupBottomNavigation()
        Log.d(TAG, "onCreate: ")
        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser
        val database = FirebaseDatabase.getInstance().reference
        database.child("users").child(user!!.uid).addListenerForSingleValueEvent(
            ValueEventListenerAdapter {

                val user = it.getValue(User::class.java)
                username.setText(user!!.username, TextView.BufferType.NORMAL)

            })


        mAuth.addAuthStateListener {
            if (it.currentUser == null) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }

        val linearLayoutManager = LinearLayoutManager(baseContext, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = linearLayoutManager
        loadRSS()

    }


    private fun loadRSS() {
        val loadRSSAsync = object : AsyncTask<String, String, String>() {
            var mDialog = ProgressDialog(this@MainActivity)
            override fun onPostExecute(result: String?) {
                mDialog.dismiss()
                var rssObject: RSSObject
                rssObject = Gson().fromJson<RSSObject>(result, RSSObject::class.java!!)
                val adapter = FeedAdapter(rssObject, baseContext)
                recyclerView.adapter = adapter
                adapter.notifyDataSetChanged()

            }

            override fun onPreExecute() {
                mDialog.setMessage("please wait..")
                mDialog.show()
            }


            override fun doInBackground(vararg params: String?): String {
                var result: String
                var http = HTTPDataHanlder()
                result = http.GetHTTPDataHanlder(params[0])
                return result

            }
        }

        val url_get_data = StringBuilder(RSS_to_JSON_API)
        url_get_data.append(RSS_link)
        loadRSSAsync.execute(url_get_data.toString())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_refresh)
            loadRSS()
        return true
    }

    override fun onStart() {
        super.onStart()
        if (mAuth.currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
