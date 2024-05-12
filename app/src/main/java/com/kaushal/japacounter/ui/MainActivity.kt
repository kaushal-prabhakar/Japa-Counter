package com.kaushal.japacounter.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.kaushal.japacounter.R
import com.kaushal.japacounter.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(savedInstanceState == null) {
            val welcomeFragment: WelcomeFragment = WelcomeFragment.newInstance()
            supportFragmentManager.beginTransaction().add(R.id.container, welcomeFragment).commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
       // Log.i(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
       // Log.i(TAG, "onResume")
    }
}