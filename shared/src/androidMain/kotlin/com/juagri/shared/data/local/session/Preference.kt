package com.juagri.shared.data.local.session

const val SP_NAME = "JUAgriLocalPreference"

actual fun SessionContext.putInt(key: String, value: Int) {
    getSpEditor().putInt(key, value).apply()
}

actual fun SessionContext.getInt(key: String, default: Int): Int {
    return  getSp().getInt(key, default )
}

actual fun SessionContext.putString(key: String, value: String) {
    getSpEditor().putString(key, value).apply()
}

actual fun SessionContext.getString(key: String): String? {
    return  getSp().getString(key, null)
}

actual fun SessionContext.putBool(key: String, value: Boolean) {
    getSpEditor().putBoolean(key, value).apply()
}

actual fun SessionContext.getBool(key: String, default: Boolean): Boolean {
    return getSp().getBoolean(key, default)
}

private fun SessionContext.getSp() = getSharedPreferences(SP_NAME, 0)

private fun SessionContext.getSpEditor() = getSp().edit()