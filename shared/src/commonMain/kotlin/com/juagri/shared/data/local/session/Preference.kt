package com.juagri.shared.data.local.session

expect fun SessionContext.putInt(key: String, value: Int)

expect fun SessionContext.getInt(key: String, default: Int): Int

expect fun SessionContext.putString(key: String, value: String)

expect fun SessionContext.getString(key: String) : String?

expect fun SessionContext.putBool(key: String, value: Boolean)

expect fun SessionContext.getBool(key: String, default: Boolean): Boolean

expect fun SessionContext.clearAll()