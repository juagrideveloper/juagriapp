package components.fields

import AppTypography
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SmallText(text: String = "",modifier :Modifier = Modifier) = Text(
    text,
    style = AppTypography.labelSmall,
    modifier = modifier
)

@Composable
fun MediumText(text: String = "",modifier :Modifier = Modifier) = Text(
    text,
    style = AppTypography.labelMedium,
    modifier = modifier
)

@Composable
fun TitleText(text: String = "",modifier :Modifier = Modifier) = Text(
    text,
    style = AppTypography.labelLarge,
    modifier = modifier
)