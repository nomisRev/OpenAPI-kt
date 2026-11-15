package io.github.nomisrev.render.test.client.context.variants.both

import kotlin.OptIn
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class SummaryRead(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val owner: PersonRead? = null,
)
