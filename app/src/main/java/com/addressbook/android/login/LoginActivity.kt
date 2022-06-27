package com.addressbook.android.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import com.addressbook.android.R
import com.addressbook.android.api.ApiRequest
import com.addressbook.android.api.NetworkStatus
import com.addressbook.android.databinding.ActivityLoginBinding
import com.addressbook.android.login.viewModel.LoginViewModel
import com.addressbook.android.main.AddressBookListingActivity
import com.addressbook.android.model.LoginUserResponse
import com.addressbook.android.model.UserLoginDetail
import com.addressbook.android.util.*
import com.facebook.CallbackManager


class LoginActivity : BaseAppCompatActivity(), View.OnClickListener {

    //Facebook
    var callbackmanager: CallbackManager? = null
    lateinit var binding: ActivityLoginBinding
    lateinit var viewModel: LoginViewModel
    private val currentContext = this@LoginActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        setUpObserver()
    }

    private fun init() {
        binding.apply {
            setSupportActionBar(toolbar.toolbar)
            toolbar.toolbarTitle.text = getString(R.string.title_login)
            btnLogin.setOnClickListener(currentContext)
            btnLoginFb.setOnClickListener(currentContext)
        }
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
    }

    private fun doRequestForLogin() {
        val body = ApiRequest.LoginBody(binding.etEmail.text.toString().trim(), binding.etPassword.text.toString().trim())
        viewModel.loginUser(body)
    }

    private fun setUpObserver() {
        viewModel.getLoginData().observe(this) {
            when (it) {
                is NetworkStatus.Failed -> {
                    toast(it.msg)
                    Globals.showProgressDialog(this)
                }
                is NetworkStatus.NoInternet -> {
                    Globals.dismissDialog()
                }
                is NetworkStatus.Running -> {
                    Globals.showProgressDialog(this)
                }
                is NetworkStatus.Success -> {
                    Globals.dismissDialog()
                    val loginData = it.data as LoginUserResponse.Data
                    val userLoginDetail = UserLoginDetail(loginData.password, loginData.email)
                    SharedPrefsHelper.setUserDetails(userLoginDetail)
                    intentAddressBookListing()
                }
            }
        }
    }

    override fun onClick(view: View?) {
        currentContext.apply {
            Globals.hideKeyboard(this)
            when (view?.id) {
                R.id.btn_login -> {
                    if (isValid()) {
                        if (ConnectionDetector.internetCheck(this, true))
                            doRequestForLogin()
                    }
                }
                R.id.btn_login_fb -> {
                    if (ConnectionDetector.internetCheck(this, true))
                        fBLogin()
                }
            }
        }
    }

    private fun fBLogin() {
        viewModel.fBLogin(this).observe(this, androidx.lifecycle.Observer {
            if (it.accessToken != null) {
                intentAddressBookListing()
            }
        })
    }

    private fun intentAddressBookListing() {
        openActivity(AddressBookListingActivity::class.java)
        finish()
    }

    private fun isValid(): Boolean {
        if (UtilsValidation.validateEmptyEditText(binding.etEmail)) {
            toast(getString(R.string.toast_err_email))
            requestFocus(binding.etEmail)
            return false
        }
        if (UtilsValidation.validateEmail(binding.etEmail)) {
            toast(getString(R.string.toast_err_enter_valid_email))
            requestFocus(binding.etEmail)
            return false
        }
        if (UtilsValidation.validateEmptyEditText(binding.etPassword)) {
            toast(getString(R.string.toast_err_password))
            requestFocus(binding.etPassword)
            return false
        }
        return true
    }

    private fun requestFocus(view: View) {
        if (view.requestFocus()) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackmanager?.onActivityResult(requestCode, resultCode, data)
    }
}
