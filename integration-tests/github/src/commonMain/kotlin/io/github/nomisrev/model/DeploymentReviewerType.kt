package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
enum class DeploymentReviewerType {
    User, Team;
}
