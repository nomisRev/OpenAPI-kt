package io.github.model

import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A version of a software package
 */
@Serializable
public data class PackageVersion(
  public val id: Long,
  public val name: String,
  public val url: String,
  @SerialName("package_html_url")
  public val packageHtmlUrl: String,
  @SerialName("html_url")
  public val htmlUrl: String? = null,
  public val license: String? = null,
  public val description: String? = null,
  @SerialName("created_at")
  public val createdAt: Instant,
  @SerialName("updated_at")
  public val updatedAt: Instant,
  @SerialName("deleted_at")
  public val deletedAt: Instant? = null,
  public val metadata: Metadata? = null,
) {
  @Serializable
  public data class Metadata(
    @SerialName("package_type")
    public val packageType: PackageType,
    public val container: Container? = null,
    public val docker: Docker? = null,
  ) {
    @JvmInline
    @Serializable
    public value class Container(
      public val tags: List<String>,
    )

    @JvmInline
    @Serializable
    public value class Docker(
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
