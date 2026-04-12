package com.workfort.pstuian.view.ui.common.component

import androidx.compose.runtime.Composable

@Composable
actual fun rememberImagePickerLauncher(onImageSelected: (String) -> Unit): () -> Unit {
    return {}
}

@Composable
actual fun rememberPdfPickerLauncher(onPdfSelected: (String) -> Unit): () -> Unit {
    return {}
}
