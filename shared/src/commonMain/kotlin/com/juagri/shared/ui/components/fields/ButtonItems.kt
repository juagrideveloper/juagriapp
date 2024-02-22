package com.juagri.shared.ui.components.fields

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.juagri.shared.data.remote.chat.ChatButtonContent
import com.juagri.shared.utils.getBackgroundGradient
import com.juagri.shared.utils.getColors

@Composable
fun ButtonNormal(
    text: String,
    onClick: () -> Unit = { },
) {
    Button(
        modifier = Modifier,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues(),
        onClick = { onClick() },
    ) {
        Box(
            modifier = Modifier
                .background(getBackgroundGradient())
                .then(Modifier)
                .wrapContentWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp),
            contentAlignment = Alignment.Center,
        ) {
            TextMedium(text = text, color = getColors().background)
        }
    }
}

@Composable
fun ButtonChatOption(content: ChatButtonContent, isEnabled: MutableState<Boolean>, onClicked: (ChatButtonContent) -> Unit) {
    val animatedButtonColor = animateColorAsState(
        targetValue = if (isEnabled.value) getColors().tertiary else Color.Gray,
        animationSpec = tween(1000, 0, LinearEasing)
    )
    return Button(
        onClick = {
            if (isEnabled.value) {
                onClicked.invoke(content)
            }
        },
        contentPadding = PaddingValues(start = 8.dp, end = 8.dp),
        modifier = Modifier.height(30.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = animatedButtonColor.value,
            disabledContainerColor = animatedButtonColor.value,
        )
    ) {
        TextMedium(text = content.title, color = getColors().background)
    }
}

/*
@Composable
fun CustomFloatingActionButton(
    expandable: Boolean,
    onFabClick: () -> Unit,
    fabIcon: ImageVector
) {
    var isExpanded by remember { mutableStateOf(false) }
    if (!expandable) { // Close the expanded fab if you change to non expandable nav destination
        isExpanded = false
    }

    val fabSize = 64.dp
    val expandedFabWidth by animateDpAsState(
        targetValue = if (isExpanded) 200.dp else fabSize,
        animationSpec = spring(dampingRatio = 3f)
    )
    val expandedFabHeight by animateDpAsState(
        targetValue = if (isExpanded) 58.dp else fabSize,
        animationSpec = spring(dampingRatio = 3f)
    )

    Column {

        // ExpandedBox over the FAB
        Box(
            modifier = Modifier
                .offset (y = (25).dp)
                .size(
                    width = expandedFabWidth,
                    height = (animateDpAsState(if (isExpanded) 225.dp else 0.dp, animationSpec = spring(dampingRatio = 4f))).value)
                .background(
                    getColors().surface,
                    shape = RoundedCornerShape(18.dp)
                )
        ) {
            // Customize the content of the expanded box as needed
        }

        FloatingActionButton(
            onClick = {
                onFabClick()
                if (expandable) {
                    isExpanded = !isExpanded
                }
            },
            modifier = Modifier
                .width(expandedFabWidth)
                .height(expandedFabHeight),
            shape = RoundedCornerShape(18.dp)

        ) {

            Icon(
                imageVector = fabIcon,
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .offset(x = animateDpAsState(if (isExpanded) -70.dp else 0.dp, animationSpec = spring(dampingRatio = 3f)).value)
            )

            Text(
                text = "Create Reminder",
                softWrap = false,
                modifier = Modifier
                    .offset(x = animateDpAsState(if (isExpanded) 10.dp else 50.dp, animationSpec = spring(dampingRatio = 3f)).value)
                    .alpha(
                        animateFloatAsState(
                            targetValue = if (isExpanded) 1f else 0f,
                            animationSpec = tween(
                                durationMillis = if (isExpanded) 350 else 100,
                                delayMillis = if (isExpanded) 100 else 0,
                                easing = EaseIn)).value)
            )

        }
    }
}*/