package io.github.nomisrev.render.test.client.json.suffix.request.bodies

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class ClaimPatch(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val status: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val note: String? = null,
)
