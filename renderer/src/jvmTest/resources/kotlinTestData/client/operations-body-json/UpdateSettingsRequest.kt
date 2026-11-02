package io.github.nomisrev.render.test.client.operations.body.json

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class UpdateSettingsRequest(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val theme: String? = null,
)
