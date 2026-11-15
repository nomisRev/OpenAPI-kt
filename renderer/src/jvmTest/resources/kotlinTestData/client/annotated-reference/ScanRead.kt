package io.github.nomisrev.render.test.client.annotated.reference

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class ScanRead(
  public val name: String,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val status: Status? = null,
)
