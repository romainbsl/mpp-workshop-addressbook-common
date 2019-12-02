package com.mybusiness.presentation

import com.mybusiness.ApplicationDispatcher
import com.mybusiness.UIDispatcher
import com.mybusiness.api.ContactApi
import com.mybusiness.model.Contact
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.erased.instance
import kotlin.coroutines.CoroutineContext
import kotlin.js.JsName

class ContactCreationUpdate {
    interface Presenter {
        @JsName("updateOrCreateContact")
        fun updateOrCreateContact(contact: Contact)
    }
    interface View {
        @JsName("updateOrCreationSucceed")
        fun updateOrCreationSucceed()
        @JsName("updateOrCreationFails")
        fun updateOrCreationFails()
    }
}
class ContactCreationUpdatePresenter(
        override val kodein: Kodein,
        coroutineContext: CoroutineContext = ApplicationDispatcher
) : ContactCreationUpdate.Presenter, BasePresenter<ContactCreationUpdate.View>(coroutineContext), KodeinAware {

    private val contactApi: ContactApi by instance()

    override fun updateOrCreateContact(contact: Contact) {
        scope.launch {
            try {
                if (contact.id == "-1") contactApi.putContact(contact)
                else contactApi.postContact(contact)

                withContext(UIDispatcher) { view?.updateOrCreationSucceed() }
            } catch (e: Exception) {
                withContext(UIDispatcher) { view?.updateOrCreationFails() }
            }
        }
    }
}