package io.youtrack.model

import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.EncodeDefault
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
@Serializable(with = CommandVisibilityRead.Serializer::class)
public sealed interface CommandVisibilityRead {
  public val id: String?

  @SerialName("CommandVisibility")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = Default.Serializer::class)
  public data class Default(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
  ) : CommandVisibilityRead {
    public object Serializer : TaggedCaseSerializer<Default>(Default.generatedSerializer(), "${'$'}type", "CommandVisibility")
  }

  @SerialName("CommandUnlimitedVisibility")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = CommandUnlimitedVisibility.Serializer::class)
  public data class CommandUnlimitedVisibility(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
  ) : CommandVisibilityRead {
    public object Serializer : TaggedCaseSerializer<CommandUnlimitedVisibility>(CommandUnlimitedVisibility.generatedSerializer(), "${'$'}type", "CommandUnlimitedVisibility")
  }

  @SerialName("CommandLimitedVisibility")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = CommandLimitedVisibility.Serializer::class)
  public data class CommandLimitedVisibility(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val permittedGroups: List<UserGroupRead>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val permittedUsers: List<UserRead>? = null,
  ) : CommandVisibilityRead {
    public object Serializer : TaggedCaseSerializer<CommandLimitedVisibility>(CommandLimitedVisibility.generatedSerializer(), "${'$'}type", "CommandLimitedVisibility")
  }

  public object Serializer : JsonContentPolymorphicSerializer<CommandVisibilityRead>(CommandVisibilityRead::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<CommandVisibilityRead> = when (val tag = element.discriminatorValue("${'$'}type")) {
      "CommandVisibility" -> Default.serializer()
      "CommandUnlimitedVisibility" -> CommandUnlimitedVisibility.serializer()
      "CommandLimitedVisibility" -> CommandLimitedVisibility.serializer()
      else -> throw SerializationException("'${'$'}type' of CommandVisibilityRead selects none of CommandVisibility, CommandUnlimitedVisibility, CommandLimitedVisibility: " + tag)
    }
  }
}
