package com.juagri.shared.ui.components.layouts

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import com.juagri.shared.utils.getColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.juagri.shared.ui.components.fields.TextDropdown

@Composable
fun DropDownLayout(text :String,modifier: Modifier,onClick:()->Unit) {
    Row (
        modifier = modifier.clickable { onClick.invoke() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextDropdown(text)
        Icon(
            imageVector = Icons.Default.ArrowDropDown,
            null,
            tint = getColors().onBackground
        )
    }
}

@Composable
fun DropDownLayout(title: String, text: MutableState<String>, onClick: () -> Unit) {
    OutlinedTextField(
        value = text.value,
        onValueChange = { text.value = it },
        label = { TextDropdown(title) },
        enabled = false,
        modifier = Modifier.fillMaxWidth().padding(0.dp).clickable { onClick.invoke() },
        textStyle = TextStyle(fontSize = 16.sp),
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                null,
                tint = getColors().onBackground
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            disabledTextColor = getColors().onBackground,
            disabledBorderColor = getColors().onBackground,
            disabledLabelColor = getColors().onBackground,
        )
    )
}

@Composable
fun EdittextLayout(title: String, text: MutableState<String>,keyboardType: KeyboardType = KeyboardType.Text,maxLength: Int = 100) {
    OutlinedTextField(
        value = text.value,
        onValueChange = { if(it.length <= maxLength) text.value = it },
        label = { TextDropdown(title) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),

        modifier = Modifier.fillMaxWidth().padding(0.dp)
    )
}

@Composable
fun LabelLayout(title: String, text: MutableState<String>) {
    OutlinedTextField(
        value = text.value,
        onValueChange = { text.value = it },
        label = { TextDropdown(title) },
        enabled = false,
        modifier = Modifier.fillMaxWidth().padding(0.dp),
        colors = OutlinedTextFieldDefaults.colors(
            disabledTextColor = getColors().onBackground,
            disabledBorderColor = getColors().onBackground,
            disabledLabelColor = getColors().onBackground,
        )
    )
}

@Composable
fun CheckboxLayout(title: String, checked: MutableState<Boolean>) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .border(BorderStroke(1.dp, getColors().onBackground), RoundedCornerShape(4.dp))
            .padding(top = 4.dp, bottom = 4.dp, start = 14.dp, end = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextDropdown(title)
        Checkbox(
            checked = checked.value,
            onCheckedChange = { isChecked -> checked.value = isChecked }
        )
    }
}

@Composable
fun CheckboxListItemLayout(title: String, checked: MutableState<Boolean>,onChecked: ((Boolean) -> Unit)? = null) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { checked.value = !checked.value
                onChecked?.invoke(checked.value )
            }
            .padding(start = 14.dp, end = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextDropdown(title)
        Checkbox(
            checked = checked.value,
            onCheckedChange = {
                isChecked -> checked.value = isChecked
                onChecked?.invoke(isChecked)
            }
        )
    }
    Divider(color = getColors().onBackground, thickness = 0.2.dp)
}

@Composable
fun Modifier.getModifier() = Modifier
    .fillMaxWidth()
    .wrapContentHeight()
    .border(BorderStroke(0.1.dp, getColors().onBackground), RoundedCornerShape(5.dp))
    .padding(4.dp)