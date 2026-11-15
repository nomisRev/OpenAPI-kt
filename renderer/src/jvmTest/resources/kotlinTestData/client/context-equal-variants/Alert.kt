package io.github.nomisrev.render.test.client.context.equal.variants

import kotlin.Long
import kotlin.OptIn
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class Alert(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val id: Long? = null,
  @SerialName("created_at")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val createdAt: AlertCreatedAt? = null,
)
