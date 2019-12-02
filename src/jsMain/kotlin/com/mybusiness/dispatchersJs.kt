package com.mybusiness

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

internal actual val ApplicationDispatcher: CoroutineContext = Dispatchers.Default
internal actual val UIDispatcher: CoroutineContext = Dispatchers.Main