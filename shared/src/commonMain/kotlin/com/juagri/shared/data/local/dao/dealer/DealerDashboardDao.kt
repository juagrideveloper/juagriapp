package com.juagri.shared.data.local.dao.dealer

import com.juagri.shared.DealerDetailsQueries
import com.juagri.shared.domain.model.dashboard.DealerDashboard
import com.juagri.shared.domain.model.dashboard.DealerSales
import com.juagri.shared.utils.isDeleted
import com.juagri.shared.utils.value
import dev.gitlive.firebase.firestore.Timestamp
import dev.gitlive.firebase.firestore.fromMilliseconds

class DealerDashboardDao(private val queries: DealerDetailsQueries) {

    fun getDealerDashboard(empCode: String): DealerDashboard {
        queries.getDashboard(empCode).executeAsOne().apply {
            return DealerDashboard(
                cCode = id,
                cdAvailable = cd_available.value(),
                cdAvailed = cd_availed.value(),
                cdAvailMonth = cd_availmonth.value(),
                cdoId = cdoid.value(),
                cName = cname.value(),
                cPhone = cphone.value(),
                g180 = g180.value(),
                l120 = l120.value(),
                l180 = l180.value(),
                l90 = l90.value(),
                msalValue = msal_value.value(),
                regCode = regcode.value(),
                tCode = tcode.value(),
                totalOS = totalos.value(),
                ysalValue = ysal_value.value(),
                updatedTime = Timestamp.fromMilliseconds(updatedTime.value())
            )
        }
    }

    fun setDealerDashboard(dashboard: DealerDashboard){
        dashboard.let{
            queries.insertDashboard(
                id = it.cCode.value(),
                cd_available = it.cdAvailable.value(),
                cd_availed = it.cdAvailed.value(),
                cd_availmonth = it.cdAvailMonth.value(),
                cdoid = it.cdoId.value(),
                cname = it.cName.value(),
                cphone = it.cPhone.value(),
                g180 = it.g180.value(),
                l120 = it.l120.value(),
                l180 = it.l180.value(),
                l90 = it.l90.value(),
                msal_value = it.msalValue.value(),
                regcode = it.regCode.value(),
                tcode = it.tCode.value(),
                totalos = it.totalOS.value(),
                ysal_value = it.ysalValue.value(),
                updatedTime = it.updatedTime.value()
            )
        }
    }

    fun setProductSalesData(dealerSales: List<DealerSales>) {
        dealerSales.forEach { sales->
            if(sales.status.isDeleted()){
                queries.deleteProductSalesItem(sales.id.value())
            }else {
                queries.insertProductSalesReport(
                    id = sales.id.value(),
                    bcode = sales.bcode.value(),
                    bname = sales.brand.value(),
                    ccode = sales.cCode.value(),
                    qty = sales.qty.value(),
                    type = sales.type.value(),
                    updatedTime = sales.updatedTime.value()
                )
            }
        }
    }

    fun getProductSalesData(empCode: String): List<DealerSales> {
        return queries.getProductSalesReport(empCode).executeAsList().map {
            DealerSales(
                id = it.id.value(),
                bcode = it.bcode.value(),
                brand = it.bname.value(),
                cCode = it.ccode.value(),
                qty = it.qty.value(),
                type = it.type.value(),
                updatedTime = Timestamp.fromMilliseconds(it.updatedTime.value())
            )
        }
    }

    fun getDashboardLastUpdatedTime(empCode: String): Double= queries.getDashboardLastUpdatedTime(empCode).executeAsOne().max.value()

    fun getProductSalesLastUpdateTime(empCode: String): Double= queries.getProductSalesLastUpdatedTime(empCode).executeAsOne().max.value()
}