package com.juagri.shared.ui.components.layouts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.juagri.shared.domain.model.ledger.DealerLedgerItem
import com.juagri.shared.ui.components.fields.ColumnSpaceMedium
import com.juagri.shared.ui.components.fields.ColumnSpaceSmall
import com.juagri.shared.ui.components.fields.LabelAmount
import com.juagri.shared.ui.components.fields.LabelContent
import com.juagri.shared.ui.components.fields.LabelHeading
import com.juagri.shared.ui.components.fields.LedgerIcon
import com.juagri.shared.ui.components.fields.RowSpaceExtraSmall
import com.juagri.shared.ui.components.fields.TextLegend
import com.juagri.shared.utils.getColors
import com.juagri.shared.utils.strings.Names
import com.juagri.shared.utils.theme.ledger_cheque
import com.juagri.shared.utils.theme.ledger_invoice
import com.juagri.shared.utils.toDDMMYYYY
import com.juagri.shared.utils.toMoneyFormat
import com.juagri.shared.utils.value

@Composable
fun LedgerLayout(names: Names, dealerLedgerItem: DealerLedgerItem){
    CardLayout {
        Row {
            Column(modifier = Modifier.weight(1f)) {
                LabelHeading(names.openingBalance)
                LabelAmount(dealerLedgerItem.openingBalance.toMoneyFormat())
                LabelHeading(names.debit)
                LabelAmount(dealerLedgerItem.totalDebit.toMoneyFormat())
                TextLegend(getColors().ledger_invoice,names.invoiceNo)
            }
            Column(modifier = Modifier.weight(1f)) {
                LabelHeading(names.closingBalance)
                LabelAmount(dealerLedgerItem.closingBalance.toMoneyFormat())
                LabelHeading(names.credit)
                LabelAmount(dealerLedgerItem.totalCredit.toMoneyFormat())
                TextLegend(getColors().ledger_cheque,names.chequeNo)
            }
        }
    }
    ColumnSpaceMedium()
    dealerLedgerItem.ledgerItems?.forEach {item->
        CardLayout {
            Row {
                LabelHeading(item.postDate.toDDMMYYYY(), modifier = Modifier.weight(1f))
                LabelHeading(names.debit, modifier = Modifier.weight(1f))
                LabelHeading(names.credit, modifier = Modifier.weight(1f))
                LabelHeading(names.balance, modifier = Modifier.weight(1f))
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f).padding(end = 4.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        LedgerIcon(getColors().ledger_invoice)
                        RowSpaceExtraSmall()
                        LabelContent(item.docNo.value(), modifier = Modifier.weight(1f))
                    }
                    if(item.chequeNo.value().isNotEmpty()){
                        ColumnSpaceSmall()
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            LedgerIcon(getColors().ledger_cheque)
                            RowSpaceExtraSmall()
                            LabelContent(item.chequeNo.value(), modifier = Modifier.weight(1f))
                        }
                    }
                }
                LabelContent(item.debitAmt.toMoneyFormat(), modifier = Modifier.weight(1f))
                LabelContent(item.creditAmt.toMoneyFormat(), modifier = Modifier.weight(1f))
                LabelContent(item.balanceAmt.toMoneyFormat(), modifier = Modifier.weight(1f))
            }
        }
        ColumnSpaceSmall()
    }
}