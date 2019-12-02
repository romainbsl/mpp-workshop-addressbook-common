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

class ContactList {
    interface Presenter
    interface View {
        @JsName("displayContactList")
        fun displayContactList(contactList: List<Contact>)
    }
}
class ContactListPresenter(
        override val kodein: Kodein,
        coroutineContext: CoroutineContext = ApplicationDispatcher
) : ContactList.Presenter, BasePresenter<ContactList.View>(coroutineContext), KodeinAware {
    private val contactApi: ContactApi by instance()

    override fun onViewAttached(view: ContactList.View) {

        scope.launch {
            val contactList = contactApi.getAllContacts()
            withContext(UIDispatcher) {
                view?.displayContactList(contactList)
            }
        }
    }
}