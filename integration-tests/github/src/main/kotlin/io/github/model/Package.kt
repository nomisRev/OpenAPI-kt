package io.github.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A software package
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class Package(
  public val id: Long,
  public val name: String,
  @SerialName("package_type")
  public val packageType: PackageType,
  public val url: String,
  @SerialName("html_url")
  public val htmlUrl: String,
  @SerialName("version_count")
  public val versionCount: Long,
  public val visibility: Visibility,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val owner: Optional<NullableSimpleUser> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val repository: Optional<NullableMinimalRepository> = Optional.Absent,
  @SerialName("created_at")
  public val createdAt: Instant,
  @SerialName("updated_at")
  public val updatedAt: Instant,
) {
  @Serializable
  public enum class PackageType(
    public val `value`: String,
  ) {
    @SerialName("npm")
    Npm("npm"),
    @SerialName("maven")
    Maven("maven"),
    @SerialName("rubygems")
    Rubygems("rubygems"),
    @SerialName("docker")
    Docker("docker"),
    @SerialName("nuget")
    Nuget("nuget"),
    @SerialName("container")
    Container("container"),
    ;
  }

  @Serializable
  public enum class Visibility(
    public val `value`: String,
  ) {
    @SerialName("private")
    Private("private"),
    @SerialName("public")
    Public("public"),
    ;
  }
}
