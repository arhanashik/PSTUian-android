package com.workfort.pstuian.util.remote.util

import okhttp3.RequestBody
import okio.*
import java.io.IOException

/**
 *  ****************************************************************************
 *  * Created by : arhan on 20 Oct, 2021 at 12:08 AM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 10/20/21.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

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