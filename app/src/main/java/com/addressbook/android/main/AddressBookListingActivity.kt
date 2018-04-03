package com.addressbook.android.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.AdapterView
import com.addressbook.android.R
import com.addressbook.android.greendao.db.AddressBook
import com.addressbook.android.greendao.db.DaoSession
import com.addressbook.android.util.Constant
import com.addressbook.android.util.Globals
import kotlinx.android.synthetic.main.activity_address_book_listing.*
import kotlinx.android.synthetic.main.layout_tool_bar.*
import java.io.Serializable
import java.util.*

class AddressBookListingActivity : AppCompatActivity(), AdapterView.OnItemClickListener {


    private var daoSession: DaoSession? = null

    private var addressbookList: ArrayList<AddressBook> = ArrayList()
    private var adapterAddressBookList: AdapterAddressBookList? = null
    internal var globals: Globals? = null
    var ADD_EDIT_ADDRESS_REQ_CODE = 1010

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address_book_listing)
        init()
    }

    private fun init() {
        globals = applicationContext as Globals
        daoSession = globals?.getInstance()?.getDaoSession()
        setSupportActionBar(toolbar)
        toolbar_title.setText(getString(R.string.lbl_address_book))
        toolbar_left.setVisibility(View.VISIBLE)
        toolbar_right.setVisibility(View.VISIBLE)
        toolbar_left.setText(getString(R.string.action_logout))
        toolbar_right.setText(getString(R.string.action_add))

        toolbar_right.setOnClickListener(clickListener)
        toolbar_left.setOnClickListener(clickListener)

        setUpList()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_EDIT_ADDRESS_REQ_CODE && resultCode == RESULT_OK) {
            setUpList()
        }
    }

    private fun setUpList() {

        val addressBookDao = daoSession?.addressBookDao
        addressbookList = addressBookDao?.loadAll() as ArrayList<AddressBook>

        setAdapter()
    }

    fun setAdapter() {
        if (!addressbookList.isEmpty()) {
            if (adapterAddressBookList == null) {
                adapterAddressBookList = AdapterAddressBookList(getContext())
                adapterAddressBookList!!.setOnItemClickListener(this)
            }
            adapterAddressBookList!!.doRefresh(addressbookList)

            if (rv_address_list.getAdapter() == null) {
                rv_address_list.setHasFixedSize(false)
                rv_address_list.setLayoutManager(LinearLayoutManager(getContext()))
                rv_address_list.setAdapter(adapterAddressBookList)
                rv_address_list.setNestedScrollingEnabled(false)
                rv_address_list.setFocusable(false)
            }
        }
        handleEmptyList()
    }

    override fun onItemClick(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
        editAddressBook(pos)
    }

    private fun editAddressBook(pos: Int) {

        val i_edit_remove_address_book = Intent(getContext(), EditRemoveAddressBookActivity::class.java)
        i_edit_remove_address_book.putExtra(Constant.Key_editAddressBook, addressbookList.get(pos) as Serializable)
        startActivityForResult(i_edit_remove_address_book, ADD_EDIT_ADDRESS_REQ_CODE)

    }

    private fun getContext(): Context {
        return this@AddressBookListingActivity
    }

    private fun getContextActivity(): Activity {
        return this@AddressBookListingActivity
    }

    fun handleEmptyList() {
        if (tv_no_list != null) {

            if (addressbookList.isEmpty()) {
                tv_no_list.visibility = View.VISIBLE
                rv_address_list.visibility = View.GONE
            } else {
                tv_no_list.visibility = View.GONE
                rv_address_list.visibility = View.VISIBLE
            }
        }
    }

    val clickListener = View.OnClickListener { view ->
        when (view.getId()) {
            R.id.toolbar_right -> {
                saveAddressBook()
            }

            R.id.toolbar_left -> {
                globals?.setUserDetails(null)
                globals?.logoutProcess(getContext())
            }
        }
    }

    private fun saveAddressBook() {
        val i_edit_remove_address_book = Intent(getContext(), EditRemoveAddressBookActivity::class.java)
        startActivityForResult(i_edit_remove_address_book, ADD_EDIT_ADDRESS_REQ_CODE)
    }
}
