package com.juagri.shared.di

import Constants
import app.cash.sqldelight.db.SqlDriver
import com.juagri.shared.JUDatabase
import com.juagri.shared.data.remote.login.LoginRepositoryImpl
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import com.juagri.shared.domain.LoginUseCase
import com.juagri.shared.domain.usecase.LoginRepository
import org.koin.core.context.startKoin
import org.koin.dsl.module
import com.juagri.shared.data.local.session.SessionPreference
import com.juagri.shared.data.local.session.datamanager.DataManager
import com.juagri.shared.data.local.session.datamanager.DataStore
import com.juagri.shared.ui.home.HomeViewModel
import com.juagri.shared.ui.login.LoginViewModel

fun initKoin(sessionPreference: SessionPreference,sqlDriver: SqlDriver) {
    println("1231312312313 - 1")
    startKoin {
        modules(
            module {
                println("1231312312313 - 2")
                single { JUDatabase(sqlDriver) }
                single { sessionPreference }
                single<LoginRepository> { LoginRepositoryImpl(Firebase.firestore.collection(Constants.TABLE_EMP_ACCESS)) }
                single { LoginUseCase(get()) }
                single { DataManager(DataStore()) }
                factory { LoginViewModel(get(),get(),get()) }
                factory { HomeViewModel(get()) }
                println("1231312312313 - 3")
            }
        )
    }
}