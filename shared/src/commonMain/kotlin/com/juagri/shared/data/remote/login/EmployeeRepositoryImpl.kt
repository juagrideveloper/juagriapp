package com.juagri.shared.data.remote.login

import Constants
import com.juagri.shared.domain.model.employee.JUEmployee
import com.juagri.shared.domain.model.menu.ChildSlideMenu
import com.juagri.shared.domain.model.menu.HeaderSlideMenu
import com.juagri.shared.domain.model.user.JURegion
import com.juagri.shared.domain.model.user.JUTerritory
import com.juagri.shared.domain.model.user.LoginInfo
import com.juagri.shared.domain.repo.login.EmployeeRepository
import com.juagri.shared.utils.AppUtils
import com.juagri.shared.utils.JUError
import com.juagri.shared.utils.ResponseState
import com.juagri.shared.utils.value
import dev.gitlive.firebase.firestore.CollectionReference
import dev.gitlive.firebase.firestore.where
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class EmployeeRepositoryImpl(
    private val db:CollectionReference,
    private val menuDB:CollectionReference,
    private val regionDB:CollectionReference,
    private val territoryDB:CollectionReference,
    private val deviceInfoDB:CollectionReference,
): EmployeeRepository {

    override suspend fun getEmployeeDetails(
        mobileNo: String,
        withMenu: Boolean
    ): Flow<ResponseState<JUEmployee>> = callbackFlow {
        trySend(ResponseState.Loading(true))
        try {
            val result = db.document(mobileNo).get().reference
            val employee = result.get().data() as JUEmployee
            if(employee.active) {
                if (withMenu) {
                    employee.menuId?.let {
                        employee.menuItems = menuDB.document(it).get().reference.get().data()
                        /*employee.menuItems?.forEach { menu ->
                            if (menu.value.id.value() == "H_001") {
                                when (employee.roleId.value()) {
                                    Constants.EMP_ROLE_SO, Constants.EMP_ROLE_RM, Constants.EMP_ROLE_DM -> {
                                        employee.menuItems!![menu.key]?.childMenus?.add(
                                            ChildSlideMenu(
                                                Constants.SCREEN_PARTICIPATION,
                                                2,
                                                "Promotion Activity",
                                                true
                                            )
                                        )
                                    }
                                }
                            }
                        }
                        employee.menuItems!![Constants.HEADING_MENU_0002] = HeaderSlideMenu(
                            Constants.HEADING_MENU_0002,
                            menuName = "Services",
                            isActive = true,
                            childMenus = mutableListOf()
                        )
                        employee.menuItems!![Constants.HEADING_MENU_0002]?.childMenus?.add(
                            ChildSlideMenu(
                                Constants.SCREEN_JU_Doctor,
                                1,
                                "Promotion Activity",
                                true
                            )
                        )
                        employee.menuItems!![Constants.HEADING_MENU_0002]?.childMenus?.add(
                            ChildSlideMenu(Constants.SCREEN_WEATHER, 2, "Promotion Activity", true)
                        )*/
                    }
                    if (employee.roleId == "CDO" || employee.roleId == "SO") {
                        employee.territoryCode?.let { tCode ->
                            employee.territoryList.apply {
                                clear()
                                addAll(getTerritoryByCode(tCode))
                            }
                        }
                    } else {
                        employee.regionCode?.let { regCode ->
                            employee.territoryList.apply {
                                clear()
                                addAll(getTerritoryByCode(regCode))
                            }
                        }
                    }
                    employee.regionCode?.let { regCode ->
                        employee.regionList.apply {
                            clear()
                            addAll(getRegionListByCode(regCode))
                        }
                    }
                    val deviceInfo = AppUtils.getDeviceInfo()
                    deviceInfoDB.document(employee.code.value()).set(
                        LoginInfo(
                            empCode = employee.code.value(),
                            empName = employee.name.value(),
                            empRole = employee.role.value(),
                            empRoleId = employee.roleId.value(),
                            tCode = employee.territoryCode.value(),
                            regCode = employee.regionCode.value(),
                            deviceOS = deviceInfo["device_os"].toString(),
                            deviceSDK = deviceInfo["device_sdk"].toString(),
                            appVersion = deviceInfo["app_version"].toString(),
                            deviceMode = deviceInfo["device_mode"].toString()
                        )
                    )
                }
                trySend(ResponseState.Loading())
                trySend(ResponseState.Success(employee))
            }else{
                trySend(ResponseState.Loading())
                trySend(ResponseState.Error(JUError.DeactivatedUserError))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            trySend(ResponseState.Error(JUError.CustomError("Please enter registered mobile number!")))
        }
        awaitClose {
            channel.close()
        }
    }

    private suspend fun getRegionListByCode(regCodes: String): List<JURegion> {
        val result = regionDB.where {
            Constants.FIELD_REG_CODE inArray regCodes.split(",")
        }.get().documents
        return try {
            val regionList: List<JURegion> = result.map { it.data() }
            regionList
        } catch (e: Exception) {
            listOf()
        }
    }

    private suspend fun getTerritoryByCode(tCode: String): List<JUTerritory> {
        val whereName = if (tCode.lowercase().startsWith("rgn")) {
            Constants.FIELD_REG_CODE
        } else {
            Constants.FIELD_T_CODE
        }
        val result = territoryDB.where {
            whereName inArray tCode.split(",")
        }.get().documents

        return try {
            val territoryList: List<JUTerritory> = result.map { it.data() }
            territoryList
        } catch (e: Exception) {
            listOf()
        }
    }
}