package com.mybusiness.model

import com.soywiz.klock.DateTime
import kotlinx.serialization.Serializable
import kotlin.random.Random

@Serializable
data class Contact(
        val id: String,
        val name: Name,
        val addresses: List<Address> = mutableListOf(),
        val phones: List<Phone> = mutableListOf()
) {
    val fullName: String
        get() = "${name.lastName} ${name.firstName}"

    val birthday: DateTime
        get() = DateTime(
                    year = Random.nextInt(1950, 2019),
                    month = Random.nextInt(1, 12),
                    day = Random.nextInt(1, 28)
            )
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