package io.github.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class PullRequestMinimal(
  public val id: Long,
  public val number: Long,
  public val url: String,
  public val head: Head,
  public val base: Base,
) {
  @Serializable
  public data class Base(
    public val ref: String,
    public val sha: String,
    public val repo: Repo,
  ) {
    @Serializable
    public data class Repo(
      public val id: Long,
      public val url: String,
      public val name: String,
    )
  }

  @Serializable
  public data class Head(
    public val ref: String,
    public val sha: String,
    public val repo: Repo,
  ) {
    @Serializable
    public data class Repo(
      public val id: Long,
      public val url: String,
      public val name: String,
    )
  }
}
