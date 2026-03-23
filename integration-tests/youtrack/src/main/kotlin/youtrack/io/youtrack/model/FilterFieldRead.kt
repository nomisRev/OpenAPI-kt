package io.youtrack.model

import kotlin.OptIn
import kotlin.String
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

  @SerialName("Default")
  @Serializable
  public data class Default(
    override val id: String? = null,
    override val presentation: String? = null,
    override val name: String? = null,
  ) : FilterFieldRead

  @SerialName("PredefinedFilterField")
  @Serializable
  public data class PredefinedFilterField(
    override val id: String? = null,
    override val presentation: String? = null,
    override val name: String? = null,
  ) : FilterFieldRead

  @SerialName("CustomFilterField")
  @Serializable
  public data class CustomFilterField(
    override val id: String? = null,
    override val presentation: String? = null,
    override val name: String? = null,
    public val customField: CustomFieldRead? = null,
  ) : FilterFieldRead
}
