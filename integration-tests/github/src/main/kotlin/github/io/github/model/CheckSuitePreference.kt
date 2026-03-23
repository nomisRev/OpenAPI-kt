package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Check suite configuration preferences for a repository.
 */
@Serializable
public data class CheckSuitePreference(
  public val preferences: Preferences,
  public val repository: MinimalRepository,
) {
  @JvmInline
  @Serializable
  public value class Preferences(
    @SerialName("auto_trigger_checks")
    public val autoTriggerChecks: List<AutoTriggerChecks>? = null,
  ) {
    @Serializable
    public data class AutoTriggerChecks(
      @SerialName("app_id")
      public val appId: Long,
      public val setting: Boolean,
    )
  }
}
