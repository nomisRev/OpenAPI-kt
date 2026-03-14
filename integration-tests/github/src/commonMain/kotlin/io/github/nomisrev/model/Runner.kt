package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Runner(
    val id: Long,
    @SerialName("runner_group_id") val runnerGroupId: Long? = null,
    val name: String,
    val os: String,
    val status: String,
    val busy: Boolean,
    val labels: List<RunnerLabel>,
    val ephemeral: Boolean? = null,
)
