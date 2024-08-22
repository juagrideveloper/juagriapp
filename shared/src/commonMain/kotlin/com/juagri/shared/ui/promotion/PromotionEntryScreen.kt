package com.juagri.shared.ui.promotion

import Constants
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.juagri.shared.domain.model.filter.FilterType
import com.juagri.shared.domain.model.promotion.PromotionChildItem
import com.juagri.shared.domain.model.promotion.PromotionField
import com.juagri.shared.domain.model.promotion.PromotionFilterDataItem
import com.juagri.shared.domain.model.promotion.PromotionParentItem
import com.juagri.shared.domain.model.promotion.PromotionValue
import com.juagri.shared.domain.model.promotion.VillageItem
import com.juagri.shared.ui.components.dialogs.FilterDialog
import com.juagri.shared.ui.components.dialogs.PromotionFilterDialog
import com.juagri.shared.ui.components.fields.ButtonNormal
import com.juagri.shared.ui.components.fields.LabelHeading
import com.juagri.shared.ui.components.layouts.CardLayout
import com.juagri.shared.ui.components.layouts.DropDownLayout
import com.juagri.shared.ui.components.layouts.EdittextLayout
import com.juagri.shared.ui.components.layouts.LabelLayout
import com.juagri.shared.ui.components.layouts.PeekabooCameraView
import com.juagri.shared.ui.components.layouts.ScreenLayout
import com.juagri.shared.utils.PermissionUtils
import com.juagri.shared.utils.UIState
import com.juagri.shared.utils.disable
import com.juagri.shared.utils.enable
import com.juagri.shared.utils.getColors
import com.juagri.shared.utils.maxLen
import com.juagri.shared.utils.selectedValue
import com.juagri.shared.utils.value
import com.preat.peekaboo.image.picker.SelectionMode
import com.preat.peekaboo.image.picker.rememberImagePickerLauncher
import com.preat.peekaboo.image.picker.toImageBitmap
import com.preat.peekaboo.ui.camera.PeekabooCamera
import com.preat.peekaboo.ui.camera.rememberPeekabooCameraState
import dev.gitlive.firebase.firestore.FieldValue
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import moe.tlaster.precompose.koin.koinViewModel
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource


@Composable
fun PromotionEntryScreen() {
    val viewModel = koinViewModel(PromotionEntryViewModel::class)
    viewModel.setScreenId(Constants.SCREEN_PROMOTION_ENTRY)
    ScreenLayout(viewModel) {
        viewModel.apply {
            CardLayout(true) {
                DropDownLayout(
                    getDealerLabel(),
                    mutableStateOf(selectedDealer.value?.cName.value())
                ) {
                    viewModel.getDealerListByCDO()
                }
                if(selectedDealer.value != null) {
                    DropDownLayout(
                        names().selectEvent,
                        mutableStateOf(selectedPromotionEvent.value?.name.value())
                    ) {
                        viewModel.getPromotionEventList()
                    }
                }
                when (val result = viewModel.promotionFieldItems.collectAsState().value) {
                    is UIState.Success -> {
                        updatePromotionFields(viewModel, result.data)
                    }
                    else -> {
                        updatePromotionFields(viewModel, listOf())
                    }
                }
                when (val result = viewModel.setPromotionEntryData.collectAsState().value) {
                    is UIState.Success -> {
                        if (result.data) {
                            resetFields()
                        }
                    }
                    else -> {}
                }
            }
            FilterDialog(showDialog) {
                when (val item = it.data) {
                    is FilterType.PROMOTION_EVENT -> {
                        selectedPromotionEvent.value = item.data
                        dropdownItem.value = selectedPromotionEvent.value.selectedValue(names())
                        getPromotionFieldList(item.data.id.value())
                    }

                    is FilterType.DISTRICT -> {
                        selectedDistrictItem.value = item.data
                        selectedVillageItem.value = VillageItem()
                    }

                    is FilterType.VILLAGE -> {
                        selectedVillageItem.value = item.data
                    }

                    is FilterType.DEALER -> {
                        selectedDealer.value = item.data
                    }

                    else -> {}
                }
            }
            PromotionFilterDialog(showPromotionDialog) {
                val selectedItems = mutableListOf<String>()
                if (showPromotionDialog.value.childFilterItems.isNotEmpty()) {
                    it.childFilterItems.filter { filter -> filter.isSelected.value }
                        .forEach { childItem ->
                            selectedItems.add(childItem.name)
                            viewModel.writeLog("Selected Product: " + childItem.name)
                        }
                    showPromotionDialog.value.selectedItems.value =
                        selectedItems.joinToString(separator = ", ")
                } else {
                    it.parentFilterItems.forEach { parent ->
                        parent.child.filter { it.isSelected.value }
                            .forEach { child ->
                                selectedItems.add(child.name)
                            }
                    }
                    showPromotionDialog.value.selectedItems.value =
                        selectedItems.joinToString(separator = ", ")
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalResourceApi::class)
@Composable
private fun updatePromotionFields(
    viewModel: PromotionEntryViewModel,
    selectedFieldsList: List<PromotionField>
){
    val keyboardController = LocalFocusManager.current
    val fieldsList = selectedFieldsList.toMutableList()
    if(selectedFieldsList.isNotEmpty()) {
        fieldsList.add(PromotionField(type = Constants.FType_Buttons.toDouble()))
    }
    if(fieldsList.size > 0) {
        val selectedImages = remember { mutableStateListOf<ByteArray>() }
        val openCameraLauncher = remember { mutableStateOf(false) }
        val openPermissionLauncher = remember { mutableStateOf(false) }
        if (openPermissionLauncher.value) {
            openPermissionLauncher.value = false
            PermissionUtils.CameraPermission {
                if (it) {
                    openCameraLauncher.value = true
                } else {
                    viewModel.showErrorMessage("Please allow permission in settings.")
                }
            }
        }
        PeekabooCameraView(
            modifier = Modifier.fillMaxSize(),
            openCameraLauncher,
            onCapture = { byteArray ->
                byteArray?.let {
                    selectedImages.add(it)
                }
                openCameraLauncher.value = false
            },
        )
        val scope = rememberCoroutineScope()
        val multipleImagePicker = rememberImagePickerLauncher(
            // Optional: Set a maximum selection limit, e.g., SelectionMode.Multiple(maxSelection = 5).
            // Default: No limit, depends on system's maximum capacity.
            selectionMode = SelectionMode.Single,
            scope = scope,
            onResult = { byteArrays ->
                byteArrays.forEach {
                    // Process the selected images' ByteArrays.
                    println(it)
                    selectedImages.add(it)
                }
            }
        )
        LazyColumn {
            items(fieldsList.size) { index ->
                val field = fieldsList[index]

                //fieldsList.forEachIndexed { index, field ->
                when (field.type.value().toInt()) {
                    Constants.FType_Label -> {
                        LabelLayout(field.name.value(), field.selectValue)
                    }

                    Constants.FType_TextBox -> {
                        val textBoxValue: MutableState<PromotionValue> =
                            mutableStateOf(PromotionValue.Text())
                        fieldsList[index].valueItem = textBoxValue
                        EdittextLayout(
                            field.name.value(),
                            field.selectValue,
                            field.validation.getInputType(),
                            field.maxlength.maxLen()
                        )
                    }

                    Constants.FType_AreaDropdown -> {
                        val districtValue =
                            mutableStateOf(viewModel.selectedDistrictItem.value.name.value())
                        val villageValue =
                            mutableStateOf(viewModel.selectedVillageItem.value.name.value())
                        /*val areaDropdownValue = mutableStateOf(PromotionValue.AreaDropdown())
                        val areaDropdown: MutableState<PromotionValue> =
                            mutableStateOf(PromotionValue.AreaDropdown())
                        fieldsList[index].valueItem = areaDropdown*/
                        DropDownLayout(viewModel.names().district, districtValue) {
                            viewModel.getDistrictList()
                        }
                        DropDownLayout(viewModel.names().village, villageValue) {
                            viewModel.getVillageList()
                        }
                    }

                    Constants.FType_AreaDropdownMultiple -> {

                    }

                    Constants.FType_ScrollGridView -> {
                        val productData = field.data.value().replace("\\", "")
                        val list = mutableListOf<PromotionChildItem>()
                        Json.parseToJsonElement(productData).jsonArray.forEach {
                            list.add(
                                PromotionChildItem(
                                    it.jsonObject["Name"].toString().replace("\"", "")
                                )
                            )
                        }
                        DropDownLayout(field.name.value(), field.selectValue) {
                            viewModel.showPromotionDialog.value = PromotionFilterDataItem(
                                title = field.name.value(),
                                childFilterItems = list,
                                selectedItems = field.selectValue,
                                isEnabled = mutableStateOf(true)
                            )
                        }
                    }

                    Constants.FType_ExpandableRecyclerView -> {
                        val parentItems = mutableListOf<PromotionParentItem>()
                        val cropList = field.data.value().replace("\\", "")
                        Json.parseToJsonElement(cropList).jsonArray.forEach { parent ->
                            val childItems = mutableListOf<PromotionChildItem>()
                            Json.parseToJsonElement(parent.jsonObject["Data"].toString()).jsonArray.forEach { child ->
                                childItems.add(
                                    PromotionChildItem(
                                        child.jsonObject["Name"].toString().replace("\"", ""),
                                        mutableStateOf(false)
                                    )
                                )
                            }
                            parentItems.add(
                                PromotionParentItem(
                                    childItems,
                                    parent.jsonObject["Name"].toString().replace("\"", ""),
                                )
                            )
                        }
                        DropDownLayout(field.name.value(), field.selectValue) {
                            viewModel.showPromotionDialog.value = PromotionFilterDataItem(
                                title = field.name.value(),
                                parentFilterItems = parentItems,
                                selectedItems = field.selectValue,
                                isEnabled = mutableStateOf(true)
                            )
                        }
                    }

                    Constants.FType_CaptureImages -> {
                        Column(
                            modifier = Modifier
                                .padding(top = 8.dp, bottom = 8.dp)
                                .border(
                                    border = BorderStroke(1.dp, getColors().onBackground),
                                    shape = RectangleShape
                                )
                                .padding(8.dp)
                        ) {
                            LabelHeading(field.name.value())
                            FlowRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                if (selectedImages.size < field.maxlength.value().toInt()) {
                                    Column(
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Image(
                                            painterResource(DrawableResource("icon_camera.xml")),
                                            contentDescription = null,
                                            colorFilter = ColorFilter.tint(getColors().onBackground),
                                            modifier = Modifier.size(40.dp).clickable {
                                                if (selectedImages.size < field.maxlength.value()
                                                        .toInt()
                                                ) {
                                                    openPermissionLauncher.value = true
                                                    //openCameraLauncher.value = true
                                                } else {
                                                    viewModel.showErrorMessage(
                                                        "Only allowed ${
                                                            field.maxlength.value().toInt()
                                                        } files!"
                                                    )
                                                }
                                            }
                                        )
                                    }
                                }
                                selectedImages.forEach {
                                    Box(contentAlignment = Alignment.TopEnd) {
                                        Image(
                                            bitmap = it.toImageBitmap(),
                                            contentDescription = "Selected Image",
                                            modifier =
                                            Modifier
                                                .size(100.dp).padding(4.dp)
                                                .clip(shape = RoundedCornerShape(12.dp)),
                                            contentScale = ContentScale.Crop,
                                        )
                                        IconButton(onClick = {
                                            selectedImages.remove(it)
                                        }, modifier = Modifier.size(24.dp)) {
                                            Icon(
                                                imageVector = Icons.Default.Close,
                                                null,
                                                tint = Color.White,
                                                modifier = Modifier.background(Color.Black)
                                                    .clip(RoundedCornerShape(10.dp))
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Constants.FType_Dropdown -> {

                    }

                    Constants.FType_Calendar -> {

                    }

                    Constants.FType_Checkbox -> {

                    }

                    Constants.FType_Seekbar -> {

                    }

                    Constants.FType_Location -> {

                    }

                    Constants.FType_isWhatsApp -> {

                    }

                    Constants.FType_Buttons -> {
                        Row(
                            Modifier.padding(8.dp).fillMaxWidth(),
                            Arrangement.spacedBy(8.dp, Alignment.End),
                        ) {
                            ButtonNormal("Cancel") {
                                viewModel.resetFields()
                            }
                            ButtonNormal("Submit") {
                                viewModel.getCurrentLocation.enable()
                            }
                        }
                    }

                    else -> {}
                }
            }
        }
        if (viewModel.getCurrentLocation.value) {
            viewModel.getCurrentLocation.disable()
            keyboardController.clearFocus(true)
            viewModel.showProgressBar(true)
            PermissionUtils.GetCurrentLocation { lat, long ->
                println("JUAgriAppTestLogs: GetCurrentLocation setPromotionEntry")
                if (lat == 0.0 && long == 0.0) {
                    viewModel.showProgressBar(false)
                    viewModel.showErrorMessage(Constants.ERROR_MSG)
                } else {
                    val updateEntry = mutableMapOf<String, Any>()
                    var isValid = true
                    fieldsList.sortedBy { it.slNo.value() }.forEach { selectedField ->
                        if (isValid) {
                            when (selectedField.type.value().toInt()) {
                                Constants.FType_Label -> {
                                    updateEntry[selectedField.param.value()] =
                                        selectedField.value.value()
                                }

                                Constants.FType_TextBox -> {
                                    selectedField.selectValue.value.let {
                                        if (it.isEmpty()) {
                                            viewModel.showErrorMessage("Please enter valid " + selectedField.name.value())
                                            isValid = false
                                        }
                                        if (selectedField.validation.value()
                                                .toInt() == 2
                                        ) {
                                            if (selectedField.maxlength.value().toInt() == 10) {
                                                if (it.length != 10) {
                                                    viewModel.showErrorMessage("Please enter valid " + selectedField.name.value())
                                                    isValid = false
                                                }
                                                if (it.isNotEmpty() && it[0].digitToInt() < 6) {
                                                    viewModel.showErrorMessage("Please enter valid " + selectedField.name.value())
                                                    isValid = false
                                                }
                                            }

                                        }
                                        updateEntry[selectedField.param.value()] = it
                                    }
                                }

                                Constants.FType_AreaDropdown -> {
                                    viewModel.selectedVillageItem.value.name?.let {
                                        updateEntry["village"] = it
                                    } ?: run {
                                        viewModel.showErrorMessage("Please enter valid Village")
                                        isValid = false
                                    }
                                    viewModel.selectedDistrictItem.value.name?.let {
                                        updateEntry["district"] = it
                                    } ?: run {
                                        viewModel.showErrorMessage("Please enter valid District")
                                        isValid = false
                                    }
                                }

                                Constants.FType_ScrollGridView -> {
                                    if (selectedField.selectValue.value.isEmpty()) {
                                        viewModel.showErrorMessage("Please enter valid " + selectedField.name.value())
                                        isValid = false
                                    } else {
                                        updateEntry[selectedField.param.value()] =
                                            selectedField.selectValue.value
                                    }
                                }

                                Constants.FType_ExpandableRecyclerView -> {
                                    if (selectedField.selectValue.value.isEmpty()) {
                                        viewModel.showErrorMessage("Please enter valid " + selectedField.name.value())
                                        isValid = false
                                    } else {
                                        updateEntry[selectedField.param.value()] =
                                            selectedField.selectValue.value
                                    }
                                }

                                Constants.FType_CaptureImages -> {
                                    if (selectedImages.isEmpty()) {
                                        viewModel.showErrorMessage("Please select images")
                                        isValid = false
                                    }
                                }
                            }
                        }
                    }
                    if (isValid) {
                        viewModel.getJUEmployee()?.let { emp ->
                            updateEntry["activity_code"] =
                                viewModel.selectedPromotionEvent.value?.id.value()
                            updateEntry["updated_time"] = FieldValue.Companion.serverTimestamp
                            updateEntry["updated_empcode"] = emp.code.value()
                            updateEntry["updated_emprole"] = emp.roleId.value()
                            updateEntry["updated_empname"] = emp.name.value()
                            updateEntry["updated_regcode"] = emp.regionCode.value()
                            updateEntry["updated_terrcode"] = emp.territoryCode.value()
                            updateEntry["dealer_code"] = viewModel.selectedDealer.value?.cCode.value()
                            updateEntry["participant_SO_userId"] = ""
                            updateEntry["participant_RM_userId"] = ""
                            updateEntry["participant_DM_userId"] = ""
                            updateEntry["updated_emp_lat"] = lat
                            updateEntry["updated_emp_long"] = long
                            viewModel.setPromotionEntry(updateEntry, selectedImages)
                        }
                    } else {
                        viewModel.showProgressBar(false)
                    }
                }
            }
        }
    }
}

@Composable
fun CustomCameraView() {
    val state = rememberPeekabooCameraState(onCapture = { /* Handle captured images */ })
    PeekabooCamera(
        state = state,
        modifier = Modifier.fillMaxSize(),
        permissionDeniedContent = {
            println("Camera Permission Denied!")
            // Custom UI content for permission denied scenario
        },
    )
}

private fun Double?.getInputType(): KeyboardType = when(this.value().toInt()){
    2-> KeyboardType.Number
    3-> KeyboardType.Email
    else -> KeyboardType.Text
}