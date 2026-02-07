package com.juagri.shared.ui.ledger

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.juagri.shared.domain.model.ledger.LedgerInvoiceItemOld
import com.juagri.shared.ui.components.fields.ColumnSpaceSmall
import com.juagri.shared.utils.toMoneyFormat
import com.juagri.shared.utils.value

@Composable
fun LedgerInvoiceDialog(
    invoice: LedgerInvoiceItemOld,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Invoice Details") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                Text("Doc No: ${invoice.docNo.value()}")
                Text("Date: ${invoice.invDate.value()}")
                Text("Cheque No: ${invoice.chqNo.value()}")
                Text("Debit: ${invoice.debitAmt.toMoneyFormat()}")
                Text("Credit: ${invoice.creditAmt.toMoneyFormat()}")
                invoice.items?.forEach { line ->
                    ColumnSpaceSmall()
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            "${line.productName.value()} - Qty: ${line.qty.value()} @ ${line.rate.toMoneyFormat()} = ${line.amount.toMoneyFormat()}"
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}
