package com.juagri.shared.ui.components.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.juagri.shared.domain.model.filter.FilterDataItem
import com.juagri.shared.domain.model.filter.FilterItem
import com.juagri.shared.ui.components.fields.ButtonNormal
import com.juagri.shared.ui.components.fields.ColumnSpaceMedium
import com.juagri.shared.ui.components.fields.DialogTitle
import com.juagri.shared.ui.components.fields.FilterTitleItem
import com.juagri.shared.ui.components.fields.SearchView
import com.juagri.shared.ui.components.fields.TextTitle
import com.juagri.shared.utils.disable
import com.juagri.shared.utils.getBackgroundGradient
import com.juagri.shared.utils.getColors
import com.juagri.shared.utils.isContains

@Composable
fun MyAlertDialog(
    showDialog: MutableState<Boolean>,
    title: String = "Filter",
    onClickCancel: () -> Unit,
    onClickOK: () -> Unit,
) {
    if (showDialog.value) {
        Dialog({}) {
            Surface(shape = MaterialTheme.shapes.medium) {
                Column {
                    Column(Modifier.padding(16.dp)) {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            TextTitle(title)
                            IconButton(onClick = {
                                showDialog.disable()
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    null,
                                    tint = getColors().onBackground
                                )
                            }
                        }
                        ColumnSpaceMedium()
                    }
                    Spacer(Modifier.size(4.dp))
                    Row(
                        Modifier.padding(8.dp).fillMaxWidth(),
                        Arrangement.spacedBy(8.dp, Alignment.End),
                    ) {
                        ButtonNormal("Cancel") {
                            showDialog.disable()
                            onClickCancel.invoke()
                        }
                        ButtonNormal("OK") {
                            showDialog.disable()
                            onClickOK.invoke()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FilterDialog(
    showDialog: MutableState<FilterDataItem>,
    onItemSelect: (FilterItem) -> Unit,
) {
    if (showDialog.value.isEnabled.value) {
        Dialog({}) {
            Surface(shape = MaterialTheme.shapes.medium, modifier = Modifier.heightIn(max = 550.dp)) {
                Column(modifier = Modifier.background(getColors().background)) {
                    Column(Modifier.background(getBackgroundGradient()).padding(8.dp)) {
                        Row(
                            Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            DialogTitle(showDialog.value.title)
                            IconButton(onClick = { showDialog.value.isEnabled.disable() }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    null,
                                    tint = getColors().background
                                )
                            }
                        }
                    }
                    val searchText = remember { mutableStateOf(TextFieldValue()) }
                    SearchView(searchText)
                    var filteredItems: List<FilterItem>
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        filteredItems = if (searchText.value.text.isNotEmpty()) {
                            showDialog.value.filterItems.filter { it.name.isContains(searchText.value.text) }
                        } else {
                            showDialog.value.filterItems.toList()
                        }
                        items(filteredItems.size) {
                            val item = filteredItems[it]
                            FilterTitleItem(item.name) {
                                showDialog.value.isEnabled.disable()
                                onItemSelect.invoke(item)
                            }
                        }
                    }
                }
            }
        }
    }
}