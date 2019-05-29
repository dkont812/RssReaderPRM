package com.example.rssreaderprm.Activities

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import com.example.rssreaderprm.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_news.*
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDate
import java.time.ZoneOffset.UTC
import java.util.Locale.US

class NewsActivity : AppCompatActivity() {
    private lateinit var mDatabase : DatabaseReference
    private lateinit var mAuth : FirebaseAuth
    private lateinit var mStorage: StorageReference


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        val title: String = intent.getStringExtra("title")
        val desc: String = intent.getStringExtra("desc")
        val link: String = intent.getStringExtra("link")
        val img: String = intent.getStringExtra("img")
        val pubDate: String = intent.getStringExtra("pubDate")
        var mStorage : StorageReference = FirebaseStorage.getInstance().reference




        title_new.setText(img)
        description.setText(desc)
        Picasso
            .get()
            .load(img)
            .fit()
            .into(new_img)
        pubDate_new.setText(pubDate)


        webLink.tooltipText = "Go to Source"
        webLink.setOnClickListener {
            var webBrowser = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            startActivity(webBrowser)
            finish()
        }
        addToFavorite.setOnLongClickListener { addTofav(title) }


    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addTofav(link : String) : Boolean{

        mAuth = FirebaseAuth.getInstance()
        mStorage = FirebaseStorage.getInstance().reference
        mDatabase = FirebaseDatabase.getInstance().reference
        val uid = mAuth.currentUser!!.uid
//        mStorage.child("users/$uid/news").putFile(Uri.parse(link)).addOnCompleteListener{
//            if(it.isSuccessful){
                mDatabase.child("users/$uid/news/new" + LocalDate.now().toString()).push().setValue(link)
//            } else {
//                Toast.makeText(this,it.exception!!.message!!,Toast.LENGTH_LONG).show()
//            }
//        }
        return true

    }
}
