package io.github.model

import kotlin.Boolean
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Private registry configuration for an organization
 */
@Serializable
public data class OrgPrivateRegistryConfiguration(
  public val name: String,
  @SerialName("registry_type")
  public val registryType: RegistryType,
  public val url: String? = null,
  public val username: String? = null,
  @SerialName("replaces_base")
  public val replacesBase: Boolean? = null,
  public val visibility: Visibility,
  @SerialName("created_at")
  public val createdAt: Instant,
  @SerialName("updated_at")
  public val updatedAt: Instant,
) {
  @Serializable
  public enum class RegistryType(
    public val `value`: String,
  ) {
    @SerialName("maven_repository")
    MavenRepository("maven_repository"),
    @SerialName("nuget_feed")
    NugetFeed("nuget_feed"),
    @SerialName("goproxy_server")
    GoproxyServer("goproxy_server"),
    @SerialName("npm_registry")
    NpmRegistry("npm_registry"),
    @SerialName("rubygems_server")
    RubygemsServer("rubygems_server"),
    @SerialName("cargo_registry")
    CargoRegistry("cargo_registry"),
    @SerialName("composer_repository")
    ComposerRepository("composer_repository"),
    @SerialName("docker_registry")
    DockerRegistry("docker_registry"),
    @SerialName("git_source")
    GitSource("git_source"),
    @SerialName("helm_registry")
    HelmRegistry("helm_registry"),
    @SerialName("hex_organization")
    HexOrganization("hex_organization"),
    @SerialName("hex_repository")
    HexRepository("hex_repository"),
    @SerialName("pub_repository")
    PubRepository("pub_repository"),
    @SerialName("python_index")
    PythonIndex("python_index"),
    @SerialName("terraform_registry")
    TerraformRegistry("terraform_registry"),
    ;
  }

  @Serializable
  public enum class Visibility(
    public val `value`: String,
  ) {
    @SerialName("all")
    All("all"),
    @SerialName("private")
    Private("private"),
    @SerialName("selected")
    Selected("selected"),
    ;
  }
}
