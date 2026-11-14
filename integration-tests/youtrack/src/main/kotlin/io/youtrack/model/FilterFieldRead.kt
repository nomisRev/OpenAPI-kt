package io.youtrack.model

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

/**
 * Represents an issue property, which can be a predefined field, a custom field, a link, and so on.
 */
@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("${'$'}type")
@Serializable
public sealed interface FilterFieldRead {
  public val id: String?

  public val presentation: String?

  public val name: String?

  @SerialName("FilterField")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Default(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val presentation: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
  ) : FilterFieldRead

  @SerialName("PredefinedFilterField")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class PredefinedFilterField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val presentation: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
  ) : FilterFieldRead

  @SerialName("CustomFilterField")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class CustomFilterField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val presentation: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val customField: CustomFieldRead? = null,
  ) : FilterFieldRead
}
