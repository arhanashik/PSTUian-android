package com.workfort.pstuian.common.component

import androidx.compose.runtime.Composable

@Composable
expect fun rememberImagePickerLauncher(onImageSelected: (String) -> Unit): () -> Unit

@Composable
expect fun rememberPdfPickerLauncher(onPdfSelected: (String) -> Unit): () -> Unit

@Composable
expect fun rememberPdfSaverLauncher(onPdfSelected: (String) -> Unit): (String) -> Unit
