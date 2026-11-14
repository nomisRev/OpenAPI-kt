package io.youtrack.model

import kotlin.Boolean
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Describes sprints configuration.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class SprintsSettings(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val id: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val isExplicit: Boolean? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val cardOnSeveralSprints: Boolean? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val defaultSprint: SprintRead? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val disableSprints: Boolean? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val explicitQuery: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val sprintSyncField: CustomFieldRead? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val hideSubtasksOfCards: Boolean? = null,
  @SerialName("${'$'}type")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val type: String? = null,
)
