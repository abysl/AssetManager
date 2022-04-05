package com.abysl.assetmanager.components.game

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.abysl.assetmanager.components.Component

class GameScreen(): Component() {

    @Composable
    override fun view() {
        Text("Game Screen")
    }
}