package com.abysl.assetmanager.ui.util

import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

object Theme {

    val cyan = Color(0xFF39ace7)
    val cyanVariant = Color(0xFF0784b5)
    val purple = Color(0xFF9518e2)
    val purpleVariant = Color(0xFF6c13a4)
    val lightGray = Color(0xFF414c50)
    val gray = Color(0xFF2d383c)
    val darkGray = Color(0xFF192428)

    val darkMaterial = darkColors(
        primary = cyan,
        primaryVariant = cyanVariant,
        secondary = purple,
        secondaryVariant = purpleVariant,
        background = gray,
        surface = lightGray,
        error = Color.Red,
        onPrimary = darkGray,
        onSecondary = darkGray,
        onBackground = Color.White,
        onSurface = Color.White,
        onError = Color.White
    )

    val lightMaterial = lightColors(
        primary = cyan,
        primaryVariant = cyanVariant,
        secondary = purple,
        secondaryVariant = purpleVariant,
    )
}