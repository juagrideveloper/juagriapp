package com.juagri.shared.com.juagri.shared.ui.components.fields

import AppTypography
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

@Composable
fun TextSmall(text: String = "", textAlign: TextAlign = TextAlign.Start, modifier :Modifier = Modifier, color: Color = MaterialTheme.colorScheme.onBackground) = Text(
    text,
    style = AppTypography.labelSmall,
    modifier = modifier,
    color = color
)

@Composable
fun TextMedium(text: String = "", textAlign: TextAlign = TextAlign.Start,modifier :Modifier = Modifier, color: Color = MaterialTheme.colorScheme.onBackground) = Text(
    text,
    style = AppTypography.labelMedium,
    modifier = modifier,
    color = color,
    textAlign = textAlign
)

@Composable
fun TextTitle(text: String = "", modifier :Modifier = Modifier, color: Color = MaterialTheme.colorScheme.onBackground) = Text(
    text,
    style = AppTypography.titleLarge,
    modifier = modifier,
    color = color,
    fontWeight = FontWeight.Bold
)