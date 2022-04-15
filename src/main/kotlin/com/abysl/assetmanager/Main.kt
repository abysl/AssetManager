package com.abysl.assetmanager

import androidx.compose.material.MaterialTheme
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.abysl.assetmanager.services.DBService
import com.abysl.assetmanager.services.DownloadService
import com.abysl.assetmanager.services.ImageService
import com.abysl.assetmanager.services.NavigationService
import com.abysl.assetmanager.services.AssetImportService
import com.abysl.assetmanager.ui.components.main.MainComponent
import com.abysl.assetmanager.ui.util.Theme
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import mu.KotlinLogging
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import java.awt.Dimension


fun main() {
    val db = DBService()
    val downloadService = DownloadService().also { it.start() }
    val mainModule = module {
        single { db }
        single { ImageService(cacheLocation = Prefs.IMAGE_CACHE) }
        single { downloadService }
        single { HttpClient(CIO) }
        single { KotlinLogging.logger {} }
        singleOf(::NavigationService)
        singleOf(::AssetImportService)

    }
    startKoin {
        modules(mainModule)
    }

    val mainComponent = MainComponent()

    application {
        val state = rememberWindowState()
        MaterialTheme(colors = if (Prefs.darkMode) Theme.darkMaterial else Theme.lightMaterial) {
            Window(
                title = "Abysl Asset Manager",
                onCloseRequest = ::exitApplication,
                state = state,
            ) {
                this.window.size = Dimension(1440, 900)
                mainComponent.view()
            }

        }
    }
}