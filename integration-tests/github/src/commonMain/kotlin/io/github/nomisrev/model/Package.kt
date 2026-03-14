package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Package(
    val id: Long,
    val name: String,
    @SerialName("package_type") val packageType: PackageType,
    val url: String,
    @SerialName("html_url") val htmlUrl: String,
    @SerialName("version_count") val versionCount: Long,
    val visibility: Visibility,
    val owner: NullableSimpleUser? = null,
    val repository: NullableMinimalRepository? = null,
    @SerialName("created_at") val createdAt: LocalDateTime,
    @SerialName("updated_at") val updatedAt: LocalDateTime,
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
    enum class Visibility {
        @SerialName("private") Private, @SerialName("public") Public;
    }
}
