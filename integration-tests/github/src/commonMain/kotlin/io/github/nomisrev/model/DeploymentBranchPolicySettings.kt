package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class DeploymentBranchPolicySettings(
    @SerialName("protected_branches") val protectedBranches: Boolean,
    @SerialName("custom_branch_policies") val customBranchPolicies: Boolean,
)
