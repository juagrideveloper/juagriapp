package com.juagri.shared.di

import Constants
import app.cash.sqldelight.db.SqlDriver
import com.juagri.shared.JUDatabase
import com.juagri.shared.data.local.dao.common.UserDetailsDao
import com.juagri.shared.data.local.dao.dealer.DealerDashboardDao
import com.juagri.shared.data.local.dao.dealer.DealerLedgerDao
import com.juagri.shared.ui.TestScreenViewModel
import com.juagri.shared.data.remote.login.OTPRepositoryImpl
import com.juagri.shared.domain.repo.login.EmployeeRepository
import com.juagri.shared.domain.repo.login.OTPRepository
import com.juagri.shared.domain.usecase.EmployeeUseCase
import com.juagri.shared.ui.dashboard.dealer.DealerDashboardViewModel
import com.juagri.shared.data.local.session.SessionPreference
import com.juagri.shared.data.local.session.datamanager.DataManager
import com.juagri.shared.data.local.session.datamanager.DataStore
import com.juagri.shared.data.remote.dashboard.DealerDashboardRepositoryImpl
import com.juagri.shared.data.remote.ledger.DealerLedgerRepositoryImpl
import com.juagri.shared.data.remote.login.EmployeeRepositoryImpl
import com.juagri.shared.data.remote.user.UserRepositoryImpl
import com.juagri.shared.domain.repo.dashboard.DealerDashboardRepository
import com.juagri.shared.domain.repo.ledger.DealerLedgerRepository
import com.juagri.shared.domain.repo.user.UserRepository
import com.juagri.shared.domain.usecase.DealerDashboardUseCase
import com.juagri.shared.domain.usecase.DealerLedgerUseCase
import com.juagri.shared.domain.usecase.OTPUseCase
import com.juagri.shared.domain.usecase.UserDetailsUseCase
import com.juagri.shared.ui.home.HomeViewModel
import com.juagri.shared.ui.ledger.LedgerViewModel
import com.juagri.shared.ui.login.LoginViewModel
import com.juagri.shared.ui.splash.SplashViewModel
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.accept
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.dsl.module
import kotlin.native.concurrent.ThreadLocal

fun initKoin(sessionPreference: SessionPreference,sqlDriver: SqlDriver) {

    if(!sessionPreference.isFirestorePersistenceNotDone()) {
        Firebase.firestore.setSettings(false)
        sessionPreference.setFirestorePersistence(true)
    }
    JUDatabase(sqlDriver).userDetailsQueries.apply {
        createRegionMaster()
        createTerritoryMaster()
        createDealerMaster()
        createFinYearMaster()
        createFinMonthMaster()
    }
    JUDatabase(sqlDriver).dealerDetailsQueries.apply {
        createDashboard()
        createProductSalesReport()
        createOpeningBalance()
        createLedger()
    }
    startKoin {
        modules(
            module {
                single { ApiClient.client }
                single { sessionPreference }
                single { DataManager(DataStore()) }
                single { UserDetailsDao(JUDatabase(sqlDriver).userDetailsQueries) }
                single { DealerDashboardDao(JUDatabase(sqlDriver).dealerDetailsQueries) }
                single { DealerLedgerDao(JUDatabase(sqlDriver).dealerDetailsQueries) }
                single<EmployeeRepository> {
                    EmployeeRepositoryImpl(
                        Firebase.firestore.collection(Constants.TABLE_EMP_ACCESS),
                        Firebase.firestore.collection(Constants.TABLE_MENU)
                    )
                }
                single<OTPRepository> { OTPRepositoryImpl(get()) }
                //single<FirebaseAuthRepository> { FirebaseAuthRepositoryImpl(Firebase.auth) }
                single<DealerDashboardRepository> {
                    DealerDashboardRepositoryImpl(
                        Firebase.firestore.collection(Constants.TABLE_DASHBOARD),
                        Firebase.firestore.collection(Constants.TABLE_PRODUCT_SALES),
                        get()
                    )
                }
                single<UserRepository> {
                    UserRepositoryImpl(
                        Firebase.firestore.collection(Constants.TABLE_REGION_MASTER),
                        Firebase.firestore.collection(Constants.TABLE_TERRITORY_MASTER),
                        Firebase.firestore.collection(Constants.TABLE_DEALER_MASTER),
                        Firebase.firestore.collection(Constants.TABLE_FIN_YEAR_MASTER),
                        Firebase.firestore.collection(Constants.TABLE_FIN_MONTH_MASTER),
                        get()
                    )
                }
                single<DealerLedgerRepository> {
                    DealerLedgerRepositoryImpl(
                        Firebase.firestore.collection(Constants.TABLE_LEDGER_OPENING_BALANCE),
                        Firebase.firestore.collection(Constants.TABLE_DEALER_LEDGER),
                        get()
                    )
                }
                single { EmployeeUseCase(get()) }
                single { DealerDashboardUseCase(get()) }
                single { DealerLedgerUseCase(get()) }
                single { OTPUseCase(get()) }
                single { UserDetailsUseCase(get()) }
                factory { SplashViewModel(get(),get()) }
                factory { LoginViewModel(get(),get(),get(),get()) }
                factory { HomeViewModel(get(),get(),get()) }
                factory { DealerDashboardViewModel(get(),get(),get()) }
                factory { LedgerViewModel(get(),get(),get(),get()) }
                factory { TestScreenViewModel(get(),get(),get(),get()) }
            }
        )
    }
}

@ThreadLocal
private object ApiClient {

    //Configure the HttpCLient
    @OptIn(ExperimentalSerializationApi::class)
    var client = HttpClient {

        engine {
            pipelining = true
        }

        // For Logging
        install(Logging) {
            level = LogLevel.ALL
        }

        // Timeout plugin
        install(HttpTimeout) {
            requestTimeoutMillis = 60000L
            connectTimeoutMillis = 60000L
            socketTimeoutMillis = 60000L
        }

        // JSON Response properties
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                    explicitNulls = false
                }
            )
        }

        // Default request for POST, PUT, DELETE,etc...
        install(DefaultRequest) {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            //add this accept() for accept Json Body or Raw Json as Request Body
            accept(ContentType.Application.Json)
        }
    }
}