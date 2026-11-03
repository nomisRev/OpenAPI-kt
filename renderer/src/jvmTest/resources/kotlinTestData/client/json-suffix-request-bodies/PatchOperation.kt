package io.github.nomisrev.render.test.client.json.suffix.request.bodies

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class PatchOperation(
  public val op: String,
  public val path: String,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val `value`: String? = null,
)
