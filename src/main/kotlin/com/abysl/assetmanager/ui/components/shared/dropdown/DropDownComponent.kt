package com.abysl.assetmanager.ui.components.shared.dropdown

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.abysl.assetmanager.ui.components.Component
import com.abysl.assetmanager.ui.util.Theme

class DropDownComponent<C : Iterable<T>, T>(
    val items: C,
    val onItemSelect: (T) -> Unit = {},
    val fieldSize: Dp = 200.dp
) : Component() {

    var expanded by mutableStateOf(false)

    @Composable
    override fun view() {
        Column {
            var selectedItem by remember { mutableStateOf(items.first()) }
            button(selectedItem)
            menu { selectedItem = it }
        }
    }

    @Composable
    fun button(selectedItem: T?) {
        Button(
            onClick = { expanded = !expanded },
            modifier = Modifier
                .width(fieldSize)
        ) {
            Text(selectedItem.toString())
        }
    }

    @Composable
    fun menu(internalOnSelect: (T) -> Unit) {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(fieldSize)
                .border(2.dp, Theme.cyan)
        ) {
            for (item in items) {
                DropdownMenuItem(onClick = {
                    println(item)
                    onItemSelect(item)
                    internalOnSelect(item)
                    expanded = false
                }) {
                    Text(item.toString())
                }
            }
        }
    }


}