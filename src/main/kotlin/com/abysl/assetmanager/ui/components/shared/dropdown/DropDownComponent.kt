package com.abysl.assetmanager.ui.components.shared.dropdown

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.abysl.assetmanager.ui.components.Component
import com.abysl.assetmanager.ui.util.Theme

class DropDownComponent<C: Iterable<T>, T>(
    val items: C,
    val onItemSelect: (T) -> Unit = {},
    val fieldSize: Dp = 200.dp
) : Component() {

    var expanded by mutableStateOf(false)
    var selectedItem by mutableStateOf(items.firstOrNull())

    @Composable
    override fun view() {
        Column {
            button()
            menu()
        }
    }

    @Composable
    fun button() {
        Button(
            onClick = { expanded = !expanded },
            modifier = Modifier
                .width(fieldSize)
        ) {
                Text(selectedItem.toString())
        }
    }

    @Composable
    fun menu() {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(fieldSize)
                .border(2.dp, Theme.cyan)
        ) {
            for (item in items) {
                DropdownMenuItem(onClick = {
                    onItemSelect(item)
                    selectedItem = item
                    expanded = false
                }) {
                    Text(item.toString())
                }
            }
        }
    }


}