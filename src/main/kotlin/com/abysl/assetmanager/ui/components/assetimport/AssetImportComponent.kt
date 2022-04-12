package com.abysl.assetmanager.ui.components.assetimport

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.abysl.assetmanager.ui.components.Component
import com.abysl.assetmanager.ui.components.shared.dropdown.DropDownComponent

class AssetImportComponent(val ctx: AssetImportContext = AssetImportContext()) : Component() {

    @Composable
    override fun view() {
        Column {
            importSelector()
            when(ctx.selectedImportType){
                ImportTypes.ITCH -> itchImportForm()
                ImportTypes.HUMBLE -> humbleImportForm()
                ImportTypes.LOCAL -> localImportForm()
            }
        }
    }

    @Composable
    fun importSelector() {
        DropDownComponent(
            items = ImportTypes.values().asIterable(),
            onItemSelect = { ctx.selectedImportType = it },
            fieldSize = FIELD_SIZE
        ).view()
    }

    @Composable
    fun itchImportForm(){
        Text("Itch")
    }

    @Composable
    fun humbleImportForm(){
        Text("Humble")
    }

    @Composable
    fun localImportForm(){
        Text("Local")
    }

    companion object {
        private val FIELD_SIZE = 200.dp;
        private val BORDER_SIZE = 2.dp
    }
}