package io.github.nomisrev.render.test.client.reference.siblings

import kotlin.Double
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class TaxComponent(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val rate: Double? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val modifiedDate: String? = null,
  public val name: String,
)
