package com.mybusiness.presentation

import com.mybusiness.ApplicationDispatcher
import com.mybusiness.UIDispatcher
import com.mybusiness.api.ContactApi
import com.mybusiness.model.Contact
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
        private val contactApi: ContactApi,
        coroutineContext: CoroutineContext = ApplicationDispatcher
) : ContactCreationUpdate.Presenter, BasePresenter<ContactCreationUpdate.View>(coroutineContext) {
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