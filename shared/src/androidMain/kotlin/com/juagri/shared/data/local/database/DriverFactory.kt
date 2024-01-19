package com.juagri.shared.data.local.database

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.juagri.shared.JUDatabase


actual class DriverFactory(private val context: Context){
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            JUDatabase.Schema, context, "JUDatabase.db"
        )
    }
}
