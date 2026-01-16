package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class IssueLinkRequest(val linkType: IssueLinkTypeRequest? = null, val issues: List<IssueRequest>? = null)
