package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlin.jvm.JvmInline

@Serializable
data class PackageVersion(
    val id: Long,
    val name: String,
    val url: String,
    @SerialName("package_html_url") val packageHtmlUrl: String,
    @SerialName("html_url") val htmlUrl: String? = null,
    val license: String? = null,
    val description: String? = null,
    @SerialName("created_at") val createdAt: LocalDateTime,
    @SerialName("updated_at") val updatedAt: LocalDateTime,
    @SerialName("deleted_at") val deletedAt: LocalDateTime? = null,
    val metadata: Metadata? = null,
) {
    @Serializable
    data class Metadata(
        @SerialName("package_type") val packageType: PackageType,
        val container: Container? = null,
        val docker: Docker? = null,
    ) {
        @Serializable
        enum class PackageType {
            @SerialName("npm")
            Npm,
            @SerialName("maven")
            Maven,
            @SerialName("rubygems")
            Rubygems,
            @SerialName("docker")
            Docker,
            @SerialName("nuget")
            Nuget,
            @SerialName("container")
            Container;
        }

        @Serializable
        @JvmInline
        value class Container(val tags: List<String>)

        @Serializable
        @JvmInline
        value class Docker(val tag: List<String>? = null)
    }
}
