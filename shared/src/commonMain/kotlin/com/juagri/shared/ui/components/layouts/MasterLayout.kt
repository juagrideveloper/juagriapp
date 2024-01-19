package com.juagri.shared.com.juagri.shared.ui.components.layouts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import kotlinx.coroutines.launch

@Composable
fun ScreenLayoutWithMenuActionBar(
    title: String = "",
    modifier: Modifier = Modifier,
    onBackPressed:(()-> Unit)? =null,
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
                        .fillMaxSize()
                        .padding(horizontal = 24.dp, vertical = 12.dp)
                ) {
                    ProfileCard()

                    /*Text("Drawer title", modifier = Modifier.padding(16.dp))
                    Divider()
                    NavigationDrawerItem(
                        label = { Text(text = "Drawer Item") },
                        selected = false,
                        onClick = { *//*TODO*//* }
                    )*/
                }
            }
        },
    ) {
        Scaffold(
            topBar = {
                ActionBarLayout(title) {
                    scope.launch {
                        drawerState.apply {
                            if (isClosed) open() else close()
                        }
                    }
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
    onBackPressed:(()-> Unit)? =null,
    content: @Composable() () -> Unit
) {
    Scaffold(
        topBar = {
            ActionBarLayout(title) { onBackPressed?.invoke() }
        }) { paddingValues ->
        Layout(
            modifier = modifier.padding(paddingValues).background(color = MaterialTheme.colorScheme.background),
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
    onBackPressed:(()-> Unit)? =null,
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