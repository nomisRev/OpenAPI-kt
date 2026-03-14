package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName

@Serializable
data class CheckSuitePreference(val preferences: Preferences, val repository: MinimalRepository) {
    @Serializable
    @JvmInline
    value class Preferences(@SerialName("auto_trigger_checks") val autoTriggerChecks: List<AutoTriggerChecks>? = null) {
        @Serializable
        data class AutoTriggerChecks(@SerialName("app_id") val appId: Long, val setting: Boolean)
    }
}
