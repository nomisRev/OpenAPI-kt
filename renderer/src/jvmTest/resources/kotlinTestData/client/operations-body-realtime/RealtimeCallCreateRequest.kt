package io.github.nomisrev.render.test.client.operations.body.realtime

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class RealtimeCallCreateRequest(
  public val sdp: String,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val session: Session? = null,
) {
  @Serializable
  public data class Session(
    public val type: String,
    public val model: String,
  )
}
