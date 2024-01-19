package com.juagri.shared.com.juagri.shared.ui.components.layouts

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun LoginLayout() {
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.TopStart
        ) {
            Image(
                painterResource("bg_splash_round.png"),
                null,
                modifier = Modifier.height(180.dp).width(140.dp).offset((-30).dp, (-40).dp)
            )
            Image(
                painterResource("bg_splash_round.png"),
                null,
                modifier = Modifier.height(160.dp).padding(start = 40.dp)
                    .offset((0).dp, (-50).dp)
            )
        }

        Box(
            modifier = Modifier.fillMaxWidth().weight(1f),
            contentAlignment = Alignment.BottomStart
        ) {
            Row {
                Image(
                    painterResource("bg_splash_round.png"),
                    null,
                    modifier = Modifier.height(180.dp).width(140.dp).offset((-30).dp, (40).dp)
                )
                Box(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    Image(
                        painterResource("bg_splash_round.png"),
                        null,
                        modifier = Modifier.height(160.dp).offset((20).dp, (20).dp),
                        alignment = Alignment.BottomEnd
                    )
                }
            }
        }
    }
}