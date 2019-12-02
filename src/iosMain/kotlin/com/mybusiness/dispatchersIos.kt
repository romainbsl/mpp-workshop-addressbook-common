package com.mybusiness

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Runnable
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue
import kotlin.coroutines.CoroutineContext

internal actual val ApplicationDispatcher: CoroutineContext = IosMainDispatcher()
internal actual val UIDispatcher: CoroutineContext = IosMainDispatcher()

internal class IosMainDispatcher() : CoroutineDispatcher() {
    override fun dispatch(context: CoroutineContext, block: Runnable) {
        dispatch_async(dispatch_get_main_queue()) { block.run() }
    }
}