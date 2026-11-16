package io.github.nomisrev.render.test.client.allof.`property`.two.refs

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class ProductVoice(
  public val `value`: String,
) {
  SMS("SMS"),
  @SerialName("VOICE-CALL")
  VOICECALL("VOICE-CALL"),
  ;
}
