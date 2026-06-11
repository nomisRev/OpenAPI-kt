package io.github.model

import kotlin.Boolean
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The type of deployment branch policy for this environment. To allow all branches to deploy, set to `null`.
 */
@Serializable
public data class DeploymentBranchPolicySettings(
  @SerialName("protected_branches")
  public val protectedBranches: Boolean,
  @SerialName("custom_branch_policies")
  public val customBranchPolicies: Boolean,
)
