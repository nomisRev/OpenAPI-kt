package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class IssueLinkTypeRequest(
    val name: String? = null,
    val localizedName: String? = null,
    val sourceToTarget: String? = null,
    val localizedSourceToTarget: String? = null,
    val targetToSource: String? = null,
    val localizedTargetToSource: String? = null,
    val directed: Boolean? = null,
    val aggregation: Boolean? = null,
)
