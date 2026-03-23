package io.github.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
public value class ActionsForkPrContributorApproval(
  @SerialName("approval_policy")
  public val approvalPolicy: ApprovalPolicy,
) {
  @Serializable
  public enum class ApprovalPolicy(
    public val `value`: String,
  ) {
    @SerialName("first_time_contributors_new_to_github")
    FirstTimeContributorsNewToGithub("first_time_contributors_new_to_github"),
    @SerialName("first_time_contributors")
    FirstTimeContributors("first_time_contributors"),
    @SerialName("all_external_contributors")
    AllExternalContributors("all_external_contributors"),
    ;
  }
}
