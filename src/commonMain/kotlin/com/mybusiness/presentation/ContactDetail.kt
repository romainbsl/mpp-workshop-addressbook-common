package com.mybusiness.presentation

import com.mybusiness.ApplicationDispatcher
import com.mybusiness.UIDispatcher
import com.mybusiness.api.ContactApi
import com.mybusiness.model.Contact
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.js.JsName

class ContactDetail {
    interface Presenter {
        @JsName("getContact")
        fun getContact(contactId: String)
    }
    interface View {
        @JsName("displayContact")
        fun displayContact(contact: Contact)
    }
}
class ContactDetailPresenter(
        private val contactApi: ContactApi,
        coroutineContext: CoroutineContext = ApplicationDispatcher
) : ContactDetail.Presenter, BasePresenter<ContactDetail.View>(coroutineContext) {
    override fun getContact(contactId: String) {
        scope.launch {
            val contact = contactApi.getContactById(contactId)
            withContext(UIDispatcher) {
                view?.displayContact(contact)
            }
        }
    }
}