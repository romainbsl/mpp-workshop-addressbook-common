package com.mybusiness.presentation

import com.mybusiness.ApplicationDispatcher
import com.mybusiness.UIDispatcher
import com.mybusiness.api.ContactApi
import com.mybusiness.model.Contact
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.js.JsName

class ContactList {
    interface Presenter
    interface View {
        @JsName("displayContactList")
        fun displayContactList(contactList: List<Contact>)
    }
}
class ContactListPresenter(
        private val contactApi: ContactApi,
        coroutineContext: CoroutineContext = ApplicationDispatcher
) : ContactList.Presenter, BasePresenter<ContactList.View>(coroutineContext) {
    override fun onViewAttached(view: ContactList.View) {
        scope.launch {
            val contactList = contactApi.getAllContacts()
            withContext(UIDispatcher) {
                view?.displayContactList(contactList)
            }
        }
    }
}