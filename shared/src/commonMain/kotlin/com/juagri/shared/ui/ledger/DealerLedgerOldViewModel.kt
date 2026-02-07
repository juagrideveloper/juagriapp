package com.juagri.shared.ui.ledger

import com.juagri.shared.data.local.session.SessionPreference
import com.juagri.shared.data.local.session.datamanager.DataManager
import com.juagri.shared.domain.model.filter.FilterType
import com.juagri.shared.domain.model.ledger.DealerLedgerItemOld
import com.juagri.shared.domain.model.ledger.LedgerInvoiceItemOld
import com.juagri.shared.domain.model.user.FinMonth
import com.juagri.shared.domain.model.user.FinYear
import com.juagri.shared.domain.model.user.JUDealer
import com.juagri.shared.domain.model.user.JURegion
import com.juagri.shared.domain.model.user.JUTerritory
import com.juagri.shared.domain.usecase.DealerLedgerOldUseCase
import com.juagri.shared.domain.usecase.UserDetailsUseCase
import com.juagri.shared.ui.components.base.BaseViewModel
import com.juagri.shared.utils.UIState
import com.juagri.shared.utils.isNotEqualTo
import com.juagri.shared.utils.toYYYYMMDD
import com.juagri.shared.utils.value
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class DealerLedgerOldViewModel(
    session: SessionPreference,
    dataManager: DataManager,
    private val userDetailsUseCase: UserDetailsUseCase,
    private val ledgerOldUseCase: DealerLedgerOldUseCase
) : BaseViewModel(session, dataManager) {

    private val _dealerLedgerItem = MutableStateFlow<UIState<DealerLedgerItemOld?>>(UIState.Init)
    val dealerLedgerItem = _dealerLedgerItem.asStateFlow()

    private val _ledgerInvoice = MutableStateFlow<UIState<LedgerInvoiceItemOld?>>(UIState.Init)
    val ledgerInvoice = _ledgerInvoice.asStateFlow()

    fun getRegionList() {
        backgroundScope {
            userDetailsUseCase.getRegionList().collect { response ->
                uiScopeFilter(response, FilterType.REGION(JURegion()))
            }
        }
    }

    fun getTerritoryList() {
        selectedRegion.value?.let {
            backgroundScope {
                userDetailsUseCase.getTerritoryList(it.regCode.value()).collect { response ->
                    uiScopeFilter(response, FilterType.TERRITORY(JUTerritory()))
                }
            }
        } ?: showErrorMessage(names().pleaseSelectValidRegion)
    }

    fun getDealerList() {
        selectedTerritory.value?.let {
            backgroundScope {
                userDetailsUseCase.getDealerList(it.tCode.value()).collect { response ->
                    uiScopeFilter(response, FilterType.DEALER(JUDealer()))
                }
            }
        } ?: showErrorMessage(names().pleaseSelectValidTerritory)
    }

    fun getFinYearList() {
        backgroundScope {
            userDetailsUseCase.getFinYear().collect { response ->
                uiScopeFilter(response, FilterType.FIN_YEAR(FinYear()))
            }
        }
    }

    fun getFinMonthList() {
        selectedFinYear.value?.let {
            backgroundScope {
                userDetailsUseCase.getFinMonth(it.startDate!!, it.endDate!!).collect { response ->
                    uiScopeFilter(response, FilterType.FIN_MONTH(FinMonth()))
                }
            }
        } ?: showErrorMessage(names().pleaseSelectValidFinYear)
    }

    fun getLedgerDetails() {
        /*selectedRegion.value ?: run {
            showErrorMessage(names().pleaseSelectValidRegion)
            return
        }
        selectedTerritory.value ?: run {
            showErrorMessage(names().pleaseSelectValidTerritory)
            return
        }
        val dealer = selectedDealer.value ?: run {
            showErrorMessage(names().pleaseSelectValidDealer)
            return
        }*/
        val finYear = selectedFinYear.value ?: run {
            showErrorMessage(names().pleaseSelectValidFinYear)
            return
        }
        val finMonth = selectedFinMonth.value
        // Firestore CustomerLedger month doc IDs are YYYY-MM-DD (e.g. 2025-09-01); use startDate for specific month
        val monthCode = when {
            finMonth == null || !finMonth.fMonth.isNotEqualTo(names().all) -> "ALL"
            finMonth.startDate == null -> "ALL"
            else -> finMonth.startDate.toYYYYMMDD()
        }
        showProgressBar(true)
        backgroundScope {
            ledgerOldUseCase.getDealerLedger(getJUEmployee()?.code.value(), finYear.fYear.value(), monthCode)
                .collect { response ->
                    val sorted = response.ledgerItems.sortedBy { it.invdate.value() }
                    var balanceAmount = response.openingBalanceAmount
                    var totalDebit = 0.0
                    var totalCredit = 0.0
                    sorted.forEach { item ->
                        totalDebit += item.dbamt
                        totalCredit += item.cramt
                        balanceAmount = (balanceAmount - item.cramt) + item.dbamt
                        item.balamt = balanceAmount
                    }
                    val result = DealerLedgerItemOld(
                        openingBalanceAmount = response.openingBalanceAmount,
                        closingBalanceAmount = balanceAmount,
                        ledgerItems = sorted,
                        debit = totalDebit,
                        credit = totalCredit
                    )
                    uiScope {
                        showProgressBar(false)
                        _dealerLedgerItem.value = UIState.Success(result)
                    }
                }
        }
    }

    fun getLedgerInvoice(invoiceNo: String) {
        //"BS/AK/2526/00989"
        backgroundScope {
            showProgressBar(true)
            ledgerOldUseCase.getInvoiceDetails(invoiceNo.value().replace("/", "___")).collect { item ->
                uiScope {
                    showProgressBar(false)
                    _ledgerInvoice.value = UIState.Success(item)
                }
            }
        }
    }

    fun resetLedger() {
        _dealerLedgerItem.value = UIState.Success(null)
    }

    fun clearInvoiceState() {
        _ledgerInvoice.value = UIState.Init
    }
}
