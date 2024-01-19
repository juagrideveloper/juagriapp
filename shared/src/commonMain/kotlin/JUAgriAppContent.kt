package com.juagri.shared

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import com.juagri.shared.com.juagri.shared.ui.navigation.AppInitNav
import org.jetbrains.compose.resources.ExperimentalResourceApi
import com.juagri.shared.com.juagri.shared.ui.theme.AppTheme

@OptIn(ExperimentalResourceApi::class)
@Composable
fun JUAgriAppContent() {
    AppTheme {
        /*Surface {
            MainContent()
        }*/
        /*var greetingText by remember { mutableStateOf("Hello, World!") }
        var showImage by remember { mutableStateOf(false) }
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = {
                greetingText = "Hello, ${getPlatformName()}"
                showImage = !showImage
            }) {
                Text(greetingText)
            }
            AnimatedVisibility(showImage) {
                Image(
                    //painterResource("compose-multiplatform.xml"),
                    painterResource("ic_launcher_logo.xml"),
                    null
                )
            }
        }*/
        //ListContent(onItemClick = {  })
        /*var loading by remember { mutableStateOf(false) }
        Button(onClick = { loading = true }, enabled = !loading) {
            Text("Start loading")
        }
        ProgressDialog(loading,{})*/


        //SplashScreen {}



        AppInitNav()

        /*Column(Modifier.fillMaxWidth()) {
            var list by remember { mutableStateOf(listOf<String>()) }
            LaunchedEffect(Unit) {
                list = getUsers()
            }
            LazyColumn {
                items(list) {
                    Text(
                        text = it,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
            }
        }*/
    }
}

suspend fun getUsers(): List<String> {
    val firebaseFirestore = Firebase.firestore
    try {
        val userResponse =
            firebaseFirestore.collection("AppMaster").get()
        return userResponse.documents.map {
            it.data()
        }
    } catch (e: Exception) {
       return listOf("UserApp - "+ e.message)
    }
    //return listOf("UserApp")
}

@Composable
fun UserItem(user: String) {
    Column {
        Text(
            text = user
        )
    }
}

@Composable
fun ListContent(onItemClick: (String) -> Unit) {
    val items = remember { List(100) { "Item $it" } }

    LazyColumn {
        items(items) { item ->
            Text(
                text = item,
                modifier = Modifier
                    .clickable { onItemClick(item) }
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}

@Composable
fun DetailsContent(text: String, onBack: () -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = text)

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onBack) {
            Text(text = "Back")
        }
    }
}