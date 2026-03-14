package discriminatedunion.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("type")
@Serializable
sealed interface User {
    val id: Long

    @SerialName("AnonymousUser")
    @Serializable
    @JvmInline
    value class AnonymousUser(override val id: Long) : User

    @SerialName("RegisteredUser")
    @Serializable
    data class RegisteredUser(override val id: Long, val email: String) : User

    @SerialName("ProUser")
    @Serializable
    data class ProUser(override val id: Long, val email: String, val subscriptionId: Long) : User
}
