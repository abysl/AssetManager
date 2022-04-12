package com.abysl.assetmanager.ui.components.main

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.abysl.assetmanager.Prefs
import com.abysl.assetmanager.ui.components.Component
import com.abysl.assetmanager.ui.components.assetviewer.AssetViewerComponent
import com.abysl.assetmanager.ui.components.shared.sidebar.SideBarComponent
import com.abysl.assetmanager.ui.components.shared.topbar.TopBarComponent
import com.abysl.assetmanager.ui.util.Theme
import org.koin.core.component.KoinComponent

class MainComponent(val context: MainContext = MainContext()) : Component(), KoinComponent {

    private val topBar = TopBarComponent()
    private val sideBar = SideBarComponent()

    init {
        context.navService.navigate(AssetViewerComponent())
    }

    @Preview
    @Composable
    override fun view() {
            Scaffold(
                topBar = { TopAppBar { topBar.view() } },
            ) {
                Row(
                    modifier = Modifier.padding(15.dp),
                    horizontalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    if (topBar.context.menuPressed.value) {
                        sideBar.view()
                    }
                    context.navService.currentView()
                }
            }
        }
}