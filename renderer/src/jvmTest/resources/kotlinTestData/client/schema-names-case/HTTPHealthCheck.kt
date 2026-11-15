package io.github.nomisrev.render.test.client.schema.names.case

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class HTTPHealthCheck(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val a: String? = null,
)
