package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class OrgRepoCustomPropertyValues(
    @SerialName("repository_id") val repositoryId: Long,
    @SerialName("repository_name") val repositoryName: String,
    @SerialName("repository_full_name") val repositoryFullName: String,
    val properties: List<CustomPropertyValue>,
)
