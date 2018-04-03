package com.addressbook.android.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.addressbook.android.R
import com.addressbook.android.main.AddressBookListingActivity
import com.addressbook.android.util.*
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.layout_tool_bar.*
import java.util.*


class LoginActivity : BaseAppCompatActivity() {

    var globals: Globals? = null

    //Facebook
    var callbackmanager: CallbackManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        init()
    }

    private fun init() {
        globals = applicationContext as Globals
        setSupportActionBar(toolbar)
        toolbar_title.setText(getString(R.string.title_login))

        btn_login.setOnClickListener(clickListener)
        btn_login_fb.setOnClickListener(clickListener)

    }

    val clickListener = View.OnClickListener { view ->

        when (view.getId()) {

            R.id.btn_login -> {
                globals?.hideKeyboard(getContextActivity())
                if (isValid()) {
                    if (ConnectionDetector.internetCheck(getContext(), true))
                        doRequestForLoginUser()
                }
            }
            R.id.btn_login_fb -> {
                globals?.hideKeyboard(getContextActivity())
                if (ConnectionDetector.internetCheck(getContext(), true))
                    FBLogin()
            }
        }
    }

    fun FBLogin() {
        callbackmanager = CallbackManager.Factory.create()
        // Set permissions
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"))
        LoginManager.getInstance().registerCallback(callbackmanager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                intentAddressBookListing()
            }

            override fun onCancel() {
                Log.d("cancel", "On cancel")
            }

            override fun onError(error: FacebookException) {
                Log.d("error", error.toString())
            }
        })
    }

    fun doRequestForLoginUser() {
        var body = API.LoginBody(Email = globals?.trimString(et_email)!!,
                Password = globals?.trimString(et_password)!!)

        Log.e("Login body : ", body.toString())

        globals?.showProgressDialog(getContext())
        API.getInstance().login(body)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError({ e -> Toast.makeText(getContext(), e.message, Toast.LENGTH_SHORT).show() })
                .subscribe({ response ->
                    globals?.dismissDialog()
                    globals?.setUserDetails(response)
                    intentAddressBookListing()

                }, { error ->
                    globals?.dismissDialog()
                })
    }

    private fun intentAddressBookListing() {
        val i_address_book_listing = Intent(this@LoginActivity, AddressBookListingActivity::class.java)
        startActivity(i_address_book_listing)
        finish()
    }

    fun isValid(): Boolean {
        if (UtilsValidation.validateEmptyEditText(et_email)) {
            globals?.showToast(this@LoginActivity, getString(R.string.toast_err_email))
            requestFocus(et_email)
            return false
        }
        if (UtilsValidation.validateEmail(et_email)) {
            globals?.showToast(this@LoginActivity, getString(R.string.toast_err_enter_valid_email))
            requestFocus(et_email)
            return false
        }
        if (UtilsValidation.validateEmptyEditText(et_password)) {
            globals?.showToast(this@LoginActivity, getString(R.string.toast_err_password))
            requestFocus(et_password)
            return false
        }
        return true
    }

    private fun requestFocus(view: View) {
        if (view.requestFocus()) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        }
    }

    private fun getContext(): Context {
        return this@LoginActivity
    }

    private fun getContextActivity(): Activity {
        return this@LoginActivity
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackmanager?.onActivityResult(requestCode, resultCode, data)
    }
}
