package com.abysl.assetmanager.ui.components.shared.sidebar

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.abysl.assetmanager.services.NavigationService
import com.abysl.assetmanager.ui.components.Component
import com.abysl.assetmanager.ui.components.assetimport.AssetImportComponent
import com.abysl.assetmanager.ui.components.assetviewer.AssetViewerComponent
import com.abysl.assetmanager.ui.components.shared.settings.SettingsComponent
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class SideBarComponent: Component(), KoinComponent {

    private val navigationService by inject<NavigationService>()

    @Preview
    @Composable
    override fun view() {
        Column(
            modifier = Modifier.fillMaxWidth(0.2f)
        ) {
            NavigationButton("Assets") {
                navigationService.navigate(AssetViewerComponent())
            }
            NavigationButton("Settings") {
                navigationService.navigate(SettingsComponent())
            }
            NavigationButton("Import Assets") {
                navigationService.navigate(AssetImportComponent())
            }
        }
    }

    companion object {
        @Composable
        fun NavigationButton(text: String, onClick: () -> Unit){
            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth()
            ){
                Text(text)
            }
        }
    }
}