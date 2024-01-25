package com.juagri.shared.data.local.session

import com.juagri.shared.utils.value

class SessionPreference(private val context: SessionContext) {

    fun put(key: String, value: Int) {
        context.putInt(key, value)
    }

    fun put(key: String, value: String) {
        context.putString(key, value)
    }

    fun put(key: String, value: Boolean) {
        context.putBool(key, value)
    }

    fun getInt(key: String, default: Int): Int = context.getInt(key, default)


    fun getString(key: String): String? = context.getString(key)


    fun getBool(key: String, default: Boolean): Boolean =
        context.getBool(key, default)

    fun setAlreadyLoggedIn(alreadyLogin: Boolean) {
        context.putBool(IS_ALREADY_LOGGED_IN, alreadyLogin)
    }

    fun isAlreadyLoggedIn() = context.getBool(IS_ALREADY_LOGGED_IN, false)

    fun setEmpCode(empCode: String){
        context.putString(EMP_CODE,empCode)
    }

    fun empCode(): String = context.getString(EMP_CODE).value()

    fun setEmpName(empName: String){
        context.putString(EMP_NAME,empName)
    }

    fun empName(): String = context.getString(EMP_NAME).value()

    fun setEmpMobile(empName: String){
        context.putString(EMP_MOBILE,empName)
    }

    fun empMobile(): String = context.getString(EMP_MOBILE).value()

    fun setEmpRoleId(empRoleID: String){
        context.putString(EMP_ROLE_ID,empRoleID)
    }

    fun empRoleId(): String = context.getString(EMP_ROLE_ID).value()

    fun setFirestorePersistence(done: Boolean) {
        context.putBool(IS_FIRESTORE_PERSISTENCE_DONE, done)
    }

    fun isFirestorePersistenceNotDone() = context.getBool(IS_FIRESTORE_PERSISTENCE_DONE, false)

    private companion object {
        const val IS_FIRESTORE_PERSISTENCE_DONE = "isFirestorePersistenceDone"
        const val IS_ALREADY_LOGGED_IN = "isAlreadyLoggedIn"
        const val EMP_CODE = "emp_code"
        const val EMP_NAME = "emp_name"
        const val EMP_MOBILE = "emp_mobile"
        const val EMP_ROLE_ID = "emp_role_id"
    }

}