package com.abysl.assetmanager.ui.components.main

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import com.abysl.assetmanager.ui.components.Component
import com.abysl.assetmanager.ui.components.assetviewer.AssetViewerComponent
import com.abysl.assetmanager.ui.components.shared.navigationbar.NavigationBar
import org.koin.core.component.KoinComponent

class MainComponent(val context: MainContext = MainContext()) : Component(), KoinComponent {
    private val navigationBar = NavigationBar()


    init {
        context.navService.navigate(AssetViewerComponent())
    }

    @Preview
    @Composable
    override fun view() {
        MaterialTheme {
            Column {
                navigationBar.view()
                context.navService.currentView()
            }
        }
    }
}