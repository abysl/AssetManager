package com.abysl.assetmanager.ui.components.main

import com.abysl.assetmanager.services.NavigationService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainContext: KoinComponent {

    val navService by inject<NavigationService>()
}