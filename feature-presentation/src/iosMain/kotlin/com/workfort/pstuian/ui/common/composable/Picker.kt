package com.workfort.pstuian.ui.common.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.uikit.LocalUIViewController
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSData
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.NSURL
import platform.Foundation.NSUUID
import platform.Foundation.data
import platform.Foundation.writeToURL
import platform.UIKit.UIDocumentPickerDelegateProtocol
import platform.UIKit.UIDocumentPickerViewController
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIImagePickerControllerDelegateProtocol
import platform.UIKit.UIImagePickerControllerOriginalImage
import platform.UIKit.UIImagePickerControllerSourceType
import platform.UIKit.UINavigationControllerDelegateProtocol
import platform.UIKit.UIViewController
import platform.UniformTypeIdentifiers.UTTypePDF
import platform.darwin.NSObject

@Composable
actual fun rememberImagePickerLauncher(onImageSelected: (String) -> Unit): () -> Unit {
    val rootController = LocalUIViewController.current
    val delegate = remember(onImageSelected) {
        object : NSObject(), UIImagePickerControllerDelegateProtocol,
            UINavigationControllerDelegateProtocol {
            override fun imagePickerController(
                picker: UIImagePickerController,
                didFinishPickingMediaWithInfo: Map<Any?, *>,
            ) {
                try {
                    val image = didFinishPickingMediaWithInfo[UIImagePickerControllerOriginalImage] as? UIImage
                        ?: return@imagePickerController
                    val path = tempFilePath(suffix = ".jpg")
                    val fileUrl = NSURL.fileURLWithPath(path)
                    val jpeg = UIImageJPEGRepresentation(image, 0.92) ?: return@imagePickerController
                    if (!jpeg.writeToURL(fileUrl, true)) return@imagePickerController
                    fileUrl.absoluteString?.let { onImageSelected(it) }
                } finally {
                    picker.dismissViewControllerAnimated(true, null)
                }
            }

            override fun imagePickerControllerDidCancel(picker: UIImagePickerController) {
                picker.dismissViewControllerAnimated(true, null)
            }
        }
    }

    return remember(rootController, delegate) {
        {
            val sourceType = UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypePhotoLibrary
            if (UIImagePickerController.isSourceTypeAvailable(sourceType)) {
                val picker = UIImagePickerController()
                picker.sourceType = sourceType
                picker.allowsEditing = false
                picker.delegate = delegate
                rootController.topMostPresented().presentViewController(
                    picker,
                    animated = true,
                    completion = null,
                )
            }
        }
    }
}

@Composable
actual fun rememberPdfPickerLauncher(onPdfSelected: (String) -> Unit): () -> Unit {
    val rootController = LocalUIViewController.current
    val delegate = remember(onPdfSelected) {
        object : NSObject(), UIDocumentPickerDelegateProtocol {
            override fun documentPicker(
                controller: UIDocumentPickerViewController,
                didPickDocumentsAtURLs: List<*>,
            ) {
                try {
                    val url = didPickDocumentsAtURLs.firstOrNull() as? NSURL
                    url?.absoluteString?.let { onPdfSelected(it) }
                } finally {
                    controller.dismissViewControllerAnimated(true, null)
                }
            }

            override fun documentPickerWasCancelled(controller: UIDocumentPickerViewController) {
                controller.dismissViewControllerAnimated(true, null)
            }
        }
    }

    return remember(rootController, delegate) {
        {
            val picker = UIDocumentPickerViewController(
                forOpeningContentTypes = listOf(UTTypePDF),
                asCopy = true,
            )
            picker.delegate = delegate
            picker.allowsMultipleSelection = false
            rootController.topMostPresented().presentViewController(
                picker,
                animated = true,
                completion = null,
            )
        }
    }
}

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
@Composable
actual fun rememberPdfSaverLauncher(onPdfSelected: (String) -> Unit): (String) -> Unit {
    val rootController = LocalUIViewController.current
    val delegate = remember(onPdfSelected) {
        object : NSObject(), UIDocumentPickerDelegateProtocol {
            override fun documentPicker(
                controller: UIDocumentPickerViewController,
                didPickDocumentsAtURLs: List<*>,
            ) {
                try {
                    val url = didPickDocumentsAtURLs.firstOrNull() as? NSURL
                    url?.absoluteString?.let { onPdfSelected(it) }
                } finally {
                    controller.dismissViewControllerAnimated(true, null)
                }
            }

            override fun documentPickerWasCancelled(controller: UIDocumentPickerViewController) {
                controller.dismissViewControllerAnimated(true, null)
            }
        }
    }

    return remember(rootController, delegate) {
        { suggestedName: String ->
            val safeName = suggestedName.substringAfterLast('/')
                .ifEmpty { "document.pdf" }
                .let { n -> if (n.endsWith(".pdf", ignoreCase = true)) n else "$n.pdf" }
            val path = tempFilePath(baseName = safeName)
            val fileUrl = NSURL.fileURLWithPath(path)
            NSData.data().writeToURL(fileUrl, true)
            val picker = UIDocumentPickerViewController(
                forExportingURLs = listOf(fileUrl),
                asCopy = true,
            )
            picker.delegate = delegate
            rootController.topMostPresented().presentViewController(
                picker,
                animated = true,
                completion = null,
            )
        }
    }
}

private fun tempFilePath(baseName: String? = null, suffix: String = ""): String {
    val dir = NSTemporaryDirectory().trimEnd('/')
    val unique = NSUUID().UUIDString
    val name = when {
        baseName != null -> "${unique}_$baseName"
        suffix.isNotEmpty() -> unique + suffix
        else -> unique
    }
    return "$dir/$name"
}

private fun UIViewController.topMostPresented(): UIViewController {
    var top: UIViewController = this
    while (top.presentedViewController != null) {
        top = top.presentedViewController ?: break
    }
    return top
}
