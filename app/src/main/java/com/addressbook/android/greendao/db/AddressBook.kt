package com.addressbook.android.greendao.db

import org.greenrobot.greendao.annotation.Generated
import org.greenrobot.greendao.annotation.Id
import java.io.Serializable

/**
 * Created by Administrator on 3/14/18.
 */
class AddressBook : Serializable {

    @Id(autoincrement = true)
    var id: Long? = null
    var name: String? = null
    var email: String? = null
    var contact_number: String? = null
    var isactive: Boolean? = null

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    @Generated(hash = 44583721)
    constructor() {
    }

    constructor(id: Long?) {
        this.id = id
    }

    @Generated(hash = 1483669639)
    constructor(id: Long?, name: String?, email: String?, contact_number: String?, isactive: Boolean?) {
        this.id = id
        this.name = name
        this.email = email
        this.contact_number = contact_number
        this.isactive = isactive
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}