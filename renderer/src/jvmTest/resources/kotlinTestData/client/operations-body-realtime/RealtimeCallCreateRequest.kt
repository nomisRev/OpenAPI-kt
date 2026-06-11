package io.github.nomisrev.render.test.client.operations.body.realtime

import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class RealtimeCallCreateRequest(
  public val sdp: String,
  public val session: Session? = null,
) {
  @Serializable
  public data class Session(
    public val type: String,
    public val model: String,
  )
}
