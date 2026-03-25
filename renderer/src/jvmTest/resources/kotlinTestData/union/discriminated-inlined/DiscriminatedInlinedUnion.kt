package io.github.nomisrev.render.test.union.discriminated.inlined

import kotlin.Boolean
import kotlin.OptIn
import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("event")
@Serializable
public sealed interface DiscriminatedInlinedUnion {
  @JvmInline
  @SerialName("created")
  @Serializable
  public value class Created(
    public val id: String,
  ) : DiscriminatedInlinedUnion

  @JvmInline
  @SerialName("deleted")
  @Serializable
  public value class Deleted(
    public val hard: Boolean,
  ) : DiscriminatedInlinedUnion
}
