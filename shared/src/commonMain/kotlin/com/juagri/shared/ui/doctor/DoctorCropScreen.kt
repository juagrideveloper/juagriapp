package com.juagri.shared.ui.doctor

import Constants
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.router.stack.push
import com.juagri.shared.ui.components.layouts.CardLayout
import com.juagri.shared.ui.components.layouts.DoctorChildLayout
import com.juagri.shared.ui.components.layouts.DoctorCropLayout
import com.juagri.shared.ui.components.layouts.DoctorManagementLayout
import com.juagri.shared.ui.components.layouts.DoctorSolutionLayout
import com.juagri.shared.ui.components.layouts.ScreenLayout
import com.juagri.shared.ui.components.layouts.ScreenLayoutWithoutActionBar
import com.juagri.shared.ui.navigation.AppScreens
import com.juagri.shared.utils.UIState
import com.juagri.shared.utils.getColors
import com.juagri.shared.utils.theme.doctor_mgmt_1
import com.juagri.shared.utils.theme.doctor_mgmt_2
import com.juagri.shared.utils.theme.doctor_mgmt_3
import com.juagri.shared.utils.theme.doctor_mgmt_4
import com.juagri.shared.utils.theme.doctor_mgmt_5
import com.juagri.shared.utils.value
import io.github.xxfast.decompose.router.Router
import moe.tlaster.precompose.koin.koinViewModel

@Composable
fun DoctorCropScreen(router: Router<AppScreens>, parentId: String) {
    val viewModel = koinViewModel(DoctorViewModel::class)
    viewModel.setScreenId(Constants.SCREEN_JU_Doctor)
    var isBaseApiNotCalled = true
    ScreenLayoutWithoutActionBar {
        ScreenLayout(viewModel, false) {
            CardLayout(true) {
                when (val result = viewModel.juDoctorItems.collectAsState().value) {
                    is UIState.Success -> {
                        val managementColors = listOf(
                            getColors().doctor_mgmt_1,
                            getColors().doctor_mgmt_2,
                            getColors().doctor_mgmt_3,
                            getColors().doctor_mgmt_4,
                            getColors().doctor_mgmt_5,
                        )
                        if(result.data.isNotEmpty()){
                            if (result.data[0].type.value() == 4){
                                LazyColumn (modifier = Modifier.fillMaxWidth()){
                                    val list = result.data
                                    itemsIndexed(list) {index, item ->
                                        val rowNo = index+1
                                        DoctorSolutionLayout(rowNo,item)
                                        if(rowNo < list.size){
                                            Divider(color = getColors().onBackground, thickness = 0.2.dp)
                                        }
                                    }
                                }
                            }else{
                                LazyVerticalGrid(
                                    columns = GridCells.Adaptive(minSize = 128.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    itemsIndexed(result.data) { index, item ->
                                        when(item.type.value()){
                                            1 -> DoctorCropLayout(item){ doctorItem-> router.push(AppScreens.JUDoctorManagement(doctorItem.id.value())) }
                                            2 -> DoctorManagementLayout(item,managementColors[index]){ doctorItem-> router.push(AppScreens.JUDoctorChild(doctorItem.id.value()))}
                                            3 -> DoctorChildLayout(item){ doctorItem-> router.push(AppScreens.JUDoctorSolution(doctorItem.id.value()))}
                                        }
                                    }
                                }
                            }
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    if(isBaseApiNotCalled){
        viewModel.getJUDoctorDetails(parentId)
        isBaseApiNotCalled = false
    }
}