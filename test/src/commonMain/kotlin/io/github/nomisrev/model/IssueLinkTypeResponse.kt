package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class IssueLinkTypeResponse(
    val id: String? = null,
    val name: String? = null,
    val localizedName: String? = null,
    val sourceToTarget: String? = null,
    val localizedSourceToTarget: String? = null,
    val targetToSource: String? = null,
    val localizedTargetToSource: String? = null,
    val directed: Boolean? = null,
    val aggregation: Boolean? = null,
    val readOnly: Boolean? = null,
    @SerialName($$"$type") val type: String? = null,
)
