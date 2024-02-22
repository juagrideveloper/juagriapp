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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import com.juagri.shared.utils.getColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
fun Modifier.getModifier() = Modifier
    .fillMaxWidth()
    .wrapContentHeight()
    .border(BorderStroke(0.1.dp, getColors().onBackground), RoundedCornerShape(5.dp))
    .padding(4.dp)