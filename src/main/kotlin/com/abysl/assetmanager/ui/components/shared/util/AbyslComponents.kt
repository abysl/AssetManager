package com.abysl.assetmanager.ui.components.shared.util

import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

object AbyslComponents {

    @Composable
    fun LinkText(text: String, link: String){
        val uriHandler = LocalUriHandler.current

        val annotatedText = buildAnnotatedString {
            withStyle(style = SpanStyle(color = MaterialTheme.colors.primary)) {
                append(text)
            }
        }

        ClickableText(text = annotatedText) {
            uriHandler.openUri(link)
        }
    }
}