package io.github.nomisrev.render.test.client.schema.names.case

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class HttpHealthCheck2(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val b: String? = null,
)
