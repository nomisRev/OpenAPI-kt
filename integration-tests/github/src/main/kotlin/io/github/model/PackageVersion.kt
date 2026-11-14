package io.github.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.time.Instant
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A version of a software package
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class PackageVersion(
  public val id: Long,
  public val name: String,
  public val url: String,
  @SerialName("package_html_url")
  public val packageHtmlUrl: String,
  @SerialName("html_url")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val htmlUrl: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val license: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val description: String? = null,
  @SerialName("created_at")
  public val createdAt: Instant,
  @SerialName("updated_at")
  public val updatedAt: Instant,
  @SerialName("deleted_at")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val deletedAt: Instant? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val metadata: Metadata? = null,
) {
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Metadata(
    @SerialName("package_type")
    public val packageType: PackageType,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val container: Container? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val docker: Docker? = null,
  ) {
    @Serializable
    public data class Container(
      public val tags: List<String>,
    )

    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class Docker(
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val tag: List<String>? = null,
    )

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
  }
}
