package com.juagri.shared.ui.components.dialogs

import Constants
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.juagri.shared.domain.model.filter.FilterDataItem
import com.juagri.shared.domain.model.filter.FilterItem
import com.juagri.shared.domain.model.promotion.ParticipateDialogData
import com.juagri.shared.domain.model.promotion.ParticipationEntry
import com.juagri.shared.domain.model.promotion.PromotionChildItem
import com.juagri.shared.domain.model.promotion.PromotionFilterDataItem
import com.juagri.shared.ui.components.base.BaseViewModel
import com.juagri.shared.ui.components.fields.ButtonNormal
import com.juagri.shared.ui.components.fields.ColumnSpaceExtraSmall
import com.juagri.shared.ui.components.fields.ColumnSpaceMedium
import com.juagri.shared.ui.components.fields.DialogTitle
import com.juagri.shared.ui.components.fields.FilterTitleItem
import com.juagri.shared.ui.components.fields.LabelHeading
import com.juagri.shared.ui.components.fields.ListHeading
import com.juagri.shared.ui.components.fields.SearchView
import com.juagri.shared.ui.components.fields.TextDropdown
import com.juagri.shared.ui.components.fields.TextMedium
import com.juagri.shared.ui.components.layouts.CheckboxListItemLayout
import com.juagri.shared.ui.components.layouts.PeekabooCameraView
import com.juagri.shared.utils.PermissionUtils
import com.juagri.shared.utils.disable
import com.juagri.shared.utils.enable
import com.juagri.shared.utils.getBackgroundGradient
import com.juagri.shared.utils.getColors
import com.juagri.shared.utils.isContains
import com.juagri.shared.utils.lowerCase
import com.preat.peekaboo.image.picker.SelectionMode
import com.preat.peekaboo.image.picker.rememberImagePickerLauncher
import com.preat.peekaboo.image.picker.toImageBitmap
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalLayoutApi::class, ExperimentalResourceApi::class, InternalResourceApi::class)
@Composable
fun ParticipatedDialog(
    dialogData: ParticipateDialogData,
    viewModel: BaseViewModel,
    onClickOK: (ParticipationEntry) -> Unit,
) {
    val comments = mutableStateOf("")
    if (dialogData.showDialog.value) {
        val keyboardController = LocalFocusManager.current
        val selectedImages = remember { mutableStateListOf<ByteArray>() }
        val openCameraLauncher =remember {  mutableStateOf(false)}
        val openPermissionLauncher =  remember { mutableStateOf(false) }
        if(openPermissionLauncher.value) {
            openPermissionLauncher.value = false
            PermissionUtils.CameraPermission {
                if (it) {
                    openCameraLauncher.value = true
                }else{
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
        Dialog({}) {
            Surface(shape = MaterialTheme.shapes.medium, modifier = Modifier.heightIn(max = 550.dp)) {
                Column(modifier = Modifier.background(getColors().background)) {
                    Column(Modifier.background(getBackgroundGradient()).padding(8.dp)) {
                        Row(
                            Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            DialogTitle(dialogData.title)
                            IconButton(onClick = { dialogData.showDialog.disable() }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    null,
                                    tint = getColors().background
                                )
                            }
                        }
                    }
                    Column(Modifier.padding(8.dp)) {
                        OutlinedTextField(
                            value = comments.value,
                            onValueChange = { comments.value = it },
                            label = { TextDropdown("Comments") },
                            modifier = Modifier.fillMaxWidth().height(120.dp).padding(0.dp)
                        )
                        ColumnSpaceMedium()
                        if(!dialogData.activityCode.equals("PM_FI") && !dialogData.activityCode.equals("PM_DRC")) {
                            LabelHeading("Capture Images")
                            ColumnSpaceExtraSmall()
                            FlowRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                if (selectedImages.size < 2) {
                                    Column(
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Image(
                                            painterResource(DrawableResource("icon_camera.xml")),
                                            contentDescription = null,
                                            colorFilter = ColorFilter.tint(getColors().onBackground),
                                            modifier = Modifier.size(40.dp).clickable {
                                                openPermissionLauncher.value = true
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
                                                .size(80.dp)
                                                .clip(shape = RoundedCornerShape(12.dp)),
                                            contentScale = ContentScale.Crop,
                                        )
                                        IconButton(onClick = {
                                            selectedImages.remove(it)
                                        }, modifier = Modifier.size(18.dp)) {
                                            Icon(
                                                imageVector = Icons.Default.Close,
                                                null,
                                                tint = Color.White,
                                                modifier = Modifier.background(Color.Black)
                                                    .clip(
                                                        RoundedCornerShape(10.dp)
                                                    )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        Row(
                            Modifier.fillMaxWidth(),
                            Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                        ) {
                            ButtonNormal("Cancel") {
                                dialogData.showDialog.disable()
                            }
                            ButtonNormal("Submit") {
                                keyboardController.clearFocus()
                                if(comments.value.isEmpty()) {
                                    viewModel.showErrorMessage("Please enter comments")
                                }else if(selectedImages.isEmpty() && !dialogData.activityCode.equals("PM_FI") && !dialogData.activityCode.equals("PM_DRC")){
                                    viewModel.showErrorMessage("Please select image")
                                }else{
                                    viewModel.showProgressBar(true)
                                    viewModel.getCurrentLocation.enable()
                                }
                            }
                        }
                        if (viewModel.getCurrentLocation.value) {
                            PermissionUtils.GetCurrentLocation{ lat, long ->
                                if (lat == 0.0 && long == 0.0) {
                                    viewModel.showProgressBar(false)
                                    viewModel.showErrorMessage(Constants.ERROR_MSG)
                                }else{
                                    onClickOK(
                                        ParticipationEntry(
                                            dialogData.entryId,
                                            comments.value,
                                            lat,
                                            long,
                                            selectedImages
                                        )
                                    )
                                }
                            }
                            viewModel.getCurrentLocation.disable()
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

@Composable
fun PromotionFilterDialog(
    showDialog: MutableState<PromotionFilterDataItem>,
    onItemSelect: (PromotionFilterDataItem) -> Unit,
) {
    TextMedium(showDialog.value.selectedItems.value)
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
                            IconButton(onClick = {
                                onItemSelect.invoke(showDialog.value)
                                showDialog.value.isEnabled.disable()
                            }) {
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
                    if(showDialog.value.childFilterItems.isNotEmpty()) {
                        var filteredItems: List<PromotionChildItem>
                        LazyColumn(modifier = Modifier.fillMaxWidth()) {
                            filteredItems = if (searchText.value.text.isNotEmpty()) {
                                showDialog.value.childFilterItems.filter {
                                    it.name.isContains(
                                        searchText.value.text
                                    )
                                }
                            } else {
                                showDialog.value.childFilterItems.toList()
                            }
                            items(filteredItems.size) {
                                filteredItems[it].let { child ->
                                    CheckboxListItemLayout(child.name, child.isSelected){
                                        val selectedItems = mutableListOf<String>()
                                        showDialog.value.childFilterItems.filter { filter-> filter.isSelected.value }.forEach {childItem->
                                            selectedItems.add(childItem.name)
                                        }
                                        showDialog.value.selectedItems.value = selectedItems.joinToString(separator = ", ")
                                        if(selectedItems.size > 3 && child.isSelected.value){
                                            child.isSelected.value = false
                                        }
                                    }
                                }
                            }
                        }
                    }else{
                        showDialog.value.parentFilterItems.let {parentItems->
                            val collapsedState = remember(parentItems) {
                                parentItems.map { true }.toMutableStateList()
                            }
                            LazyColumn(Modifier) {
                               val filteredItems = if (searchText.value.text.isNotEmpty()) {
                                    showDialog.value.parentFilterItems.filter {parent->
                                        parent.child.filter { child -> child.name.lowerCase().isContains(searchText.value.text.lowerCase()) }
                                            .isNotEmpty()
                                    }
                                } else {
                                    showDialog.value.parentFilterItems.toList()
                                }
                                filteredItems.forEachIndexed { i, dataItem ->
                                    val collapsed = collapsedState[i]
                                    item(key = "header_$i") {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            modifier = Modifier.fillMaxWidth()
                                                .padding(top = 8.dp, bottom = 8.dp, start = 8.dp, end = 8.dp)
                                                .clickable {
                                                    collapsedState[i] = !collapsed
                                                }
                                        ) {
                                            ListHeading(dataItem.name)
                                            Icon(
                                                if (collapsed) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
                                                contentDescription = "",
                                                tint = getColors().onBackground,
                                            )
                                        }
                                        Divider()
                                    }
                                    if (!collapsed) {
                                        items(dataItem.child.size) { row ->
                                            Row {
                                                Spacer(
                                                    modifier = Modifier.size(
                                                        24.dp
                                                    )
                                                )
                                                CheckboxListItemLayout(
                                                    dataItem.child[row].name,
                                                    dataItem.child[row].isSelected
                                                ){
                                                    val selectedItems = mutableListOf<String>()
                                                    showDialog.value.parentFilterItems.forEach { parent ->
                                                        parent.child.filter { it.isSelected.value }
                                                            .forEach { child ->
                                                                selectedItems.add(child.name)
                                                            }
                                                    }
                                                    showDialog.value.selectedItems.value = selectedItems.joinToString(separator = ", ")
                                                    if(selectedItems.size > 3 && dataItem.child[row].isSelected.value){
                                                        dataItem.child[row].isSelected.value = false
                                                    }
                                                }
                                            }
                                            Divider()
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}