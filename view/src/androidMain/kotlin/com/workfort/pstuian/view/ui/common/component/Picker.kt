package com.workfort.pstuian.view.ui.common.component

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable

@Composable
actual fun rememberImagePickerLauncher(onImageSelected: (String) -> Unit): () -> Unit {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let { onImageSelected(it.toString()) }
    }
    return {
        launcher.launch(
            PickVisualMediaRequest(
                mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly,
            ),
        )
    }
}

@Composable
actual fun rememberPdfPickerLauncher(onPdfSelected: (String) -> Unit): () -> Unit {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let { onPdfSelected(it.toString()) }
    }
    return {
        launcher.launch(arrayOf("application/pdf"))
    }
}

@Composable
actual fun rememberPdfSaverLauncher(onPdfSelected: (String) -> Unit): (String) -> Unit {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument(mimeType = "application/pdf")
    ) { uri ->
        uri?.let { onPdfSelected(it.toString()) }
    }
    return { fileName ->
        launcher.launch(fileName)
    }
}
