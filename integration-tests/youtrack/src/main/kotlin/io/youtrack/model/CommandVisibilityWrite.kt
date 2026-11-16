package io.youtrack.model

import kotlin.OptIn
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement

/**
 * Visibility settings of the comment that is added along with the command.
 */
@Serializable(with = CommandVisibilityWrite.Serializer::class)
public sealed interface CommandVisibilityWrite {
  @SerialName("CommandVisibility")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = Default.Serializer::class)
  public data object Default : CommandVisibilityWrite {
    public object Serializer : TaggedCaseSerializer<Default>(Default.generatedSerializer(), "${'$'}type", "CommandVisibility")
  }

  @SerialName("CommandUnlimitedVisibility")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = CommandUnlimitedVisibility.Serializer::class)
  public class CommandUnlimitedVisibility() : CommandVisibilityWrite {
    public object Serializer : TaggedCaseSerializer<CommandUnlimitedVisibility>(CommandUnlimitedVisibility.generatedSerializer(), "${'$'}type", "CommandUnlimitedVisibility")
  }

  @SerialName("CommandLimitedVisibility")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = CommandLimitedVisibility.Serializer::class)
  public class CommandLimitedVisibility() : CommandVisibilityWrite {
    public object Serializer : TaggedCaseSerializer<CommandLimitedVisibility>(CommandLimitedVisibility.generatedSerializer(), "${'$'}type", "CommandLimitedVisibility")
  }

  public object Serializer : JsonContentPolymorphicSerializer<CommandVisibilityWrite>(CommandVisibilityWrite::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<CommandVisibilityWrite> = when (val tag = element.discriminatorValue("${'$'}type")) {
      "CommandVisibility" -> Default.serializer()
      "CommandUnlimitedVisibility" -> CommandUnlimitedVisibility.serializer()
      "CommandLimitedVisibility" -> CommandLimitedVisibility.serializer()
      else -> throw SerializationException("'${'$'}type' of CommandVisibilityWrite selects none of CommandVisibility, CommandUnlimitedVisibility, CommandLimitedVisibility: " + tag)
    }
  }
}
