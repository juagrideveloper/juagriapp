package com.juagri.shared.ui.components.layouts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.router.stack.popTo
import com.arkivanov.decompose.router.stack.push
import com.juagri.shared.domain.model.employee.JUEmployee
import com.juagri.shared.ui.components.base.BaseViewModel
import com.juagri.shared.ui.components.fields.NavDrawerContent
import com.juagri.shared.ui.components.fields.NavDrawerHeading
import com.juagri.shared.ui.navigation.AppScreens
import com.juagri.shared.utils.PermissionUtils
import com.juagri.shared.utils.getColors
import com.juagri.shared.utils.value
import io.github.xxfast.decompose.router.Router
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

data class PermissionData(val isNeeded: Boolean = false, val screenId: String = "")

@Composable
fun ScreenLayoutWithMenuActionBar(
    title: MutableState<String> = mutableStateOf(""),
    employee: MutableState<JUEmployee>,
    modifier: Modifier = Modifier,
    viewModel: BaseViewModel,
    onBackPressed: (() -> Unit)? = null,
    router: Router<AppScreens>? = null,
    content: @Composable() () -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val gradient = Brush.verticalGradient(
        colors = listOf(
            getColors().primary,
            getColors().secondary
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
                        .verticalScroll(rememberScrollState())
                        .width(300.dp)
                ) {
                    DrawerLayout(
                        employee = employee.value.copy(mobile = viewModel.empMobile())
                    )
                    Divider(modifier = Modifier.height(2.dp))

                    if(employee.value.menuItems != null){
                        employee.value.menuItems?.forEach {parent->
                            NavDrawerHeading(viewModel.getScreenName(parent.value.id.value()))
                            parent.value.childMenus.forEach {child->
                                val screenId = child.id.value()
                                NavDrawerContent(viewModel.getScreenName(screenId)) {
                                    updateDrawerState(scope,drawerState)
                                    if(screenId == Constants.SCREEN_PROMOTION_ENTRY || screenId == Constants.SCREEN_PROMOTION_ENTRIES_LIST){
                                        viewModel.locationPermissionNeeded.value = PermissionData(true,screenId)
                                    }else{
                                        processNavigation(viewModel, screenId, router)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },
    ) {
        Scaffold(
            topBar = {
                ActionBarLayout(title,Icons.Default.Menu,viewModel) {
                    updateDrawerState(scope,drawerState)
                    onBackPressed?.invoke()
                }
            }) { paddingValues ->
            Layout(
                modifier = modifier.padding(paddingValues)
                    .background(color = getColors().background),
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
    if(viewModel.locationPermissionNeeded.value.isNeeded) {
        PermissionUtils.LocationPermission { permissionGranted ->
            if (permissionGranted) {
                processNavigation(viewModel, viewModel.locationPermissionNeeded.value.screenId, router)
            } else {
                viewModel.showErrorMessage("Location permission needed!")
            }
        }
        viewModel.locationPermissionNeeded.value = PermissionData()
    }
}

private fun processNavigation(viewModel: BaseViewModel,screenId: String, router: Router<AppScreens>?){
    viewModel.writeLog("-----------------------")
    val screen = viewModel.getScreenMode(screenId)
    var notPoped = true
    router?.stack?.value?.items?.forEachIndexed { index, item ->
        if(screen == item.configuration) {
            router.popTo(index)
            notPoped = false
        }
    }
    if(notPoped) {
        router?.push(viewModel.getScreenMode(screenId))
    }
}

@Composable
fun ScreenLayoutWithActionBar(
    title: MutableState<String> = mutableStateOf(""),
    modifier: Modifier = Modifier,
    viewModel: BaseViewModel,
    onBackPressed: (() -> Unit)? = null,
    content: @Composable() () -> Unit
) {
    Scaffold(
        topBar = {
            ActionBarLayout(title,Icons.Default.ArrowBack, viewModel) { onBackPressed?.invoke() }
        }) { paddingValues ->
        Layout(
            modifier = modifier.padding(paddingValues)
                .background(color = getColors().background),
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
        modifier = modifier.background(color = getColors().background),
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