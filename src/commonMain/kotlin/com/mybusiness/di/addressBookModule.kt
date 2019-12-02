package com.mybusiness.di

import com.mybusiness.api.ContactApi
import com.mybusiness.presentation.ContactCreationUpdatePresenter
import com.mybusiness.presentation.ContactDetailPresenter
import com.mybusiness.presentation.ContactListPresenter
import org.kodein.di.Kodein
import org.kodein.di.erased.*

val addressBookModule = Kodein.Module("addressBookModule") {
    bind() from singleton { ContactApi() }
    bind<Kodein>() with singleton { this.kodein }
    bind() from singleton { ContactListPresenter(instance()) }
    bind() from singleton { ContactDetailPresenter(instance()) }
    bind() from singleton { ContactCreationUpdatePresenter(instance()) }
}