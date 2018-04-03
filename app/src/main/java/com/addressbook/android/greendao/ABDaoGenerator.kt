package com.addressbook.android.greendao

import org.greenrobot.greendao.generator.DaoGenerator
import org.greenrobot.greendao.generator.Entity
import org.greenrobot.greendao.generator.Schema

/**
 * Created by Administrator on 3/14/18.
 */
public class ABDaoGenerator {


    fun main(args: Array<String>) {
        val schema = Schema(1, "com.addressbook.android.greendao.db") // Your app package name and the (.db) is the folder where the DAO files will be generated into.
        schema.enableKeepSectionsByDefault()

        addTables(schema)

        try {
            DaoGenerator().generateAll(schema, "./app/src/main/java")
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun addTables(schema: Schema) {
        addAddressBookEntities(schema)
    }

    // This is use to describe the colums of your table
    private fun addAddressBookEntities(schema: Schema): Entity {
        val addressbook = schema.addEntity("AddressBook")
        addressbook.addIdProperty().primaryKey().autoincrement()
        addressbook.addStringProperty("name")
        addressbook.addStringProperty("email")
        addressbook.addStringProperty("contact_number")
        addressbook.addBooleanProperty("isactive")
        return addressbook
    }
}