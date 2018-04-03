package com.addressbook.android.util

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.provider.Settings
import android.support.annotation.RequiresApi
import com.addressbook.android.R

/**
 * Created by Administrator on 3/15/18.
 */
object ConnectionDetector {


    fun isConnectingToInternet(context: Context): Boolean {

        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val networks = connectivityManager.allNetworks
            var networkInfo: NetworkInfo
            for (mNetwork in networks) {
                networkInfo = connectivityManager.getNetworkInfo(mNetwork)
                if (networkInfo.state == NetworkInfo.State.CONNECTED) {
                    return true
                }
            }
        } else {
            if (connectivityManager != null) {
                val info = connectivityManager.allNetworkInfo
                if (info != null) {
                    for (anInfo in info) {
                        if (anInfo.state == NetworkInfo.State.CONNECTED) {
                            return true
                        }
                    }
                }
            }
        }
        return false
    }

    fun internetCheck(context: Context, showDialog: Boolean): Boolean {
        if (isConnectingToInternet(context))
            return true
        if (showDialog)
            showAlertDialog(context, context.getString(R.string.msg_NO_INTERNET_TITLE), context.getString(R.string.msg_NO_INTERNET_MSG), false)
        return false
    }

    fun showAlertDialog(context: Context, pTitle: String, pMsg: String, status: Boolean?) {
        try {
            val builder = AlertDialog.Builder(context)

            builder.setTitle(pTitle)
            builder.setMessage(pMsg)
            builder.setCancelable(true)
            builder.setPositiveButton(context.getString(R.string.msg_goto_settings)
            ) { dialog, which ->
                dialog.dismiss()
                val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                context.startActivity(intent)
            }
            val alert = builder.create()
            alert.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}