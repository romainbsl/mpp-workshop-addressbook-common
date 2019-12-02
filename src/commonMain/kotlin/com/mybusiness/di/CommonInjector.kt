package com.mybusiness.di

import com.mybusiness.api.ContactApi
import com.mybusiness.presentation.ContactCreationUpdatePresenter
import com.mybusiness.presentation.ContactDetailPresenter
import com.mybusiness.presentation.ContactListPresenter
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.erased.instance
import kotlin.native.concurrent.ThreadLocal

@ThreadLocal
object CommonInjector {
    val kodein = Kodein.lazy {
        import(addressBookModule)
    }

    //    Presenters
    fun contactListPresenter(): ContactListPresenter {
        val presenter = kodein.direct.instance<ContactListPresenter>()
        println("ContactListPresenter instance: $presenter")
        return presenter
    }
    fun contactDetailPresenter() = kodein.direct.instance<ContactDetailPresenter>()
    fun contactCreationUpdatePresenter() = kodein.direct.instance<ContactCreationUpdatePresenter>()
}