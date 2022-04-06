package com.abysl.assetmanager.ui.components.shared.navigationbar

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.abysl.assetmanager.ui.components.Component
import com.abysl.assetmanager.services.NavigationService
import com.abysl.assetmanager.ui.components.assetviewer.AssetViewerComponent
import com.abysl.assetmanager.ui.components.assetviewer.AssetViewerContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class NavigationBar: Component(), KoinComponent {

    private val navigationService by inject<NavigationService>()

    @Preview
    @Composable
    override fun view() {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Cyan)
        ) {
            NavigationButton("Backward"){
                navigationService.back()
            }

            NavigationButton("Assets"){
                navigationService.navigate(AssetViewerComponent())
            }

            NavigationButton("Assets"){
                navigationService.navigate(AssetViewerComponent())
            }

            NavigationButton("Forward"){
                navigationService.forward()
            }

        }
    }

    companion object {
        @Composable
        fun NavigationButton(text: String, onClick: () -> Unit){
            Button(
                onClick = onClick,
                modifier = Modifier
                    .requiredWidth(125.dp)
            ){
                Text(text)
            }
        }
    }
}