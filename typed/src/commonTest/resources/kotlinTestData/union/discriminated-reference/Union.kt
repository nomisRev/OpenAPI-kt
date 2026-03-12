package union.discriminated.reference.model

import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlin.jvm.JvmInline

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator($$"$type")
@Serializable
sealed interface Union {
    @SerialName("person")
    @Serializable
    @JvmInline
    value class Person(val value: Person) : Union

    @SerialName("employee")
    @Serializable
    data class Employee(val age: Int, val name: String) : Union
}
