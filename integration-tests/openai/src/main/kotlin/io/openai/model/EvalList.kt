package io.openai.model

import kotlin.Boolean
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * An object representing a list of evals.
 *
 */
@Serializable
public data class EvalList(
  @Required
  public val `object`: Object = Object.List,
  public val `data`: List<Eval>,
  @SerialName("first_id")
  public val firstId: String,
  @SerialName("last_id")
  public val lastId: String,
  @SerialName("has_more")
  public val hasMore: Boolean,
) {
  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("list")
    List("list"),
    ;
  }
}
