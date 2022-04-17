package com.abysl.assetmanager.util

import androidx.compose.runtime.Composable
import com.abysl.assetmanager.Prefs
import java.io.File
import java.io.InputStream
import javax.swing.JFileChooser
import javax.swing.filechooser.FileSystemView


fun String.asResourceStream(): InputStream =
    object {}.javaClass.classLoader.getResourceAsStream(this) ?: throw ResourceLoadException(this)

class ResourceLoadException(resource: String) : Exception("Failed to load resource: $resource")


val fileNameRegex = """(?:.+\/)([^#?&]+)""".toRegex()
fun parseFileNameFromUrl(url: String): String {
    val test = fileNameRegex.find(url)?.groupValues?.last() ?: "unknown_filename"
    return test
}

@Composable
fun folderDialog(
    title: String = "Select a folder",
    mode: Int = JFileChooser.DIRECTORIES_ONLY,
    onResult: (result: File?) -> Unit,
) {
    val fileChooser = JFileChooser(FileSystemView.getFileSystemView())
    fileChooser.currentDirectory = Prefs.USER_FOLDER
    fileChooser.dialogTitle = title
    fileChooser.fileSelectionMode = mode
    fileChooser.isAcceptAllFileFilterUsed = true
    fileChooser.selectedFile = null
    fileChooser.currentDirectory = null
    if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
        onResult(fileChooser.selectedFile)
    } else {
        onResult(null)
    }
}