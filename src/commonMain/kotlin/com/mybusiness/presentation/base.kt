package com.mybusiness.presentation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext
import kotlin.js.JsName

class PresenterCoroutineScope(
        context: CoroutineContext
) : CoroutineScope {

    private var cancellableJob = Job()
    override val coroutineContext: CoroutineContext = context + cancellableJob

    fun viewDetached() {
        cancellableJob.cancel()
    }
}

abstract class BasePresenter<T>(private val coroutineContext: CoroutineContext) {
    protected var view: T? = null; private set
    protected lateinit var scope: PresenterCoroutineScope

    @JsName("attachView")
    fun attachView(view: T) {
        this.view = view
        scope = PresenterCoroutineScope(coroutineContext)
        onViewAttached(view)
    }

    protected open fun onViewAttached(view: T) {}

    @JsName("detachView")
    fun detachView() {
        view = null
        scope.viewDetached()
    }
}