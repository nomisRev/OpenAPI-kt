package io.github.nomisrev.render.test.object_.nullability

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Required
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class Nullability(
  public val requiredNotNullable: String,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val optionalNotNullable: String? = null,
  public val requiredNullableNoDefault: String?,
  @Required
  public val requiredNullableWithDefault: String? = null,
)
