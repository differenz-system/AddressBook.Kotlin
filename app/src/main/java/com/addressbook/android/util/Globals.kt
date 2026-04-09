package com.addressbook.android.util

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.view.inputmethod.InputMethodManager
import com.addressbook.android.login.LoginActivity
import com.facebook.login.LoginManager

object Globals {
    //var ACPDialog: ACProgressFlower? = null
    var dialog: ProgressDialog? = null

    fun showProgressDialog(context: Context) {
        dialog = ProgressDialog(context)
        dialog?.setMessage("Please wait...")
        dialog?.show()
    }

    fun dismissDialog() {
        if (dialog != null && dialog!!.isShowing) {
            dialog?.dismiss()
        }
    }

    fun hideKeyboard(activity: Activity) {
        try {
            val inputManager =
                activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(
                activity.currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun logoutProcess(context: Context) {
        // logout from facebook
        if (LoginManager.getInstance() != null) LoginManager.getInstance()?.logOut()
        val i_logout = Intent(context, LoginActivity::class.java)

        // set the new task and clear flags
        i_logout.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(i_logout)
    }

}


