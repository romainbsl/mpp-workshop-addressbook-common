package com.mybusiness.di

import com.mybusiness.api.ContactApi
import com.mybusiness.presentation.ContactCreationUpdatePresenter
import com.mybusiness.presentation.ContactDetailPresenter
import com.mybusiness.presentation.ContactListPresenter
import kotlin.native.concurrent.ThreadLocal

@ThreadLocal
object CommonInjector {
    //    API
    private val api: ContactApi by lazy {
        ContactApi()
    }
    //    Presenters
    fun contactListPresenter() = ContactListPresenter(api)
    fun contactDetailPresenter() = ContactDetailPresenter(api)
    fun contactCreationUpdatePresenter() = ContactCreationUpdatePresenter(api)
}