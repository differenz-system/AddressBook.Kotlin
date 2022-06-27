package com.addressbook.android.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.inputmethod.InputMethodManager
import cc.cloudist.acplibrary.ACProgressConstant
import cc.cloudist.acplibrary.ACProgressFlower
import com.addressbook.android.login.LoginActivity
import com.facebook.login.LoginManager

object Globals {
    var ACPDialog: ACProgressFlower? = null

    fun showProgressDialog(context: Context) {
        ACPDialog = ACProgressFlower.Builder(context)
            .direction(ACProgressConstant.DIRECT_CLOCKWISE)
            .themeColor(Color.WHITE)
            .fadeColor(Color.DKGRAY).build()
        ACPDialog?.setCanceledOnTouchOutside(false)
        ACPDialog?.show()
    }

    fun dismissDialog() {
        if (ACPDialog != null && ACPDialog!!.isShowing) {
            ACPDialog?.dismiss()
        }
    }


    fun hideKeyboard(activity: Activity) {
        try {
            val inputManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun logoutProcess(context: Context) {
        // logout from facebook
        if (LoginManager.getInstance() != null)
            LoginManager.getInstance()?.logOut()
        val i_logout = Intent(context, LoginActivity::class.java)

        // set the new task and clear flags
        i_logout.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(i_logout)
    }

}


