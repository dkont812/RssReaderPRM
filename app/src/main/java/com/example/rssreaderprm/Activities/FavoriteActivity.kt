package com.example.rssreaderprm.Activities

import android.os.Bundle
import android.util.Log
import com.example.rssreaderprm.R

class FavoriteActivity : BaseActivity(1) {
    private val TAG = "FavoriteActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_home_page)
        setupBottomNavigation()
        Log.d(TAG, "onCreate: FavoriteActivity")
    }
}
