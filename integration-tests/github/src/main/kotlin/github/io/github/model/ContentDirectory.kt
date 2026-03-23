package io.github.model

import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A list of directory items
 */
@JvmInline
@Serializable
public value class ContentDirectory(
  public val items: List<Item>,
) {
  @Serializable
  public data class Item(
    public val type: Type,
    public val size: Long,
    public val name: String,
    public val path: String,
    public val content: String? = null,
    public val sha: String,
    public val url: String,
    @SerialName("git_url")
    public val gitUrl: String?,
    @SerialName("html_url")
    public val htmlUrl: String?,
    @SerialName("download_url")
    public val downloadUrl: String?,
    @SerialName("_links")
    public val links: Links,
  ) {
    @Serializable
    public data class Links(
      public val git: String?,
      public val html: String?,
      public val self: String,
    )

    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("dir")
      Dir("dir"),
      @SerialName("file")
      File("file"),
      @SerialName("submodule")
      Submodule("submodule"),
      @SerialName("symlink")
      Symlink("symlink"),
      ;
    }
  }
}
