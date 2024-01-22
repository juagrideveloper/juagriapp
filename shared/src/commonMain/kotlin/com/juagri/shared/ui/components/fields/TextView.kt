package com.juagri.shared.ui.components.fields

import AppTypography
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun TextSmall(
    text: String = "",
    textAlign: TextAlign = TextAlign.Start,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onBackground
) = Text(
    text,
    style = AppTypography.titleSmall,
    modifier = modifier,
    color = color
)

@Composable
fun TextMedium(
    text: String = "",
    textAlign: TextAlign = TextAlign.Start,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onBackground
) = Text(
    text,
    style = AppTypography.titleMedium,
    modifier = modifier,
    color = color,
    textAlign = textAlign
)

@Composable
fun TextTitle(
    text: String = "",
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onBackground
) = Text(
    text,
    style = AppTypography.titleLarge,
    modifier = modifier,
    color = color,
    fontWeight = FontWeight.Bold
)

@Composable
fun NavDrawerUsername(text: String = "", modifier: Modifier = Modifier) = Text(
    text,
    style = AppTypography.titleLarge,
    modifier = modifier,
    color = MaterialTheme.colorScheme.background,
    fontWeight = FontWeight.Bold
)

@Composable
fun NavDrawerRole(text: String = "", modifier: Modifier = Modifier) = Text(
    text,
    style = AppTypography.titleMedium,
    modifier = modifier,
    color = MaterialTheme.colorScheme.background,
    fontWeight = FontWeight.Bold
)

@Composable
fun NavDrawerHeading(text: String = "") = Text(
    text,
    style = AppTypography.bodyLarge,
    modifier = Modifier.padding(16.dp),
    color = MaterialTheme.colorScheme.background,
    fontWeight = FontWeight.Bold
)

@Composable
fun NavDrawerContent(text: String = "", onItemClick: () -> Unit) =
    Box(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .clickable(
            interactionSource = MutableInteractionSource(),
            indication = rememberRipple(color = Color.DarkGray),
            onClick = { onItemClick.invoke() }
        )) {
        Text(
            text,
            style = AppTypography.bodyMedium,
            modifier = Modifier
                .padding(top = 8.dp, bottom = 8.dp, start = 32.dp),
            color = MaterialTheme.colorScheme.background,
            fontWeight = FontWeight.Bold
        )
    }