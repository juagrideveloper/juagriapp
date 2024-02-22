package com.juagri.shared.data.local.dao.common

import com.juagri.shared.UserDetailsQueries
import com.juagri.shared.domain.model.user.FinMonth
import com.juagri.shared.domain.model.user.FinYear
import com.juagri.shared.domain.model.user.JUDealer
import com.juagri.shared.domain.model.user.JURegion
import com.juagri.shared.domain.model.user.JUTerritory
import com.juagri.shared.utils.endTime
import com.juagri.shared.utils.startTime
import com.juagri.shared.utils.toTimeStamp
import com.juagri.shared.utils.value

class UserDetailsDao(private val queries: UserDetailsQueries) {
    fun insertRegionMaster(regionList: List<JURegion>) {
        regionList.forEach {
            queries.insertRegionMaster(
                id = it.regCode.value(),
                cncode = it.cnCode.value(),
                cnname = it.cnName.value(),
                zcode = it.zCode.value(),
                zname = it.zName.value(),
                stcode = it.stCode.value(),
                stname = it.stName.value(),
                regname = it.regName.value(),
                rmcode = it.rmCode.value(),
                rmname = it.rmName.value(),
                rmmailid = it.rmMailId.value(),
                rmphoneno = it.rmPhoneNo.value(),
                updatedTime = it.updatedTime.value()
            )
        }
    }

    fun insertTerritoryMaster(territoryList: List<JUTerritory>) {
        territoryList.forEach {
            queries.insertTerritoryMaster(
                id = it.tCode.value(),
                socode = it.soCode.value(),
                soname = it.soName.value(),
                somailid = it.soMailId.value(),
                sophoneno = it.soPhoneNo.value(),
                tname = it.tName.value(),
                regcode = it.regCode.value(),
                updatedTime = it.updatedTime.value()
            )
        }
    }

    fun insertDealerMaster(dealerList: List<JUDealer>) {
        dealerList.forEach {
            queries.insertDealerMaster(
                id = it.cCode.value(),
                cname = it.cName.value(),
                mailId = it.mailId.value(),
                address = it.address.value(),
                phoneno = it.phoneNo.value(),
                tcode = it.tCode.value(),
                regcode = it.regCode.value(),
                updatedTime = it.updatedTime.value()
            )
        }
    }

    fun insertFinYear(finYearList: List<FinYear>) {
        finYearList.forEach {
            queries.insertFinYearMaster(
                id = it.fYear.value(),
                start_date = it.startDate.value(),
                end_date = it.endDate.value(),
                updatedTime = it.updatedTime.value()
            )
        }
    }

    fun insertFinMonth(finMonthList: List<FinMonth>) {
        finMonthList.forEach {
            queries.insertFinMonthMaster(
                id = it.fMonth.value(),
                start_date = it.startDate.startTime(),
                end_date = it.endDate.endTime(),
                updatedTime = it.updatedTime.value()
            )
        }
    }

    fun getRegionList() =
        queries.getRegionList().executeAsList().map {
            JURegion(
                regCode = it.id.value(),
                cnCode = it.cncode.value(),
                cnName = it.cnname.value(),
                zCode = it.zcode.value(),
                zName = it.zname.value(),
                stCode = it.stcode.value(),
                stName = it.stname.value(),
                regName = it.regname.value(),
                rmCode = it.rmcode.value(),
                rmName = it.rmname.value(),
                rmMailId = it.rmmailid.value(),
                rmPhoneNo = it.rmphoneno.value(),
                updatedTime = it.updatedTime.toTimeStamp()
            )
        }

    fun getTerritoryList(regCode: String) =
        queries.getTerritoryList(regCode).executeAsList().map {
            JUTerritory(
                tCode = it.id.value(),
                soCode = it.socode.value(),
                soName = it.soname.value(),
                soMailId = it.somailid.value(),
                soPhoneNo = it.sophoneno.value(),
                tName = it.tname.value(),
                regCode = it.regcode.value(),
                updatedTime = it.updatedTime.toTimeStamp()
            )
        }

    fun getDealerList(tCode: String) =
        queries.getDealerList(tCode).executeAsList().map {
            JUDealer(
                cCode = it.id.value(),
                cName = it.cname.value(),
                mailId = it.mailId.value(),
                address = it.address.value(),
                phoneNo = it.phoneno.value(),
                tCode = it.tcode.value(),
                regCode = it.regcode.value(),
                updatedTime = it.updatedTime.toTimeStamp()
            )
        }

    fun getFinYearList() =
        queries.getFinYearList().executeAsList().map {
            FinYear(
                fYear = it.id.value(),
                startDate = it.start_date.toTimeStamp(),
                endDate = it.end_date.toTimeStamp(),
                updatedTime = it.updatedTime.toTimeStamp()
            )
        }

    fun getFinMonthList(startDate: Double, endDate: Double) =
        queries.getFinMonthList(startDate, endDate).executeAsList().map {
            FinMonth(
                fMonth = it.id.value(),
                startDate = it.start_date.toTimeStamp(),
                endDate = it.end_date.toTimeStamp(),
                updatedTime = it.updatedTime.toTimeStamp()
            )
        }

    fun getRegionLastUpdatedTime() = queries.getRegionLastUpdatedTime().executeAsOne().max.value()
    fun getTerritoryLastUpdatedTime() = queries.getTerritoryLastUpdatedTime().executeAsOne().max.value()
    fun getDealerLastUpdatedTime() = queries.getDealerLastUpdatedTime().executeAsOne().max.value()
    fun getFinYearLastUpdatedTime() = queries.getFinYearLastUpdatedTime().executeAsOne().max.value()
    fun getFinMonthLastUpdatedTime() = queries.getFinMonthLastUpdatedTime().executeAsOne().max.value()
}