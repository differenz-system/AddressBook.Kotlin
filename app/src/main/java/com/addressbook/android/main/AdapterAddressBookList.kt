package com.addressbook.android.main

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import com.addressbook.android.R
import com.addressbook.android.greendao.db.AddressBook
import java.util.*

/**
 * Created by Administrator on 3/15/18.
 */
class AdapterAddressBookList(private val context: Context) : RecyclerView.Adapter<AdapterAddressBookList.ViewHolder>() {
    override fun getItemCount() = mValues?.size ?: 0

    private var mValues: ArrayList<AddressBook>? = null
    private var onItemClickListener: AdapterView.OnItemClickListener? = null


    fun doRefresh(addressbookList: ArrayList<AddressBook>) {
        this.mValues = addressbookList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_addressbook, parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            onItemHolderClick(this)
        }

        /**
         * @param addressBook   the data of particular row in list
         */
        fun bindAddressBook(addressBook: AddressBook) {
            with(addressBook) {
                val tv_name = itemView.findViewById(R.id.tv_name) as TextView
                val tv_email = itemView.findViewById(R.id.tv_email) as TextView
                val tv_contact_number = itemView.findViewById(R.id.tv_contact_number) as TextView

                tv_name.setText(name)
                tv_email.setText(email)
                tv_contact_number.setText(contact_number)
            }

        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val addressBookItemModel = mValues!![position]
        holder.bindAddressBook(addressBookItemModel)
    }

    fun setOnItemClickListener(onItemClickListener: AdapterView.OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    private fun onItemHolderClick(holder: ViewHolder) {
        if (onItemClickListener != null)
            onItemClickListener!!.onItemClick(null, holder.itemView, holder.adapterPosition, holder.itemId)
    }

}
