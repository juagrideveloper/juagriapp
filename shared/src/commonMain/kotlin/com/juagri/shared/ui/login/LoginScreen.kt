package com.juagri.shared.ui.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.juagri.shared.com.juagri.shared.ui.components.fields.ButtonNormal
import com.juagri.shared.com.juagri.shared.ui.components.fields.SpaceMedium
import com.juagri.shared.com.juagri.shared.ui.components.fields.SpaceSmall
import com.juagri.shared.com.juagri.shared.ui.components.fields.TextMedium
import com.juagri.shared.com.juagri.shared.ui.components.fields.TextTitle
import com.juagri.shared.com.juagri.shared.ui.components.layouts.LoginLayout
import com.juagri.shared.com.juagri.shared.ui.components.layouts.ScreenLayoutWithoutActionBar
import com.juagri.shared.domain.model.menu.SlideMenu
import com.juagri.shared.utils.value
import moe.tlaster.precompose.koin.koinViewModel
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class, ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(onBack: () -> Unit) {

    val viewModel = koinViewModel(LoginViewModel::class)
    ScreenLayoutWithoutActionBar(title = "Login", onBackPressed = {
        onBack.invoke()
    }) {
        /*viewModel.dataManager.setMenuItems(listOf(SlideMenu(1,"ID 1","Menu 1",1,false,false)))
        viewModel.dataManager.menuItems().forEach {
            viewModel.writeLog(it.menuName)
        }*/
        val mobileNo = remember { mutableStateOf("") }
        Box {
            LoginLayout()
            Column(
                modifier = Modifier.fillMaxWidth().wrapContentSize()
                    .padding(start = 24.dp, end = 24.dp)
                    .padding(top = 80.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painterResource("icon_telephone.xml"),
                    null,
                    modifier = Modifier.height(150.dp).width(100.dp)
                )
                SpaceMedium()
                TextTitle("Mobile Number")
                SpaceMedium()
                TextMedium(
                    textAlign = TextAlign.Center,
                    text = "Please enter your registered phone number, you will receive an OTP."
                )
                SpaceMedium()
                Row(
                    modifier = Modifier.padding(start = 24.dp, end = 24.dp)
                        .background(Color.Transparent)
                ) {

                    TextField(
                        value = "+91",
                        onValueChange = {},
                        modifier = Modifier
                            .width(65.dp)
                            .padding(vertical = 4.dp).border(
                                shape = RectangleShape, border = BorderStroke(
                                    (0.5).dp,
                                    Color.LightGray
                                )
                            ),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedIndicatorColor = Color.Transparent,
                            disabledContainerColor = Color.White,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledTextColor = Color.Black
                        ),
                        enabled = false
                    )
                    TextField(
                        value = mobileNo.value,
                        onValueChange = {
                            if (it.length <= 10) mobileNo.value = it
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp).border(
                                shape = RectangleShape, border = BorderStroke(
                                    1.dp,
                                    Color.LightGray
                                )
                            ),

                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedIndicatorColor = Color.Transparent,
                        )
                    )

                }
                val tableUIState by viewModel.tableData.collectAsState()
                val table1Text = remember { mutableStateOf(tableUIState.data1.value()) }
                val table2Text = remember { mutableStateOf(tableUIState.data2.value()) }
                val table3Data = remember { mutableStateOf(tableUIState.getAllData()) }
                SpaceSmall()
                ButtonNormal("Submit") {
                    onBack.invoke()
                    //viewModel.getEmployeeDetails("6001649427")
                }
                SpaceSmall()
                ButtonNormal("Get Table 1") {
                    viewModel.getTable1()
                }
                SpaceSmall()
                ButtonNormal("Get Table 2") {
                    viewModel.getTable2()
                }
                SpaceSmall()
                ButtonNormal("Get Table 3") {
                    viewModel.getAll()
                }
                SpaceSmall()
                TextTitle("Data1")
                TextTitle(table1Text.value.toString())
                SpaceSmall()
                TextTitle("Data2")
                TextTitle(table2Text.value.toString())
                SpaceSmall()
                TextTitle("Data3")
                TextTitle(table3Data.value.toString())
                /*val uiState by viewModel.employee.collectAsState()
                val loading = remember { mutableStateOf(uiState.isLoading) }
                val selectedDate = remember { mutableStateOf("") }
                ProgressDialog(loading.value,{})
                uiState.data?.let {
                    selectedDate.value = it.cname.value()
                }
                Text(selectedDate.value)*/
            }
        }
    }
}