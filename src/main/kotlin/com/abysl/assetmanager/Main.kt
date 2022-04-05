package com.abysl.assetmanager

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.abysl.assetmanager.components.main.MainComponent
import com.abysl.assetmanager.services.DBService
import com.abysl.assetmanager.services.NavigationService
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module


fun main() {
    val mainModule = module {
        singleOf(::DBService)
        singleOf(::NavigationService)
    }
    startKoin {
        printLogger()
        modules(mainModule)
    }

    val mainComponent = MainComponent()

    application {
        val state = rememberWindowState()
        Window(
            title = "AbyslTCG",
            onCloseRequest = ::exitApplication,
            state = state,
        ) {
            mainComponent.view()
        }
    }
}