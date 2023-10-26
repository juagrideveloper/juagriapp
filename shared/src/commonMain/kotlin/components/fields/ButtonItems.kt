package components.fields

import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import theme.text

@Composable
fun ButtonSmall(title: String,onClicked: () -> Unit) = Button(onClick = onClicked) {
    MediumText(title)
}