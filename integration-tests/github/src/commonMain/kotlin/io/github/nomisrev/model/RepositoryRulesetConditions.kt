package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName

@Serializable
@JvmInline
value class RepositoryRulesetConditions(@SerialName("ref_name") val refName: RefName? = null) {
    @Serializable
    data class RefName(val include: List<String>? = null, val exclude: List<String>? = null)
}
