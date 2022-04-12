package com.abysl.assetmanager.ui.components.shared.topbar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.abysl.assetmanager.services.NavigationService
import com.abysl.assetmanager.ui.components.Component
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class TopBarComponent(val context: TopBarContext = TopBarContext()) : Component(), KoinComponent {

    private val nav by inject<NavigationService>()

    @Composable
    override fun view() {
        bar()
    }

    @Composable
    fun bar() {
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            IconButton(onClick = { context.menuPressed.value = !context.menuPressed.value }) {
                Icon(
                    Icons.Filled.Menu,
                    contentDescription = "Menu"
                )
            }
            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                IconButton(onClick = { nav.back() }) {
                    Icon(
                        Icons.Filled.ArrowBack,
                        contentDescription = "Backward"
                    )
                }
                IconButton(onClick = { nav.forward() }) {
                    Icon(
                        Icons.Filled.ArrowForward,
                        contentDescription = "Forward"
                    )
                }
            }
        }
    }
}