package com.mybusiness.model

import kotlinx.serialization.Serializable

@Serializable
data class Contact(
        val id: String,
        val name: Name,
        val addresses: List<Address> = mutableListOf(),
        val phones: List<Phone> = mutableListOf()
) {
    val fullName: String
        get() = "${name.lastName} ${name.firstName}"
}

@Serializable
data class Name(
        val firstName: String,
        val lastName: String
)

@Serializable
data class Address(
        val type: Type,
        val street: String,
        val postalCode: String,
        val city: String,
        val country: String
) {
    enum class Type(val displayedName: String) { HOME("HOME"), WORK("WORK"), OTHER("OTHER") }
}

@Serializable
data class Phone(
        val type: Type,
        val number: String
) {
    enum class Type(val displayedName: String) { HOME("HOME"), WORK("WORK"), MOBILE("MOBILE"), OTHER("OTHER") }
}