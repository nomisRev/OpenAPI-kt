package io.github.model

import kotlinx.serialization.Serializable

@Serializable
public enum class DeploymentReviewerType {
  User,
  Team,
}
