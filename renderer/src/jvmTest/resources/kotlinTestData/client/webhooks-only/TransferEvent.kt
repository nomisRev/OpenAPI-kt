package io.github.nomisrev.render.test.client.webhooks.only

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class TransferEvent(
  public val type: String,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val amount: Long? = null,
)
