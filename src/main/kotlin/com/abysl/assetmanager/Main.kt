package com.abysl.assetmanager

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.abysl.assetmanager.ui.components.main.MainComponent
import com.abysl.assetmanager.services.DBService
import com.abysl.assetmanager.services.NavigationService
import com.abysl.assetmanager.services.ImageService
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import java.awt.Dimension


fun main() {
    val db = DBService()
    val mainModule = module {
        single { db }
        single { ImageService(cacheLocation = Prefs.IMAGE_CACHE) }
        singleOf(::NavigationService)

    }
    startKoin {
//        printLogger()
        modules(mainModule)
    }

    val mainComponent = MainComponent()
    println(Prefs.itchApiKey)

    application {
        val state = rememberWindowState()
        Window(
            title = "AbyslTCG",
            onCloseRequest = ::exitApplication,
            state = state,
        ) {
            this.window.size = Dimension(1440, 900)
            mainComponent.view()
        }
    }
}