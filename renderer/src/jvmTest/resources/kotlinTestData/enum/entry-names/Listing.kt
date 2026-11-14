package io.github.nomisrev.render.test.`enum`.entry.names

import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class Listing(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val ordering: Ordering? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val `operator`: Operator? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val encryption: Encryption? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val kind: Kind? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val level: Level? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val sort: Sort? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val direction: Direction? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val operators: List<Operators>? = null,
) {
  @Serializable
  public enum class Direction(
    public val `value`: String,
  ) {
    @SerialName("last_activity")
    LastActivity("last_activity"),
    @SerialName("-last_activity")
    MinusLastActivity("-last_activity"),
    ;
  }

  @Serializable
  public enum class Operators(
    public val `value`: String,
  ) {
    @SerialName("eq")
    Eq("eq"),
    @SerialName("<")
    LessThan("<"),
    @SerialName("<=")
    LessThanOrEqual("<="),
    @SerialName(">")
    GreaterThan(">"),
    @SerialName(">=")
    GreaterThanOrEqual(">="),
    ;
  }
}
