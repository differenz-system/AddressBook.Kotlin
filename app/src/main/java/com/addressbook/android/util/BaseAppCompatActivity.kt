package com.addressbook.android.util

import androidx.appcompat.app.AppCompatActivity
import com.addressbook.android.R

open class BaseAppCompatActivity:AppCompatActivity() {

    override fun onBackPressed() {
        super.onBackPressed()
        Globals.hideKeyboard(this)
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out)
    }
}