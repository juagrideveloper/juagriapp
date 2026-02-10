package com.juagri.shared.ui.components.layouts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.juagri.shared.domain.model.ledger.DealerLedgerItemOld
import com.juagri.shared.ui.components.fields.ColumnSpaceMedium
import com.juagri.shared.ui.components.fields.ColumnSpaceSmall
import com.juagri.shared.ui.components.fields.LabelAmount
import com.juagri.shared.ui.components.fields.LabelContent
import com.juagri.shared.ui.components.fields.LabelContentLink
import com.juagri.shared.ui.components.fields.LabelHeading
import com.juagri.shared.ui.components.fields.LedgerIcon
import com.juagri.shared.ui.components.fields.RowSpaceExtraSmall
import com.juagri.shared.ui.components.fields.TextLegend
import com.juagri.shared.utils.getColors
import com.juagri.shared.utils.strings.Names
import com.juagri.shared.utils.theme.ledger_cheque
import com.juagri.shared.utils.theme.ledger_invoice
import com.juagri.shared.utils.toMoneyFormat
import com.juagri.shared.utils.value

@Composable
fun LedgerOldLayout(
    names: Names,
    dealerLedgerItem: DealerLedgerItemOld,
    onInvoiceClick: ((docno: String) -> Unit)? = null
) {
    CardLayout {
        Row {
            Column(modifier = Modifier.weight(1f)) {
                LabelHeading(names.openingBalance)
                LabelAmount(dealerLedgerItem.openingBalanceAmount.toMoneyFormat())
                LabelHeading(names.debit)
                LabelAmount(dealerLedgerItem.debit.toMoneyFormat())
                TextLegend(getColors().ledger_invoice, names.invoiceNo)
            }
            Column(modifier = Modifier.weight(1f)) {
                LabelHeading(names.closingBalance)
                LabelAmount(dealerLedgerItem.closingBalanceAmount.toMoneyFormat())
                LabelHeading(names.credit)
                LabelAmount(dealerLedgerItem.credit.toMoneyFormat())
                TextLegend(getColors().ledger_cheque, names.chequeNo)
            }
        }
    }
    ColumnSpaceMedium()
    dealerLedgerItem.ledgerItems.forEach { item ->
        val clickModifier = if (onInvoiceClick != null) {
            Modifier.clickable {
                if (item.cramt.value() == 0.0) {
                    onInvoiceClick(item.docno.value().replace("/", "___"))
                }
            }
        } else Modifier
        Column(modifier = clickModifier) {
            CardLayout {
                Row {
                    LabelHeading(item.invdate.value(), modifier = Modifier.weight(1f))
                    LabelHeading(names.debit, modifier = Modifier.weight(1f))
                    LabelHeading(names.credit, modifier = Modifier.weight(1f))
                    LabelHeading(names.balance, modifier = Modifier.weight(1f))
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f).padding(end = 4.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            LedgerIcon(getColors().ledger_invoice)
                            RowSpaceExtraSmall()
                            if (item.cramt.value() == 0.0) {
                                LabelContentLink(item.docno.value(), modifier = Modifier.weight(1f))
                            } else {
                                LabelContent(item.docno.value(), modifier = Modifier.weight(1f))
                            }
                        }
                        if (item.chqno.value().isNotEmpty()) {
                            ColumnSpaceSmall()
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                LedgerIcon(getColors().ledger_cheque)
                                RowSpaceExtraSmall()
                                LabelContent(item.chqno.value(), modifier = Modifier.weight(1f))
                            }
                        }
                    }
                    LabelContent(item.dbamt.toMoneyFormat(), modifier = Modifier.weight(1f))
                    LabelContent(item.cramt.toMoneyFormat(), modifier = Modifier.weight(1f))
                    LabelContent(item.balamt.toMoneyFormat(), modifier = Modifier.weight(1f))
                }
            }
        }
        ColumnSpaceSmall()
    }
}
