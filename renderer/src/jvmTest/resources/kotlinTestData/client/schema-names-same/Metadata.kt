package io.github.nomisrev.render.test.client.schema.names.same

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class Metadata(
  public val kind: String,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val a: String? = null,
)
