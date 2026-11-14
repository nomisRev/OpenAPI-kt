package io.github.model

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Metadata for a Git tag
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class GitTag(
  @SerialName("node_id")
  public val nodeId: String,
  public val tag: String,
  public val sha: String,
  public val url: String,
  public val message: String,
  public val tagger: Tagger,
  public val `object`: Object,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val verification: Verification? = null,
) {
  @Serializable
  public data class Object(
    public val sha: String,
    public val type: String,
    public val url: String,
  )

  @Serializable
  public data class Tagger(
    public val date: String,
    public val email: String,
    public val name: String,
  )
}
