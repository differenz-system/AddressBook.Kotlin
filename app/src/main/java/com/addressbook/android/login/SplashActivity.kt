package com.addressbook.android.login

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.addressbook.android.R
import com.addressbook.android.databinding.ActivityLoginBinding
import com.addressbook.android.databinding.ActivitySplashBinding
import com.addressbook.android.main.AddressBookListingActivity
import com.addressbook.android.util.*
import java.util.*

class SplashActivity : BaseAppCompatActivity() {
    lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

         Handler(Looper.myLooper()!!).postDelayed({
             openNavigationActivity()
         }, 2000)
    }

    private fun openNavigationActivity() {
        openActivity(if (SharedPrefsHelper.getUserDetails() == null) LoginActivity::class.java else AddressBookListingActivity::class.java)
        finish()
    }
}
