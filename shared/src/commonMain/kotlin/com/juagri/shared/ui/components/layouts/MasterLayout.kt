package com.juagri.shared.ui.components.layouts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.router.stack.replaceCurrent
import com.juagri.shared.com.juagri.shared.ui.navigation.AppScreens
import com.juagri.shared.domain.model.employee.JUEmployee
import com.juagri.shared.ui.components.fields.NavDrawerContent
import com.juagri.shared.ui.components.fields.NavDrawerHeading
import io.github.xxfast.decompose.router.Router
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ScreenLayoutWithMenuActionBar(
    title: String = "",
    modifier: Modifier = Modifier,
    onBackPressed: (() -> Unit)? = null,
    router: Router<AppScreens>? = null,
    content: @Composable() () -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val gradient = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.secondary
        )
    )
    ModalNavigationDrawer(
        modifier = Modifier.background(gradient),
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier
                        .background(gradient)
                        .then(Modifier)
                        .fillMaxHeight()
                        .width(300.dp)
                ) {
                    DrawerLayout(
                        employee = JUEmployee(
                            active = true,
                            mgmt = false,
                            admin = false,
                            code = "TN-0001",
                            hrCode = "TN-0001",
                            mobile = "9578080988",
                            mailId = "kesavanpanneerselvam@gmail.com",
                            name = "Kesavan Panneerselvam",
                            role = "Manager",
                            roleId = "RM",
                            fcmid = "",
                            regionCode = "Reg-TN-001",
                            territoryCode = ""
                        )
                    )
                    Divider(modifier = Modifier.height(2.dp))
                    NavDrawerHeading("Menu")
                    NavDrawerContent("Dashboard") {
                        updateDrawerState(scope,drawerState)
                        router?.replaceCurrent(AppScreens.Dashboard)
                    }
                    NavDrawerContent("Customer Ledger") {
                        updateDrawerState(scope,drawerState)
                        router?.replaceCurrent(AppScreens.Ledger)
                    }
                    NavDrawerHeading("Services")
                    NavDrawerContent("Weather") {
                        updateDrawerState(scope,drawerState)
                        router?.replaceCurrent(AppScreens.Weather)
                    }
                    NavDrawerHeading("Personal")
                    NavDrawerContent("Profile") {
                        updateDrawerState(scope,drawerState)
                        router?.replaceCurrent(AppScreens.Profile)
                    }

                }
            }
        },
    ) {
        Scaffold(
            topBar = {
                ActionBarLayout(title) {
                    updateDrawerState(scope,drawerState)
                    /*scope.launch {
                        drawerState.apply {
                            if (isClosed) open() else close()
                        }
                    }*/
                }
            }) { paddingValues ->
            Layout(
                modifier = modifier.padding(paddingValues)
                    .background(color = MaterialTheme.colorScheme.background),
                content = content
            ) { measurables, constraints ->
                // Don't constrain child views further, measure them with given constraints
                // List of measured children
                val placeables = measurables.map { measurable ->
                    // Measure each children
                    measurable.measure(constraints)
                }

                // Set the size of the layout as big as it can
                layout(constraints.maxWidth, constraints.maxHeight) {
                    // Track the y co-ord we have placed children up to
                    var yPosition = 0

                    // Place children in the parent layout
                    placeables.forEach { placeable ->
                        // Position item on the screen
                        placeable.placeRelative(x = 0, y = yPosition)

                        // Record the y co-ord placed up to
                        yPosition += placeable.height
                    }
                }
            }
        }
    }
}

@Composable
fun ScreenLayoutWithActionBar(
    title: String = "",
    modifier: Modifier = Modifier,
    onBackPressed: (() -> Unit)? = null,
    content: @Composable() () -> Unit
) {
    Scaffold(
        topBar = {
            ActionBarLayout(title) { onBackPressed?.invoke() }
        }) { paddingValues ->
        Layout(
            modifier = modifier.padding(paddingValues)
                .background(color = MaterialTheme.colorScheme.background),
            content = content
        ) { measurables, constraints ->
            // Don't constrain child views further, measure them with given constraints
            // List of measured children
            val placeables = measurables.map { measurable ->
                // Measure each children
                measurable.measure(constraints)
            }

            // Set the size of the layout as big as it can
            layout(constraints.maxWidth, constraints.maxHeight) {
                // Track the y co-ord we have placed children up to
                var yPosition = 0

                // Place children in the parent layout
                placeables.forEach { placeable ->
                    // Position item on the screen
                    placeable.placeRelative(x = 0, y = yPosition)

                    // Record the y co-ord placed up to
                    yPosition += placeable.height
                }
            }
        }
    }
}

@Composable
fun ScreenLayoutWithoutActionBar(
    title: String = "",
    modifier: Modifier = Modifier,
    onBackPressed: (() -> Unit)? = null,
    content: @Composable() () -> Unit
) {
    Layout(
        modifier = modifier.background(color = MaterialTheme.colorScheme.background),
        content = content
    ) { measurables, constraints ->
        // Don't constrain child views further, measure them with given constraints
        // List of measured children
        val placeables = measurables.map { measurable ->
            // Measure each children
            measurable.measure(constraints)
        }

        // Set the size of the layout as big as it can
        layout(constraints.maxWidth, constraints.maxHeight) {
            // Track the y co-ord we have placed children up to
            var yPosition = 0

            // Place children in the parent layout
            placeables.forEach { placeable ->
                // Position item on the screen
                placeable.placeRelative(x = 0, y = yPosition)

                // Record the y co-ord placed up to
                yPosition += placeable.height
            }
        }
    }
}

private fun updateDrawerState(scope: CoroutineScope,drawerState: DrawerState){
    scope.launch {
        drawerState.apply {
            if (isClosed) open() else close()
        }
    }
}