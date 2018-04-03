package com.addressbook.android.greendao.db

import org.greenrobot.greendao.AbstractDao
import org.greenrobot.greendao.AbstractDaoSession
import org.greenrobot.greendao.database.Database
import org.greenrobot.greendao.identityscope.IdentityScopeType
import org.greenrobot.greendao.internal.DaoConfig

/**
 * Created by Administrator on 3/14/18.
 */
class DaoSession(db: Database, type: IdentityScopeType, daoConfigMap: Map<Class<out AbstractDao<*, *>>, DaoConfig>) : AbstractDaoSession(db) {

    private val addressBookDaoConfig: DaoConfig

    val addressBookDao: AddressBookDao

    init {

        addressBookDaoConfig = daoConfigMap[AddressBookDao::class.java]!!.clone()
        addressBookDaoConfig.initIdentityScope(type)

        addressBookDao = AddressBookDao(addressBookDaoConfig, this)

        registerDao(AddressBook::class.java, addressBookDao)
    }

    fun clear() {
        addressBookDaoConfig.clearIdentityScope()
    }

}