package com.mybusiness.api

import com.mybusiness.model.Contact
import io.ktor.client.HttpClient
import io.ktor.client.call.call
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.put
import io.ktor.content.TextContent
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLProtocol
import kotlinx.serialization.internal.StringSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.list
import kotlinx.serialization.map

const val LOCALHOST = "127.0.0.1"
expect fun apiBaseUrl(): String

class ContactApi {
    private val client = HttpClient {
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
    }
    private fun HttpRequestBuilder.apiUrl(path: String = "/") {
        url {
            host = apiBaseUrl()
            port = 8042
            protocol = URLProtocol.HTTP
            encodedPath = "/api/contacts$path"
        }
    }
    suspend fun getAllContacts(): List<Contact> {
        return Json.nonstrict.parse(
                Contact.serializer().list,
                client.get {
                    apiUrl()
                }
        )
    }
    suspend fun getContactById(contactId: String): Contact {
        return Json.nonstrict.parse(
                Contact.serializer(),
                client.get {
                    apiUrl("/$contactId")
                }
        )
    }
    suspend fun putContact(contact: Contact): String {
        return Json.nonstrict.parse(
                (StringSerializer to StringSerializer).map,
                client.put {
                    apiUrl()
                    method = HttpMethod.Put
                    body = TextContent(Json.stringify(Contact.serializer(), contact),
                            contentType = ContentType.Application.Json)
                }
        ).values.first()
    }
    suspend fun postContact(contact: Contact): Boolean {
        val response = client.call {
            apiUrl("/${contact.id}")
            method = HttpMethod.Post
            body = TextContent(Json.stringify(Contact.serializer(), contact),
                    contentType = ContentType.Application.Json)
        }.response

        return response.status == HttpStatusCode.OK
    }
}