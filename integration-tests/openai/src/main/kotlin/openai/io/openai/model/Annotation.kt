package io.openai.model

import kotlin.OptIn
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

/**
 * An annotation that applies to a span of output text.
 */
@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("type")
@Serializable
public sealed interface Annotation {
  @Serializable
  @JvmInline
  @SerialName("FileCitationBody")
  public value class FileCitationBody(
    public val `value`: io.openai.model.FileCitationBody,
  ) : Annotation

  @Serializable
  @JvmInline
  @SerialName("UrlCitationBody")
  public value class UrlCitationBody(
    public val `value`: io.openai.model.UrlCitationBody,
  ) : Annotation

  @Serializable
  @JvmInline
  @SerialName("ContainerFileCitationBody")
  public value class ContainerFileCitationBody(
    public val `value`: io.openai.model.ContainerFileCitationBody,
  ) : Annotation

  @Serializable
  @JvmInline
  @SerialName("FilePath")
  public value class FilePath(
    public val `value`: io.openai.model.FilePath,
  ) : Annotation
}
