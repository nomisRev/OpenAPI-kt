package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class BranchWithProtection(
    val name: String,
    val commit: Commit,
    @SerialName("_links") val links: Links,
    val protected: Boolean,
    val protection: BranchProtection,
    @SerialName("protection_url") val protectionUrl: String,
    val pattern: String? = null,
    @SerialName("required_approving_review_count") val requiredApprovingReviewCount: Long? = null,
) {
    @Serializable
    data class Links(val html: String, val self: String)
}
