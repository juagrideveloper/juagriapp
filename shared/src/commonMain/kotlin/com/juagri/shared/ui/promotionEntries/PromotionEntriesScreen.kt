package com.juagri.shared.ui.promotionEntries

import Constants
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.juagri.shared.domain.model.filter.FilterType
import com.juagri.shared.domain.model.promotion.ParticipateDialogData
import com.juagri.shared.ui.components.base.BaseViewModel
import com.juagri.shared.ui.components.dialogs.FilterDialog
import com.juagri.shared.ui.components.dialogs.ParticipatedDialog
import com.juagri.shared.ui.components.fields.ButtonNormal
import com.juagri.shared.ui.components.fields.ColumnSpaceSmall
import com.juagri.shared.ui.components.fields.PromotionContent
import com.juagri.shared.ui.components.fields.PromotionHeading
import com.juagri.shared.ui.components.fields.PromotionSubHeading
import com.juagri.shared.ui.components.layouts.CardLayout
import com.juagri.shared.ui.components.layouts.DropDownLayout
import com.juagri.shared.ui.components.layouts.PromotionImage
import com.juagri.shared.ui.components.layouts.ScreenLayout
import com.juagri.shared.utils.UIState
import com.juagri.shared.utils.getColors
import com.juagri.shared.utils.value
import dev.gitlive.firebase.firestore.FieldValue
import moe.tlaster.precompose.koin.koinViewModel

@Composable
fun PromotionEntriesScreen() {
    val viewModel = koinViewModel(PromotionEntriesViewModel::class)
    viewModel.setScreenId(Constants.SCREEN_PROMOTION_ENTRIES_LIST)
    ScreenLayout(viewModel) {
        var isDashboardNotCreated = true
        viewModel.apply {
            CardLayout(true, isScrollable = true) {
                if (enableRegionFilter.value) {
                    DropDownLayout(
                        names().region,
                        mutableStateOf(getRegionLabel())
                    ) {
                        getRegionList()
                    }
                    ColumnSpaceSmall()
                }
                if (enableTerritoryFilter.value) {
                    DropDownLayout(
                        names().territory,
                        mutableStateOf(getTerritoryLabel())
                    ) {
                        getTerritoryList()
                    }
                }
                when (val result = viewModel.promotionEntries.collectAsState().value) {
                    is UIState.Success -> {
                        result.data.forEach {
                            when (it["activity_code"].toString()) {
                                "PM_NDS" -> ndsView(it, viewModel)
                                "PM_DFD" -> dfdView(it, viewModel)
                                "PM_FM" -> fmView(it, viewModel)
                                //"PM_FI" -> fiView(it, viewModel)
                                //"PM_DRC" -> drcView(it, viewModel)
                                else -> {}
                            }
                            ColumnSpaceSmall()
                        }
                    }

                    else -> {}
                }
            }
            when (participationEntry.collectAsState().value) {
                is UIState.Success -> {
                    getPromotionEntries(selectedTerritory.value?.tCode.value())
                    resetParticipationEntry()
                }

                else -> {}
            }
            FilterDialog(showDialog) {
                when (val item = it.data) {
                    is FilterType.REGION -> {
                        selectedRegion.value = item.data
                        selectedTerritory.value = null
                        selectedDealer.value = null
                    }

                    is FilterType.TERRITORY -> {
                        selectedTerritory.value = item.data
                        selectedDealer.value = null
                        getPromotionEntries(selectedTerritory.value?.tCode.value())
                    }

                    else -> {}
                }
            }
            ParticipatedDialog(showParticipateDialog.value, viewModel) {
                val updateItems = mapOf(
                    "participant_" + getRoleID() + "_comments" to it.comments,
                    "participant_" + getRoleID() + "_lat" to it.lat,
                    "participant_" + getRoleID() + "_long" to it.long,
                    "participant_" + getRoleID() + "_userId" to getJUEmployee()?.code.value(),
                    "participant_" + getRoleID() + "_tCode" to getJUEmployee()?.territoryCode.value(),
                    "participant_" + getRoleID() + "_regCode" to getJUEmployee()?.regionCode.value(),
                    "participant_" + getRoleID() + "_updatedTime" to FieldValue.serverTimestamp
                )
                viewModel.setParticipationEntry(it, updateItems)
            }
            if (isDashboardNotCreated) {
                isDashboardNotCreated = false
                getPromotionEntries()
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ndsView(entry: Map<String, Any>,viewModel: BaseViewModel) {
    CardLayout {
        ColumnSpaceSmall()
        RowView {
            PromotionHeading("New Demo")
            PromotionContent(entry["updated_empname"].toString(), textAlign = TextAlign.End)
        }
        ColumnSpaceSmall()
        Divider(color = getColors().onBackground, thickness = 0.2.dp)
        ColumnSpaceSmall()
        RowView {
            PromotionSubHeading("Entry Id")
            PromotionContent(entry["entryId"].toString(), textAlign = TextAlign.End)
        }
        ColumnSpaceSmall()
        RowView {
            PromotionSubHeading("District")
            PromotionContent(entry["district"].toString(), textAlign = TextAlign.End)
        }
        RowView {
            PromotionSubHeading("Village")
            PromotionContent(entry["village"].toString(), textAlign = TextAlign.End)
        }
        RowView {
            PromotionSubHeading("Farmer Name")
            PromotionContent(entry["farmername"].toString(), textAlign = TextAlign.End)
        }
        RowView {
            PromotionSubHeading("Farmer Mobile")
            PromotionContent(entry["farmermobile"].toString(), textAlign = TextAlign.End)
        }
        RowView {
            PromotionSubHeading("Crops")
            PromotionContent(entry["croplist"].toString(), textAlign = TextAlign.End)
        }
        RowView {
            PromotionSubHeading("Products")
            PromotionContent(entry["productlist"].toString(), textAlign = TextAlign.End)
        }
        RowView {
            Row {
                entry["filenames"].toString().split(",").forEach {
                    PromotionImage(it)
                }
            }
            ButtonNormal("I Participated"){
                val entryId = entry["entryId"].toString()
                viewModel.showParticipateDialog.value = ParticipateDialogData(
                    "Add Participation Details",
                    entryId,
                    mutableStateOf(true)
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun dfdView(entry: Map<String, Any>,viewModel: BaseViewModel) {
    CardLayout {
        ColumnSpaceSmall()
        RowView {
            PromotionHeading("Demo Follow-up / Field Day")
            PromotionContent(entry["updated_empname"].toString(), textAlign = TextAlign.End)
        }
        ColumnSpaceSmall()
        Divider(color = getColors().onBackground, thickness = 0.2.dp)
        ColumnSpaceSmall()
        RowView {
            PromotionSubHeading("Entry Id")
            PromotionContent(entry["entryId"].toString(), textAlign = TextAlign.End)
        }
        ColumnSpaceSmall()
        RowView {
            PromotionSubHeading("District")
            PromotionContent(entry["district"].toString(), textAlign = TextAlign.End)
        }
        RowView {
            PromotionSubHeading("Village")
            PromotionContent(entry["village"].toString(), textAlign = TextAlign.End)
        }
        RowView {
            PromotionSubHeading("Farmer Name")
            PromotionContent(entry["farmername"].toString(), textAlign = TextAlign.End)
        }
        RowView {
            PromotionSubHeading("Farmer Mobile")
            PromotionContent(entry["farmermobile"].toString(), textAlign = TextAlign.End)
        }
        RowView {
            PromotionSubHeading("Crops")
            PromotionContent(entry["croplist"].toString(), textAlign = TextAlign.End)
        }
        RowView {
            PromotionSubHeading("Products")
            PromotionContent(entry["productlist"].toString(), textAlign = TextAlign.End)
        }
        RowView {
            Row {
                entry["filenames"].toString().split(",").forEach {
                    PromotionImage(it)
                }
            }
            ButtonNormal("I Participated"){
                val entryId = entry["entryId"].toString()
                viewModel.showParticipateDialog.value = ParticipateDialogData(
                    "Add Participation Details",
                    entryId,
                    mutableStateOf(true)
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun fmView(entry: Map<String, Any>,viewModel: BaseViewModel) {
    CardLayout {
        ColumnSpaceSmall()
        RowView {
            PromotionHeading("Farmer Meeting")
            PromotionContent(entry["updated_empname"].toString(), textAlign = TextAlign.End)
        }
        ColumnSpaceSmall()
        Divider(color = getColors().onBackground, thickness = 0.2.dp)
        ColumnSpaceSmall()
        RowView {
            PromotionSubHeading("Entry Id")
            PromotionContent(entry["entryId"].toString(), textAlign = TextAlign.End)
        }
        ColumnSpaceSmall()
        RowView {
            PromotionSubHeading("District")
            PromotionContent(entry["district"].toString(), textAlign = TextAlign.End)
        }
        RowView {
            PromotionSubHeading("Village")
            PromotionContent(entry["village"].toString(), textAlign = TextAlign.End)
        }
        RowView {
            PromotionSubHeading("Farmer Name")
            PromotionContent(entry["farmername"].toString(), textAlign = TextAlign.End)
        }
        RowView {
            PromotionSubHeading("Farmer Phone")
            PromotionContent(entry["farmerphone"].toString(), textAlign = TextAlign.End)
        }
        RowView {
            PromotionSubHeading("Farmer Count")
            PromotionContent(entry["farmerscount"].toString(), textAlign = TextAlign.End)
        }
        RowView {
            PromotionSubHeading("Products")
            PromotionContent(entry["productlist"].toString(), textAlign = TextAlign.End)
        }
        RowView {
            Row {
                entry["filenames"].toString().split(",").forEach {
                    PromotionImage(it)
                }
            }
            ButtonNormal("I Participated"){
                val entryId = entry["entryId"].toString()
                viewModel.showParticipateDialog.value = ParticipateDialogData(
                    "Add Participation Details",
                    entryId,
                    mutableStateOf(true)
                )
            }
        }
    }
}

@Composable
private fun fiView(entry: Map<String, Any>,viewModel: BaseViewModel) {
    CardLayout {
        ColumnSpaceSmall()
        RowView {
            PromotionHeading("Individual farmer Contact")
            PromotionContent(entry["updated_empname"].toString(), textAlign = TextAlign.End)
        }
        ColumnSpaceSmall()
        Divider(color = getColors().onBackground, thickness = 0.2.dp)
        ColumnSpaceSmall()
        RowView {
            PromotionSubHeading("Entry Id")
            PromotionContent(entry["entryId"].toString(), textAlign = TextAlign.End)
        }
        ColumnSpaceSmall()
        RowView {
            PromotionSubHeading("Farmer Name")
            PromotionContent(entry["farmername"].toString(), textAlign = TextAlign.End)
        }
        RowView {
            PromotionSubHeading("Farmer Phone")
            PromotionContent(entry["farmerphone"].toString(), textAlign = TextAlign.End)
        }
       /* RowView {
            PromotionSubHeading("")
            ButtonNormal("I Participated"){
                val entryId = entry["entryId"].toString()
                viewModel.showParticipateDialog.value = ParticipateDialogData(
                    "Add Participation Details",
                    entryId,
                    mutableStateOf(true),
                    entry["activity_code"].toString()
                )
            }
        }*/
    }
}

@Composable
private fun drcView(entry: Map<String, Any>,viewModel: BaseViewModel) {
    CardLayout {
        ColumnSpaceSmall()
        RowView {
            PromotionHeading("Distributor/Retailer Contact")
            PromotionContent(entry["updated_empname"].toString(), textAlign = TextAlign.End)
        }
        ColumnSpaceSmall()
        Divider(color = getColors().onBackground, thickness = 0.2.dp)
        ColumnSpaceSmall()
        RowView {
            PromotionSubHeading("Ticket Id")
            PromotionContent(entry["entryId"].toString(), textAlign = TextAlign.End)
        }
        RowView {
            PromotionSubHeading("Distributor/Retailer Name")
            PromotionContent(entry["distributorretailername"].toString(), textAlign = TextAlign.End)
        }
        RowView {
            PromotionSubHeading("Distributor/Retailer Phone No")
            PromotionContent(
                entry["distributorretailerphone"].toString(),
                textAlign = TextAlign.End
            )
        }
        /*RowView {
            PromotionSubHeading("")
            ButtonNormal("I Participated"){
                val entryId = entry["entryId"].toString()
                viewModel.showParticipateDialog.value = ParticipateDialogData(
                    "Add Participation Details",
                    entryId,
                    mutableStateOf(true),
                    entry["activity_code"].toString()
                )
            }
        }*/
    }
}

@Composable
fun RowView(block: @Composable() () -> Unit) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        block.invoke()
    }
}