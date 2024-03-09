package com.juagri.shared.ui

import Constants
import com.juagri.shared.data.local.session.SessionPreference
import com.juagri.shared.data.local.session.datamanager.DataManager
import com.juagri.shared.domain.model.dashboard.DealerSales
import com.juagri.shared.domain.model.doctor.JUDoctorDataItem
import com.juagri.shared.domain.model.doctor.JUDoctorItem
import com.juagri.shared.domain.model.menu.ChildSlideMenu
import com.juagri.shared.domain.model.menu.HeaderSlideMenu
import com.juagri.shared.domain.usecase.JUDoctorUseCase
import com.juagri.shared.ui.components.base.BaseViewModel
import com.juagri.shared.utils.UIState
import com.juagri.shared.utils.value
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.Timestamp
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject

class TestScreenViewModel(
    dataManager: DataManager,
    session: SessionPreference,
    private val juDoctorUseCase: JUDoctorUseCase
) : BaseViewModel(session, dataManager) {

    init {
        getJUDoctorDetails(1L,"0")
    }

    private var _juDoctorItems: MutableStateFlow<UIState<List<JUDoctorDataItem>>> =
        MutableStateFlow(UIState.Init)
    val juDoctorItems = _juDoctorItems.asStateFlow()

    fun getNameObject(obj: JsonObject): String{
        val nameObject = obj["itmname"].toString().value().replace("\\","").replace("\"{","{").replace("}\"","}")
        return Json.parseToJsonElement(nameObject).jsonObject["en"].toString()
    }

    fun getJUDoctorDetails(type: Long, parentId: String){
        backgroundScope {
            juDoctorUseCase.getJUDoctorItems(parentId).collect { response ->
                uiScope(response,_juDoctorItems)
            }
        }
    }

    /*private fun getItem(it: JUDoctorItem): JUDoctorItem =
        JUDoctorItem(
            name = it.name.replaceQuotes(),
            image = it.image.replaceQuotes(),
            type = it.type.value(),
            child = it.child.map { item-> getItem(item) }
        )*/

    fun setJUDoctorDetails(list: List<JUDoctorItem>){
        val doctorItems = mutableListOf<JUDoctorDataItem>()
        list.forEach {parent->
            val parentId = parent.name.value().trim()
            doctorItems.add(
                JUDoctorDataItem(
                    id = parentId,
                    name = parent.name,
                    image = parent.image,
                    type = parent.type,
                    hasChild= parent.child.isNotEmpty(),
                    parentId = "0"
                )
            )
            parent.child.forEachIndexed { child1Index, child1->
                val childId1 = parentId+"_"+child1Index
                doctorItems.add(
                    JUDoctorDataItem(
                        id = childId1,
                        name = child1.name,
                        image = child1.image,
                        type = child1.type,
                        hasChild= child1.child.isNotEmpty(),
                        parentId = parentId
                    )
                )
                child1.child.forEachIndexed { child2Index, child2->
                    val childId2 = childId1+"_"+child2Index
                    doctorItems.add(
                        JUDoctorDataItem(
                            id = childId2,
                            name = child2.name,
                            image = child2.image,
                            type = child2.type,
                            hasChild= child2.child.isNotEmpty(),
                            parentId = childId1
                        )
                    )
                    child2.child.forEachIndexed { child3Index, child3->
                        val childId3 = childId2+"_"+child3Index
                        doctorItems.add(
                            JUDoctorDataItem(
                                id = childId3,
                                name = child3.name,
                                image = child3.image,
                                type = child3.type,
                                hasChild= child3.child.isNotEmpty(),
                                parentId = childId2
                            )
                        )
                    }
                }
            }
        }
        backgroundScope {
            writeLog("TotalItems: "+doctorItems.size)
            doctorItems.forEach {
               /* Firebase.firestore
                    .collection(Constants.TABLE_JU_DOCTOR)
                    .document(it.id.value())
                    .set(it)*/
            }
        }
        /*backgroundScope {
            list.forEach {
                Firebase.firestore
                    .collection(Constants.TABLE_JU_DOCTOR+"_Test")
                    .document(it.name.replaceQuotes())
                    .set(getItem(it))
            }
        }*/
    }

   /* fun processData() {
        val doctorItems = hashMapOf<String,JUDoctorItem>()
        val jsonObject = Json.parseToJsonElement(MockResponse.mockDealerLedgerItems).jsonObject
        jsonObject.keys.forEach {
            jsonObject[it]?.jsonObject?.let { child1 ->
                writeLog("--------------------------------------")
                val child1Name = getNameObject(child1)
                val child1Type = child1["type"].toString().value().toInt()
                val child1Image = child1["itmimg"].toString()
                val child1ChildItems = mutableListOf<JUDoctorItem>()

                writeLog("Child 1 Name: $child1Name")
                child1["subchild"]?.jsonObject?.let { object1->
                    object1.keys.forEach {
                        object1[it]?.jsonObject?.let {child2->

                            val child2Name = getNameObject(child2)
                            val child2Type = child2["type"].toString().value().toInt()
                            val child2Image = child2["itmimg"].toString()
                            val child2ChildItems = mutableListOf<JUDoctorItem>()

                            writeLog("Child 2 Name: $child2Name")
                            child2["subchild"]?.jsonObject?.let {oject2->
                                oject2.keys.forEach {
                                    oject2[it]?.jsonObject?.let {child3->
                                        val child3Name = getNameObject(child3)
                                        val child3Type = child3["type"].toString().value().toInt()
                                        val child3Image = child3["itmimg"].toString()
                                        val child3ChildItems = mutableListOf<JUDoctorItem>()

                                        writeLog("Child 3 Name: $child3Name")
                                        child3["subchild"]?.jsonObject?.let { object3 ->
                                            object3.keys.forEach {
                                                object3[it]?.jsonObject?.let { child4 ->
                                                    val child4Name = getNameObject(child4)
                                                    val child4Type = child4["type"].toString().value().toInt()
                                                    val child4Image = child4["itmimg"].toString()
                                                    val child4ChildItems = mutableListOf<JUDoctorItem>()
                                                    child3ChildItems.add(
                                                        JUDoctorItem(
                                                            name = child4Name,
                                                            image = child4Image,
                                                            type = child4Type
                                                        )
                                                    )
                                                    writeLog("Child 4 Name: $child4Name")
                                                }
                                            }
                                        }

                                        child2ChildItems.add(
                                            JUDoctorItem(
                                                name = child3Name,
                                                image = child3Image,
                                                type = child3Type,
                                                child = child3ChildItems
                                            )
                                        )
                                    }
                                }
                            }

                            child1ChildItems.add(
                                JUDoctorItem(
                                    name = child2Name,
                                    image = child2Image,
                                    type = child2Type,
                                    child = child2ChildItems
                                )
                            )
                        }
                    }
                }
                doctorItems.put(
                    child1Name,
                    JUDoctorItem(
                        name = child1Name,
                        image = child1Image,
                        type = child1Type,
                        child = child1ChildItems
                    )
                )
            }
        }

        backgroundScope {
            doctorItems.forEach {
                Firebase.firestore
                    .collection(Constants.TABLE_JU_DOCTOR)
                    .document(it.key)
                    .set(it.value)
            }
        }
    }*/

    /*fun processData() {
        val jsonObject = Json.parseToJsonElement(MockResponse.mockDealerLedgerItems).jsonObject
        jsonObject.keys.forEach {
            jsonObject[it]?.jsonObject?.let { parent ->
                writeLog("--------------------------------------")
                val cropName = getNameObject(parent)
                writeLog("Crop Name: $cropName")
                parent["subchild"]?.jsonObject?.let { cropManagement->
                    cropManagement.keys.forEach {
                        cropManagement[it]?.jsonObject?.let {management->
                            val managementName = getNameObject(management)
                            writeLog("Management Name: $managementName")
                            management["subchild"]?.jsonObject?.let {solutionObject->
                                solutionObject.keys.forEach {
                                    solutionObject[it]?.jsonObject?.let {solution->
                                        val solutionName = getNameObject(solution)
                                        writeLog("Solution Name: $solutionName")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }*/

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