package com.juagri.shared.ui.loginInfo

import Constants
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.juagri.shared.domain.model.filter.FilterItem
import com.juagri.shared.domain.model.user.LoginInfo
import com.juagri.shared.ui.components.fields.ColumnSpaceMedium
import com.juagri.shared.ui.components.fields.ColumnSpaceSmall
import com.juagri.shared.ui.components.fields.FilterTitleItem
import com.juagri.shared.ui.components.fields.PromotionContent
import com.juagri.shared.ui.components.fields.PromotionHeading
import com.juagri.shared.ui.components.fields.PromotionSubHeading
import com.juagri.shared.ui.components.fields.SearchView
import com.juagri.shared.ui.components.layouts.CardLayout
import com.juagri.shared.ui.components.layouts.ScreenLayout
import com.juagri.shared.ui.promotionEntries.RowView
import com.juagri.shared.utils.UIState
import com.juagri.shared.utils.disable
import com.juagri.shared.utils.getColors
import com.juagri.shared.utils.isContains
import com.juagri.shared.utils.toDDMMYYYY
import moe.tlaster.precompose.koin.koinViewModel

@Composable
fun LoginInfoScreen() {
    val viewModel = koinViewModel(LoginInfoViewModel::class)
    viewModel.setScreenId(Constants.SCREEN_LOGIN_INFO)
    ScreenLayout(viewModel) {
        viewModel.apply {
            var isFirstApiCallNotDone = true
            CardLayout(true) {
                val searchText = remember { mutableStateOf(TextFieldValue()) }
                SearchView(searchText)
                ColumnSpaceSmall()
                when (val result = viewModel.loginInfoItems.collectAsState().value) {
                    is UIState.Success -> {
                        var loginInfoItems: List<LoginInfo>
                        LazyColumn(modifier = Modifier.fillMaxWidth()) {
                            loginInfoItems = if (searchText.value.text.isNotEmpty()) {
                                result.data.filter { it.empName.isContains(searchText.value.text) || it.empCode.isContains(searchText.value.text) }
                            } else {
                                result.data.toList()
                            }
                            items(loginInfoItems.size) {
                                val loginInfo = loginInfoItems[it]
                                CardLayout {
                                    RowView {
                                        PromotionHeading(loginInfo.empName)
                                        PromotionContent(
                                            loginInfo.empCode,
                                            textAlign = TextAlign.End
                                        )
                                    }
                                    ColumnSpaceSmall()
                                    Divider(color = getColors().onBackground, thickness = 0.2.dp)
                                    ColumnSpaceSmall()
                                    RowView {
                                        PromotionSubHeading("App Version")
                                        PromotionContent(
                                            loginInfo.appVersion,
                                            textAlign = TextAlign.End
                                        )
                                    }
                                    RowView {
                                        PromotionSubHeading("Last Online")
                                        PromotionContent(
                                            loginInfo.lastOnline.toDDMMYYYY(),
                                            textAlign = TextAlign.End
                                        )
                                    }
                                }
                                ColumnSpaceSmall()
                            }
                        }
                    }
                    else -> {}
                }
                /*when (val result = viewModel.loginInfoItems.collectAsState().value) {
                    is UIState.Success -> {
                        result.data.forEach {
                            CardLayout {
                                RowView {
                                    PromotionHeading(it.empName)
                                    PromotionContent(it.empCode, textAlign = TextAlign.End)
                                }
                                ColumnSpaceSmall()
                                Divider(color = getColors().onBackground, thickness = 0.2.dp)
                                ColumnSpaceSmall()
                                RowView {
                                    PromotionSubHeading("App Version")
                                    PromotionContent(it.appVersion, textAlign = TextAlign.End)
                                }
                                RowView {
                                    PromotionSubHeading("Last Online")
                                    PromotionContent(
                                        it.lastOnline.toDDMMYYYY(),
                                        textAlign = TextAlign.End
                                    )
                                }
                            }
                            ColumnSpaceSmall()
                        }
                    }
                    else -> {}
                }*/
            }
            if(isFirstApiCallNotDone){
                isFirstApiCallNotDone = false
                getLoginInfoDetails()
            }
        }
    }
}