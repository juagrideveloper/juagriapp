package com.juagri.shared.ui.home

import androidx.compose.runtime.Composable
import com.juagri.shared.com.juagri.shared.ui.components.layouts.ScreenLayoutWithMenuActionBar
import moe.tlaster.precompose.koin.koinViewModel

@Composable
fun HomeScreen(onBack: () -> Unit) {
    val viewModel = koinViewModel(HomeViewModel::class)
    ScreenLayoutWithMenuActionBar(title = "Dashboard") {
        //viewModel.localSession.put("123456","ABCDEFGH")

        viewModel.dataManager.menuItems().forEach {
            viewModel.writeLog(it.menuName)
        }
    }
    /*val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
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
                    Text("Drawer title", modifier = Modifier.padding(16.dp))
                    Divider()
                    NavigationDrawerItem(
                        label = { Text(text = "Drawer Item") },
                        selected = false,
                        onClick = { *//*TODO*//* }
                    )
                }
            }
        },
    ) {
        Scaffold(
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    text = { Text("Show drawer") },
                    icon = { Icon(Icons.Filled.Add, contentDescription = "") },
                    onClick = {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    }
                )
            }
        ) { contentPadding ->
            // Screen content
        }
    }*/
}