package com.workfort.pstuian.workmanager

import okhttp3.RequestBody
import okio.*
import java.io.IOException

typealias ProgressListener = (bytesWritten: Long, contentLength: Long) -> Unit

class ProgressRequestBody(
    private val requestBody: RequestBody,
    private val onProgressUpdate: ProgressListener
) : RequestBody() {
    override fun contentType() = requestBody.contentType()

    @Throws(IOException::class)
    override fun contentLength() = requestBody.contentLength()

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        val countingSink = ProgressSink(sink, this, onProgressUpdate)
        val bufferedSink = countingSink.buffer()

        requestBody.writeTo(bufferedSink)

        bufferedSink.flush()
    }
}

class ProgressSink(
    sink: Sink,
    private val requestBody: RequestBody,
    private val onProgressUpdate: ProgressListener
) : ForwardingSink(sink) {
    private var bytesWritten = 0L

    override fun write(source: Buffer, byteCount: Long) {
        super.write(source, byteCount)

        bytesWritten += byteCount
        onProgressUpdate(bytesWritten, requestBody.contentLength())
    }
}