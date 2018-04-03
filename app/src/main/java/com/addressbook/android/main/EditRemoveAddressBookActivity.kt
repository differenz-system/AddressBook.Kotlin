package com.addressbook.android.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import com.addressbook.android.R
import com.addressbook.android.greendao.db.AddressBook
import com.addressbook.android.greendao.db.DaoSession
import com.addressbook.android.util.Constant
import com.addressbook.android.util.Globals
import com.addressbook.android.util.UtilsValidation
import kotlinx.android.synthetic.main.activity_edit_remove_address_book.*
import kotlinx.android.synthetic.main.layout_tool_bar.*

class EditRemoveAddressBookActivity : AppCompatActivity() {

    internal var globals: Globals? = null

    private var daoSession: DaoSession? = null
    private var isUpdate = false
    private var extra: Bundle? = null
    private var mAddressBook: AddressBook? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_remove_address_book)
        init()
    }

    private fun init() {
        globals = applicationContext as Globals
        daoSession = globals?.getInstance()?.getDaoSession()
        setSupportActionBar(toolbar)
        toolbar_title.setText(getString(R.string.lbl_detail))
        toolbar_left.setVisibility(View.VISIBLE)
        toolbar_right.setVisibility(View.VISIBLE)
        toolbar_left.setText(getString(R.string.lbl_address_book))
        toolbar_right.setText(getString(R.string.action_logout))

        extra = intent.extras
        if (extra != null && extra!!.containsKey(Constant.Key_editAddressBook)) {
            isUpdate = true
            mAddressBook = extra!!.getSerializable(Constant.Key_editAddressBook) as AddressBook
        }

        // set the text in button based on adding or editing addressbook
        if (isUpdate) {
            // set the value from intent bundle
            edt_name.setText(mAddressBook?.name)
            edt_email.setText(mAddressBook?.email)
            edt_contact_no.setText(mAddressBook?.contact_number)
            mAddressBook?.isactive?.let { switch_active.setChecked(it) }

            btn_save_update.setText(getString(R.string.action_update))
            btn_delete_cancel.setText(getString(R.string.action_delete))
        } else {
            btn_save_update.setText(getString(R.string.action_save))
            btn_delete_cancel.setText(getString(R.string.action_cancel))
        }

        toolbar_left.setOnClickListener(clickListener)
        toolbar_right.setOnClickListener(clickListener)
        btn_save_update.setOnClickListener(clickListener)
        btn_delete_cancel.setOnClickListener(clickListener)
    }

    val clickListener = View.OnClickListener { view ->

        when (view.getId()) {

            R.id.toolbar_left -> {
                globals?.hideKeyboard(getContextActivity())
                onBackPressed()
            }

            R.id.toolbar_right -> {
                globals?.hideKeyboard(getContextActivity())
                globals?.setUserDetails(null)
                globals?.logoutProcess(getContext())
            }

            R.id.btn_save_update -> {
                globals?.hideKeyboard(getContextActivity())
                if (isUpdate) {
                    updateAddressBook()
                } else {
                    insertAddressBook()
                }
            }

            R.id.btn_delete_cancel -> {
                globals?.hideKeyboard(getContextActivity())
                if (isUpdate) {
                    deleteAddressBook()
                } else {
                    onBackPressed()
                }
            }

        }

    }

    private fun insertAddressBook() {

        if (isValid()) {
            val addressBook = AddressBook()
            addressBook.name = edt_name.text.toString().trim()
            addressBook.email = edt_email.text.toString().trim()
            addressBook.contact_number = edt_contact_no.text.toString().trim()
            addressBook.isactive = switch_active.isChecked

            daoSession?.insert(addressBook)
            val resultIntent = Intent()
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }


    private fun updateAddressBook() {
        if (isValid()) {
            val addressBook = AddressBook()
            addressBook.name = edt_name.text.toString().trim()
            addressBook.email = edt_email.text.toString().trim()
            addressBook.contact_number = edt_contact_no.text.toString().trim()
            addressBook.isactive = switch_active.isChecked
            addressBook.id = mAddressBook?.id

            daoSession?.update(addressBook)
            val resultIntent = Intent()
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }

    private fun deleteAddressBook() {
        globals?.showDialog(this, object : Globals.OnDialogClickListener {
            override fun OnDialogPositiveClick(position: Int) {
                daoSession?.delete(mAddressBook)
                val resultIntent = Intent()
                setResult(RESULT_OK, resultIntent)
                finish()
            }

            override fun OnDialogNegativeClick() {

            }
        }, getString(R.string.action_delete), getString(R.string.delete_confirmation_msg), getString(R.string.action_yes), getString(R.string.action_no), false, 0)

    }


    fun isValid(): Boolean {
        if (UtilsValidation.validateEmptyEditText(edt_name)) {
            globals?.showToast(this@EditRemoveAddressBookActivity, getString(R.string.toast_err_name))
            requestFocus(edt_name)
            return false
        }
        if (UtilsValidation.validateEmptyEditText(edt_email)) {
            globals?.showToast(this@EditRemoveAddressBookActivity, getString(R.string.toast_err_email))
            requestFocus(edt_email)
            return false
        }
        if (UtilsValidation.validateEmail(edt_email)) {
            globals?.showToast(this@EditRemoveAddressBookActivity, getString(R.string.toast_err_enter_valid_email))
            requestFocus(edt_email)
            return false
        }
        if (UtilsValidation.validateEmptyEditText(edt_contact_no)) {
            globals?.showToast(this@EditRemoveAddressBookActivity, getString(R.string.toast_err_contact_no))
            requestFocus(edt_contact_no)
            return false
        }
        if (UtilsValidation.validatePhoneNumber(edt_contact_no)) {
            globals?.showToast(this@EditRemoveAddressBookActivity, getString(R.string.toast_err_enter_valid_contact_number))
            requestFocus(edt_contact_no)
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
        return this@EditRemoveAddressBookActivity
    }

    private fun getContextActivity(): Activity {
        return this@EditRemoveAddressBookActivity
    }
}
