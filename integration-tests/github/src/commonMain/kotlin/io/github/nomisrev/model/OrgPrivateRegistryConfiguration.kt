package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class OrgPrivateRegistryConfiguration(
    val name: String,
    @SerialName("registry_type") val registryType: RegistryType,
    val url: String? = null,
    val username: String? = null,
    @SerialName("replaces_base") val replacesBase: Boolean? = null,
    val visibility: Visibility,
    @SerialName("created_at") val createdAt: LocalDateTime,
    @SerialName("updated_at") val updatedAt: LocalDateTime,
) {
    @Serializable
    enum class RegistryType {
        @SerialName("maven_repository")
        MavenRepository,
        @SerialName("nuget_feed")
        NugetFeed,
        @SerialName("goproxy_server")
        GoproxyServer,
        @SerialName("npm_registry")
        NpmRegistry,
        @SerialName("rubygems_server")
        RubygemsServer,
        @SerialName("cargo_registry")
        CargoRegistry,
        @SerialName("composer_repository")
        ComposerRepository,
        @SerialName("docker_registry")
        DockerRegistry,
        @SerialName("git_source")
        GitSource,
        @SerialName("helm_registry")
        HelmRegistry,
        @SerialName("hex_organization")
        HexOrganization,
        @SerialName("hex_repository")
        HexRepository,
        @SerialName("pub_repository")
        PubRepository,
        @SerialName("python_index")
        PythonIndex,
        @SerialName("terraform_registry")
        TerraformRegistry;
    }

    @Serializable
    enum class Visibility {
        @SerialName("all") All, @SerialName("private") Private, @SerialName("selected") Selected;
    }
}
