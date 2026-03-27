package io.github.nomisrev.render.test.union.discriminated.anyof.inherited.allof.tag

import kotlin.OptIn
import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("type")
@Serializable
public sealed interface ConversationItem {
  @JvmInline
  @SerialName("message")
  @Serializable
  public value class Message(
    public val text: String,
  ) : ConversationItem

  @SerialName("function_call")
  @Serializable
  public data class FunctionCall(
    @SerialName("call_id")
    public val callId: String,
    public val id: String,
  ) : ConversationItem
}
