package com.workfort.pstuian.util

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.COpaque
import kotlinx.cinterop.COpaquePointerVar
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.alloc
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.interpretCPointer
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.objcPtr
import kotlinx.cinterop.ptr
import kotlinx.cinterop.readBytes
import kotlinx.cinterop.set
import kotlinx.cinterop.usePinned
import kotlinx.cinterop.value
import platform.CoreFoundation.CFDataGetBytePtr
import platform.CoreFoundation.CFDataGetLength
import platform.CoreFoundation.CFDataRef
import platform.CoreFoundation.CFDictionaryCreate
import platform.CoreFoundation.CFRelease
import platform.CoreFoundation.CFStringCreateWithCString
import platform.CoreFoundation.CFStringRef
import platform.CoreFoundation.CFTypeRefVar
import platform.CoreFoundation.kCFBooleanTrue
import platform.CoreFoundation.kCFStringEncodingUTF8
import platform.Foundation.NSData
import platform.Foundation.create
import platform.Foundation.data
import platform.Security.SecItemAdd
import platform.Security.SecItemCopyMatching
import platform.Security.SecItemDelete
import platform.Security.errSecSuccess
import platform.Security.kSecAttrAccessible
import platform.Security.kSecAttrAccessibleAfterFirstUnlock
import platform.Security.kSecAttrAccount
import platform.Security.kSecAttrService
import platform.Security.kSecClass
import platform.Security.kSecClassGenericPassword
import platform.Security.kSecMatchLimit
import platform.Security.kSecMatchLimitOne
import platform.Security.kSecReturnData
import platform.Security.kSecValueData

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
object KeychainHelper {

    private const val SERVICE = "com.workfort.pstuian.deviceid"

    fun getString(key: String): String? = memScoped {
        val keyStr = key.toCFString()
        val serviceStr = SERVICE.toCFString()

        val keys = allocArray<COpaquePointerVar>(5)
        val values = allocArray<COpaquePointerVar>(5)

        keys[0] = kSecClass
        values[0] = kSecClassGenericPassword
        keys[1] = kSecAttrAccount
        values[1] = keyStr
        keys[2] = kSecAttrService
        values[2] = serviceStr
        keys[3] = kSecReturnData
        values[3] = kCFBooleanTrue
        keys[4] = kSecMatchLimit
        values[4] = kSecMatchLimitOne

        val query = CFDictionaryCreate(null, keys, values, 5, null, null)

        val result = alloc<CFTypeRefVar>()
        val status = SecItemCopyMatching(query, result.ptr)

        val output = if (status == errSecSuccess) {
            val cfData = result.value as? CFDataRef

            cfData?.let {
                val length = CFDataGetLength(it)
                val bytes = CFDataGetBytePtr(it)
                bytes?.readBytes(length.toInt())?.decodeToString()
            }
        } else {
            null
        }

        // Cleanup
        if (query != null) CFRelease(query)
        if (keyStr != null) CFRelease(keyStr)
        if (serviceStr != null) CFRelease(serviceStr)

        output
    }

    fun setString(key: String, value: String) = memScoped {
        val keyStr = key.toCFString()
        val serviceStr = SERVICE.toCFString()
        val data: NSData = value.encodeToByteArray().toNSData()

        // --- Delete existing ---
        val deleteKeys = allocArray<COpaquePointerVar>(3)
        val deleteValues = allocArray<COpaquePointerVar>(3)

        deleteKeys[0] = kSecClass
        deleteValues[0] = kSecClassGenericPassword
        deleteKeys[1] = kSecAttrAccount
        deleteValues[1] = keyStr
        deleteKeys[2] = kSecAttrService
        deleteValues[2] = serviceStr

        val deleteQuery = CFDictionaryCreate(null, deleteKeys, deleteValues, 3, null, null)
        SecItemDelete(deleteQuery)
        if (deleteQuery != null) CFRelease(deleteQuery)

        // --- Add new ---
        val keys = allocArray<COpaquePointerVar>(5)
        val values = allocArray<COpaquePointerVar>(5)

        keys[0] = kSecClass
        values[0] = kSecClassGenericPassword
        keys[1] = kSecAttrAccount
        values[1] = keyStr
        keys[2] = kSecAttrService
        values[2] = serviceStr
        keys[3] = kSecValueData
        values[3] = interpretCPointer<COpaque>(data.objcPtr())
        keys[4] = kSecAttrAccessible
        values[4] = kSecAttrAccessibleAfterFirstUnlock

        val attributes = CFDictionaryCreate(null, keys, values, 5, null, null)
        val status = SecItemAdd(attributes, null)

        if (status != errSecSuccess) {
            println("Keychain save failed: $status")
        }

        // Cleanup
        if (attributes != null) CFRelease(attributes)
        if (keyStr != null) CFRelease(keyStr)
        if (serviceStr != null) CFRelease(serviceStr)
    }

    // --- Helpers ---

    private fun String.toCFString(): CFStringRef? {
        return CFStringCreateWithCString(
            null,
            this,
            kCFStringEncodingUTF8
        )
    }
}

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
internal fun ByteArray.toNSData(): NSData {
    if (isEmpty()) return NSData.data()
    return usePinned { pinned ->
        NSData.create(
            bytes = pinned.addressOf(0),
            length = size.toULong()
        )
    }
}
