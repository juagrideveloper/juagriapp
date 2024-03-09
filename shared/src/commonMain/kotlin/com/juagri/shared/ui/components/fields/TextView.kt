package com.juagri.shared.ui.components.fields

import AppTypography
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.juagri.shared.domain.model.dashboard.OSChartItem
import com.juagri.shared.utils.getColors
import com.juagri.shared.utils.getIndianCurrencyFormat
import com.juagri.shared.utils.theme.doctor_no
import com.juagri.shared.utils.value

@Composable
fun TextSmall(
    text: String = "",
    textAlign: TextAlign = TextAlign.Start,
    modifier: Modifier = Modifier,
    color: Color = getColors().onBackground
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
    color: Color = getColors().onBackground
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
    color: Color = getColors().onBackground
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
    color = getColors().background,
    fontWeight = FontWeight.Bold
)

@Composable
fun NavDrawerRole(text: String = "", modifier: Modifier = Modifier) = Text(
    text,
    style = AppTypography.titleMedium,
    modifier = modifier,
    color = getColors().background,
    fontWeight = FontWeight.Bold
)

@Composable
fun NavDrawerHeading(text: String = "") = Text(
    text,
    style = AppTypography.bodyLarge,
    modifier = Modifier.padding(16.dp),
    color = getColors().background,
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
            color = getColors().background,
            fontWeight = FontWeight.Bold
        )
    }

@Composable
fun TextPieLegend(
    osItem: OSChartItem,
    onLegendClick: (OSChartItem) -> Unit
) = Row(modifier = Modifier.clickable(onClick = { onLegendClick.invoke(osItem) })) {
    Spacer(modifier = Modifier.width(16.dp).height(16.dp).background(osItem.color))
    RowSpaceSmall()
    Text(
        "${osItem.label} ${getIndianCurrencyFormat(osItem.value.value().toString())}",
        style = AppTypography.titleSmall,
        color = getColors().onBackground
    )
}

@Composable
fun DialogTitle(
    text: String = "",
    modifier: Modifier = Modifier,
    color: Color = getColors().background
) = Text(
    text,
    style = AppTypography.titleLarge,
    modifier = modifier,
    color = color,
    fontWeight = FontWeight.Bold
)

@Composable
fun FilterTitleItem(
    text: String = "",
    modifier: Modifier = Modifier,
    color: Color = getColors().onBackground,
    onItemClick: () -> Unit
) {
    Divider(color = getColors().onBackground, thickness = 0.2.dp)
    ClickableText(
        text = AnnotatedString(text),
        onClick = { onItemClick.invoke() },
        style = TextStyle(
            color = color,
            fontSize = 12.sp,
            fontStyle = AppTypography.bodySmall.fontStyle
        ),
        modifier = modifier.fillMaxWidth().padding(12.dp),
    )
}

@Composable
fun TextDropdown(
    text: String = "",
    textAlign: TextAlign = TextAlign.Start,
    modifier: Modifier = Modifier,
    color: Color = getColors().onBackground
) = Text(
    text,
    style = AppTypography.labelMedium,
    modifier = modifier,
    color = color,
    textAlign = textAlign
)

@Composable
fun LabelHeading(
    text: String = "",
    textAlign: TextAlign = TextAlign.Start,
    modifier: Modifier = Modifier,
    color: Color = getColors().onBackground
) = Text(
    text,
    style = AppTypography.labelMedium,
    modifier = modifier.padding(bottom = 4.dp),
    color = color,
    fontWeight = FontWeight.Bold,
    textAlign = textAlign
)

@Composable
fun LabelContent(
    text: String = "",
    textAlign: TextAlign = TextAlign.Start,
    modifier: Modifier = Modifier,
    color: Color = getColors().onBackground
) = Text(
    text,
    style = AppTypography.labelSmall,
    modifier = modifier,
    color = color,
    fontWeight = FontWeight.Normal,
    textAlign = textAlign
)

@Composable
fun LabelAmount(
    text: String = "",
    textAlign: TextAlign = TextAlign.Start,
    modifier: Modifier = Modifier,
    color: Color = getColors().onBackground
) = Text(
    text,
    style = AppTypography.labelSmall,
    modifier = modifier.padding(bottom = 4.dp),
    color = color,
    fontWeight = FontWeight.Normal,
    textAlign = textAlign
)

@Composable
fun TextLegend(
    color: Color = Color.Transparent,
    text: String,
) = Row {
    LedgerIcon(color)
    RowSpaceExtraSmall()
    Text(
        text,
        style = AppTypography.labelMedium,
        fontWeight = FontWeight.Normal,
        color = getColors().onBackground
    )
}

@Composable
fun LedgerIcon(color: Color = Color.Transparent){
    Card(
        colors = CardDefaults.cardColors(
            containerColor = getColors().background,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        shape = RoundedCornerShape(2.dp),
        modifier = Modifier
            .wrapContentWidth()
            .wrapContentHeight()
    ) {
        Spacer(modifier = Modifier.width(12.dp).height(12.dp).background(color))
    }
}

@Composable
fun TextCropTitle(
    text: String = "",
    textAlign: TextAlign = TextAlign.Center,
    modifier: Modifier = Modifier.fillMaxWidth().fillMaxHeight().background(Color.Black.copy(0.5f)),
    color: Color = Color.White
) = Text(
    text,
    style = AppTypography.headlineSmall,
    modifier = modifier,
    color = color,
    textAlign = textAlign,
    fontWeight = FontWeight.Bold
)

@Composable
fun TextManagementTitle(
    text: String = "",
    textAlign: TextAlign = TextAlign.Center,
    modifier: Modifier = Modifier.fillMaxWidth().background(Color.Black.copy(0.5f)),
    color: Color = Color.Black
) = Text(
    text,
    style = AppTypography.titleSmall,
    modifier = modifier,
    color = color,
    textAlign = textAlign,
    fontWeight = FontWeight.Bold
)

@Composable
fun TextChildTitle(
    text: String = "",
    textAlign: TextAlign = TextAlign.Center,
    modifier: Modifier = Modifier.fillMaxWidth().fillMaxHeight().background(Color.Black.copy(0.5f)),
    color: Color = Color.White
) = Text(
    text,
    style = AppTypography.titleMedium,
    modifier = modifier,
    color = color,
    textAlign = textAlign,
    fontWeight = FontWeight.Bold
)

@Composable
fun TextSolutionTitle(
    text: String = "",
    textAlign: TextAlign = TextAlign.Center,
    modifier: Modifier = Modifier.fillMaxWidth().fillMaxHeight().background(Color.Blue),
    color: Color = getColors().onBackground
) = Text(
    text,
    style = AppTypography.labelLarge,
    modifier = modifier,
    color = color,
    textAlign = textAlign
)

@Composable
fun TextSolutionNo(
    text: String = "",
    modifier: Modifier = Modifier
        .background(getColors().doctor_no)
        .wrapContentWidth()
        .wrapContentHeight()
        .padding(6.dp),
    color: Color = Color.Black
) = Text(
    text,
    style = AppTypography.labelMedium,
    modifier = modifier,
    color = color
)

@Composable
fun TextProfileHeading(
    text: String = "",
    textAlign: TextAlign = TextAlign.Center,
    modifier: Modifier = Modifier.fillMaxWidth(),
    color: Color = getColors().onBackground
) = Text(
    text,
    style = AppTypography.labelMedium,
    modifier = modifier,
    color = color,
    fontWeight = FontWeight.Bold,
    textAlign = textAlign
)

@Composable
fun TextProfileContent(
    text: String = "",
    textAlign: TextAlign = TextAlign.Center,
    modifier: Modifier = Modifier.fillMaxWidth(),
    color: Color = getColors().onBackground
) = Text(
    text,
    style = AppTypography.labelSmall,
    modifier = modifier,
    color = color,
    fontWeight = FontWeight.Normal,
    textAlign = textAlign
)