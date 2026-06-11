package io.github.nomisrev.render.test.union.discriminated.`value`.class_.case

import kotlin.OptIn
import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("kind")
@Serializable
public sealed interface DiscriminatedValueClassUnion {
  @Serializable
  @SerialName("inline")
  public data object Inline : DiscriminatedValueClassUnion

  @JvmInline
  @SerialName("remote")
  @Serializable
  public value class Remote(
    public val id: String,
  ) : DiscriminatedValueClassUnion
}
