package com.juagri.shared.ui.liquidation

import Constants
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.juagri.shared.domain.model.liquidation.DealerLiquidationData
import com.juagri.shared.ui.InputTextField
import com.juagri.shared.ui.components.dialogs.SuccessDialog
import com.juagri.shared.ui.components.fields.ButtonNormal
import com.juagri.shared.ui.components.fields.ColumnSpaceSmall
import com.juagri.shared.ui.components.fields.DashboardLabelHeading
import com.juagri.shared.ui.components.fields.HeadingText
import com.juagri.shared.ui.components.fields.LabelHeading
import com.juagri.shared.ui.components.fields.RowSpaceSmall
import com.juagri.shared.ui.components.fields.TextMedium
import com.juagri.shared.ui.components.layouts.CardLayout
import com.juagri.shared.ui.components.layouts.ScreenLayout
import com.juagri.shared.utils.UIState
import com.juagri.shared.utils.getColors
import com.juagri.shared.utils.theme.chart_m_actual_end
import com.juagri.shared.utils.theme.chart_m_plan_start
import com.juagri.shared.utils.toDDMMYYYY
import moe.tlaster.precompose.koin.koinViewModel

@Composable
fun LiquidationScreen() {
    val viewModel = koinViewModel(LiquidationViewModel::class)
    viewModel.setScreenId(Constants.SCREEN_CDO_LIQUIDATION)
    ScreenLayout(viewModel) {
        viewModel.apply {
            val focusManager = LocalFocusManager.current
            var isFirstApiCallNotDone = true
            val showSuccessDialog: MutableState<Boolean> = mutableStateOf(false)
            CardLayout(true, isScrollable = true) {
                when (val result = viewModel.dealerLiquidationItems.collectAsState().value) {
                    is UIState.Success -> {
                        result.data.let { liquidationData ->
                            liquidationData.liquidationItems.forEach { dealerItem ->
                                CardLayout {
                                    DashboardLabelHeading(dealerItem.cName)
                                    ColumnSpaceSmall()
                                    Row {
                                        HeadingText("Brand", modifier = Modifier.background(getColors().chart_m_actual_end).padding(top = 4.dp, bottom = 4.dp).fillMaxWidth().weight(0.5f))
                                        HeadingText("QTY", modifier = Modifier.background(getColors().chart_m_actual_end).padding(top = 4.dp, bottom = 4.dp).fillMaxWidth().weight(0.25f))
                                        HeadingText("Stock", modifier = Modifier.background(getColors().chart_m_actual_end).padding(top = 4.dp, bottom = 4.dp).fillMaxWidth().weight(0.25f))
                                    }
                                    dealerItem.brandItems.forEachIndexed { index, liquidation ->
                                        Row(modifier = Modifier.padding(8.dp)) {
                                            TextMedium(
                                                liquidation.bName,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .weight(0.5f)
                                            )
                                            TextMedium(
                                                liquidation.pQty.toString(),
                                                textAlign = TextAlign.End,
                                                modifier = Modifier.height(25.dp)
                                                    .weight(0.25f)
                                            )
                                            RowSpaceSmall()
                                            if(liquidationData.config.isEnabled) {
                                                InputTextField(
                                                    value = liquidation.stockItem.value.toString(),
                                                    onValueChange = {
                                                        liquidation.stockItem.value =
                                                            it.toDouble()
                                                    },
                                                    textStyle = LocalTextStyle.current.copy(
                                                        textAlign = TextAlign.End
                                                    ),
                                                    enabled = liquidationData.config.isEnabled,
                                                    keyboardOptions = KeyboardOptions.Default.copy(
                                                        keyboardType = KeyboardType.Number
                                                    ),
                                                    modifier = Modifier.height(25.dp)
                                                        .weight(0.25f),
                                                    colors = TextFieldDefaults.colors(
                                                        focusedContainerColor = Color.White,
                                                        unfocusedContainerColor = Color.White,
                                                        focusedIndicatorColor = Color.Transparent,
                                                    )
                                                )
                                            }else{
                                                TextMedium(
                                                    liquidation.stockItem.value.toString(),
                                                    textAlign = TextAlign.End,
                                                    modifier = Modifier.height(25.dp)
                                                        .weight(0.25f)
                                                )
                                            }
                                        }
                                    }
                                }
                                ColumnSpaceSmall()
                            }
                            if(liquidationData.config.isEnabled && liquidationData.liquidationItems.isNotEmpty()) {
                                Row(
                                    Modifier.padding(8.dp).fillMaxWidth(),
                                    Arrangement.spacedBy(8.dp, Alignment.End),
                                ) {
                                    ButtonNormal("Reset") {
                                        liquidationData.liquidationItems.forEach {
                                            it.brandItems.forEach {
                                                it.stockItem.value = 0.0
                                            }
                                        }
                                    }
                                    ButtonNormal("Submit") {
                                        focusManager.clearFocus(true)
                                        val updatedItems =
                                            liquidationData.liquidationItems.filter {
                                                it.brandItems.filter { brand -> brand.stockItem.value > 0 && brand.stock != brand.stockItem.value }
                                                    .isNotEmpty()
                                            }
                                        if(updatedItems.isNotEmpty()){
                                            setUpdateLiquidation(
                                                DealerLiquidationData(
                                                    liquidationData.config,
                                                    updatedItems
                                                )
                                            )
                                        }else {
                                            viewModel.showErrorMessage("Please enter valid stock!")
                                        }
                                    }
                                }
                            }
                        }
                    }
                    else -> {}
                }
                when (val result = viewModel.updateLiquidationItems.collectAsState().value) {
                    is UIState.Success -> {
                        showSuccessDialog.value = result.data
                    }
                    else -> {}
                }
                SuccessDialog(
                    showSuccessDialog,
                    title = "Success",
                    "Liquidation details updated successfully!"
                ) {
                    getDealerLiquidationItems()
                }
                if(isFirstApiCallNotDone){
                    isFirstApiCallNotDone = false
                    getDealerLiquidationItems()
                }
            }
        }
    }
}