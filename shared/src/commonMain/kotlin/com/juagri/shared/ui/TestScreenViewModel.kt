package com.juagri.shared.ui

import Constants
import com.juagri.shared.data.local.session.SessionPreference
import com.juagri.shared.data.local.session.datamanager.DataManager
import com.juagri.shared.domain.model.dashboard.DealerSales
import com.juagri.shared.domain.model.menu.ChildSlideMenu
import com.juagri.shared.domain.model.menu.HeaderSlideMenu
import com.juagri.shared.domain.usecase.DealerLedgerUseCase
import com.juagri.shared.domain.usecase.UserDetailsUseCase
import com.juagri.shared.ui.components.base.BaseViewModel
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.Timestamp
import dev.gitlive.firebase.firestore.firestore

class TestScreenViewModel(
    private val dataManager: DataManager,
    private val session: SessionPreference,
    private val userDetailsUseCase: UserDetailsUseCase,
    private val ledgerUseCase: DealerLedgerUseCase
) : BaseViewModel(session, dataManager) {

    fun setMenu() {
        val menus = hashMapOf<String,HeaderSlideMenu>()
        menus["H_001"] =
            HeaderSlideMenu(
                id = "H_001",
                index = 1,
                menuName = "Menu",
                isActive = true,
                updatedTime = Timestamp.now(),
                childMenus = listOf(
                    ChildSlideMenu(
                        id = "C_001",
                        index = 1,
                        menuName = "Dashboard",
                        isActive = true,
                    ),
                    ChildSlideMenu(
                        id = "C_002",
                        index = 2,
                        menuName = "Customer Ledger",
                        isActive = true,
                    ),
                    ChildSlideMenu(
                        id = "C_003",
                        index = 2,
                        menuName = "Online Order",
                        isActive = true,
                    ),
                    ChildSlideMenu(
                        id = "C_004",
                        index = 2,
                        menuName = "Your Orders",
                        isActive = true,
                    )
                )
            )
        menus["H_002"] =
            HeaderSlideMenu(
                id = "H_002",
                index = 2,
                menuName = "Services",
                isActive = true,
                updatedTime = Timestamp.now(),
                childMenus = listOf(
                    ChildSlideMenu(
                        id = "C_005",
                        index = 1,
                        menuName = "JU Doctor",
                        isActive = true,
                    ),
                    ChildSlideMenu(
                        id = "C_006",
                        index = 2,
                        menuName = "Weather",
                        isActive = true,
                    )
                )
            )
        menus["H_003"] =
            HeaderSlideMenu(
                id = "H_003",
                index = 2,
                menuName = "User",
                isActive = true,
                updatedTime = Timestamp.now(),
                childMenus = listOf(
                    ChildSlideMenu(
                        id = "C_007",
                        index = 1,
                        menuName = "Profile",
                        isActive = true,
                    ),
                    ChildSlideMenu(
                        id = "C_008",
                        index = 2,
                        menuName = "Devices",
                        isActive = true,
                    )
                )
            )
        backgroundScope {
            Firebase.firestore
                .collection(Constants.TABLE_MENU)
                .document("DL_MENU")
                .set(menus)
        }
    }

    fun setData() {
        /*backgroundScope {
            val region = JURegion2(
                regCode = "RGN-TN-001",
                cnCode = "IN",
                cnName = "INDIA",
                zCode = "ZONE-001",
                zName = "SOUTH",
                stCode = "TN",
                stName = "TAMIL NADU",
                regName = "TRICHY",
                rmCode = "RM-TN-001",
                rmName = "KUMAR",
                rmMailId = "KUMAR@JUAGRISCIENCES.COM",
                rmPhoneNo = "9578080988",
                updatedTime = Timestamp.ServerTimestamp
            )
            val territory = JUTerritory2(
                tCode = "TN-001",
                soCode = "TN-001",
                soName = "Territory",
                soMailId = "test@gmail.com",
                soPhoneNo = "09898989898",
                tName = "Test",
                regCode = "121231",
                updatedTime = Timestamp.ServerTimestamp
            )
            val dealer = JUDealer2(
                cCode = "CAP-0015",
                cName = "Test User",
                mailId = "test@gmail.com",
                address = "Tamil Nadu",
                phoneNo = "90909090909",
                tCode = "TN-001",
                regCode = "RGN-TN-001",
                updatedTime = Timestamp.ServerTimestamp
            )
            val finYear = FinYear2(
                fYear = "2023-2024",
                startDate = Timestamp.now(),
                endDate = Timestamp.now(),
                updatedTime = Timestamp.now()
            )
            val finMonth = FinMonth2(
                fMonth = "March - 2023",
                startDate = Timestamp.now(),
                endDate = Timestamp.now(),
                updatedTime = Timestamp.now()
            )
            Firebase.firestore
                .collection(Constants.TABLE_REGION_MASTER)
                .document(region.regCode.value())
                .set(territory)
            Firebase.firestore
                .collection(Constants.TABLE_TERRITORY_MASTER)
                .document(territory.tCode.value())
                .set(territory)
            Firebase.firestore
                .collection(Constants.TABLE_DEALER_MASTER)
                .document(dealer.cCode.value())
                .set(dealer)
            Firebase.firestore
                .collection(Constants.TABLE_FIN_YEAR_MASTER)
                .document(finYear.fYear.value())
                .set(finYear)
            Firebase.firestore
                .collection(Constants.TABLE_FIN_MONTH_MASTER)
                .document(finMonth.fMonth.value())
                .set(finMonth)
        }*/
    }

    fun getDashboard() {
        setDemoUser()
        /*withScope{
            dealerDashboardUseCase.getDashboard(session.empCode()).collect{ response ->
                when(response) {
                    is ResponseState.Loading -> showProgressBar(response.isLoading)
                    is ResponseState.Success -> _dealerDashboard.value = UIState.Success(response.data)
                    is ResponseState.Error -> processError(response.e)
                }
            }
        }

        withScope {
            dealerDashboardUseCase.getProductSalesReport(session.empCode()).collect{response ->
                when(response) {
                    is ResponseState.Loading -> showProgressBar(response.isLoading)
                    is ResponseState.Success -> {
                        _productSalesReport.value = UIState.Success(response.data)
                    }
                    is ResponseState.Error -> processError(response.e)
                }
            }
        }*/
    }

    fun uploadData() {
        //DealerDashboardItem
        val mon = mutableListOf<DealerSales>()
        /*(1..15).forEach {
            mon.add(
                DealerSales(cCode = "CTN-001", type = "MTD",bcode = "B00$it", brand = "Brand $it",qty = (100..1000).random().toDouble())
            )
        }*/
        val year = mutableListOf<DealerSales>()
        /*(1..15).forEach {
            year.add(
                DealerSales(cCode = "CTN-001", type = "YTD",bcode = "B00$it", brand = "Brand $it",qty = (100..1000).random().toDouble())
            )
        }*/
        /*viewModelScope.launch {
            Firebase.firestore
                .collection(Constants.TABLE_DASHBOARD)
                .document("CTN-001")
                .set(
                    DealerDashboardItem(
                        cCode = "CTN-001",
                        cdAvailable = 0.0,
                        cdAvailed = 0.0,
                        cdAvailMonth = "",
                        cdoId = "CDO-001",
                        cName = "SRI VENKATESWARA TRADERS - SIRIVELLA",
                        cPhone = "9578080988",
                        g180 = 25.0,
                        l120 = 25.0,
                        l180 = 25.0,
                        l90 = 25.0,
                        *//*monthSales = mon.toList(),
                        yearSales = year.toList(),*//*
                        msalValue = 25.0,
                        regCode = "RGN-TN-001",
                        tCode = "TN-001",
                        today = "28-Jan-2024",
                        totalOS = 100.0,
                        ysalValue = 25.0
                    )
                )
            mon.forEach {
                Firebase.firestore
                    .collection(Constants.TABLE_PRODUCT_SALES)
                    .document(it.type+"_"+it.bcode)
                    .set(it)
            }
            year.forEach {
                Firebase.firestore
                    .collection(Constants.TABLE_PRODUCT_SALES)
                    .document(it.type+"_"+it.bcode)
                    .set(it)
            }
        }*/
    }
    /*fun getDate(){
        showProgressBar(true)
        viewModelScope.launch {
            val array = Json{ignoreUnknownKeys = true}.decodeFromString<List<DealerLedgerItem1>>(MockResponse.mockDealerLedgerItems)
            array.forEach { item->
                Firebase.firestore
                    .collection("V1_DealerLedger_Test")
                    .document("2023-2024")
                    .collection(item.cust_code.value())
                    .document(item.id.value())
                    .set(
                        DealerLedgerItem2(
                            id = item.id.value(),
                            postDate = Timestamp.fromMilliseconds(getMilliSecs(item.post_date.value())),
                            custCode = item.cust_code,
                            docNo = item.doc_no,
                            chequeNo = item.cheque_no,
                            debitAmt = item.debit_amt,
                            creditAmt = item.credit_amt,
                            updatedTime = Timestamp.fromMilliseconds(1704881410000.0)
                        )
                    )
                writeLog("Log: "+item.doc_no)
                writeLog(Timestamp.fromMilliseconds(getMilliSecs(item.post_date.value())).toString())
            }
            showProgressBar(false)
        }
    }*/
}