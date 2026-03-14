package union.discriminated.primitive.model

import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlin.jvm.JvmInline

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator($$"$type")
@Serializable
sealed interface Union {
    @SerialName("reference")
    @Serializable
    @JvmInline
    value class Reference(val value: String) : Union

    @SerialName("employee")
    @Serializable
    data class Employee(val age: Int, val name: String) : Union
}
