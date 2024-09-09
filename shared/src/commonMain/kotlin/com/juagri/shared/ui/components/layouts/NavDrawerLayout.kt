package com.juagri.shared.ui.components.layouts

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.juagri.shared.domain.model.employee.JUEmployee
import com.juagri.shared.ui.components.fields.ColumnSpaceMedium
import com.juagri.shared.ui.components.fields.ColumnSpaceSmall
import com.juagri.shared.ui.components.fields.NavDrawerRole
import com.juagri.shared.ui.components.fields.NavDrawerUsername
import com.juagri.shared.ui.components.fields.RowSpaceMedium
import com.juagri.shared.utils.value
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun DrawerLayout(
    modifier: Modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
    employee: JUEmployee
) {
    Column(
        modifier=modifier.fillMaxWidth().wrapContentHeight(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ColumnSpaceMedium()
        /*Image(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape),
            painter = painterResource(DrawableResource("ic_user.xml")),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center
        )*/
        ProfileImageLayout(
            employee.mobile.value(),
            Modifier
                .height(90.dp)
                .width(90.dp)
                .shadow(elevation = 8.dp, RoundedCornerShape(45.dp))
                .background(Color.White, RoundedCornerShape(45.dp))
                .clip(RoundedCornerShape(8.dp))
        )
        ColumnSpaceMedium()
        NavDrawerUsername(employee.name.value())
        ColumnSpaceSmall()
        NavDrawerRole(employee.role.value())
    }
}