package io.openai.model

import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

/**
 * Combine multiple filters using `and` or `or`.
 */
@Serializable
public data class CompoundFilter(
  public val type: Type,
  public val filters: List<Filters>,
) {
  @OptIn(ExperimentalSerializationApi::class)
  @JsonClassDiscriminator("type")
  @Serializable
  public sealed interface Filters {
    @Serializable
    @JvmInline
    @SerialName("ComparisonFilter")
    public value class ComparisonFilter(
      public val `value`: io.openai.model.ComparisonFilter,
    ) : Filters

    @Serializable
    @JvmInline
    @SerialName("#")
    public value class `#`(
      public val `value`: CompoundFilter,
    ) : Filters
  }

  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("and")
    And("and"),
    @SerialName("or")
    Or("or"),
    ;
  }
}
