package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class OrganizationRole(
    val id: Long,
    val name: String,
    val description: String? = null,
    @SerialName("base_role") val baseRole: BaseRole? = null,
    val source: Source? = null,
    val permissions: List<String>,
    val organization: NullableSimpleUser?,
    @SerialName("created_at") val createdAt: LocalDateTime,
    @SerialName("updated_at") val updatedAt: LocalDateTime,
) {
    @Serializable
    enum class BaseRole {
        @SerialName("read")
        Read,
        @SerialName("triage")
        Triage,
        @SerialName("write")
        Write,
        @SerialName("maintain")
        Maintain,
        @SerialName("admin")
        Admin;
    }

    @Serializable
    enum class Source {
        Organization, Enterprise, Predefined;
    }
}
