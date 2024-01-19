package com.juagri.shared.data.local.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.juagri.shared.JUDatabase


actual class DriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(JUDatabase.Schema, "JUDatabase.db")
    }
}
