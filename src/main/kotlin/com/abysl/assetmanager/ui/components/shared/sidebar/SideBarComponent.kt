package com.abysl.assetmanager.ui.components.shared.sidebar

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
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
            modifier = Modifier
                .fillMaxWidth(0.25f).fillMaxHeight()
                .background(MaterialTheme.colors.surface)
                .padding(15.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            NavigationButton("Assets") {
                navigationService.navigate(AssetViewerComponent())
            }
            NavigationButton("Import Assets") {
                navigationService.navigate(AssetImportComponent())
            }
            NavigationButton("Settings") {
                navigationService.navigate(SettingsComponent())
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