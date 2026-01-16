package io.github.nomisrev.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator($$"$type")
@Serializable
sealed interface BundleResponse {
    val id: String?
    val isUpdateable: Boolean?

    @SerialName("Default")
    @Serializable
    data class Default(override val id: String? = null, override val isUpdateable: Boolean? = null) : BundleResponse

    @SerialName("BaseBundle")
    @Serializable
    data class BaseBundle(
        override val id: String? = null,
        override val isUpdateable: Boolean? = null,
        val values: List<BundleElementResponse>? = null,
    ) : BundleResponse

    @SerialName("StateBundle")
    @Serializable
    data class StateBundle(
        override val id: String? = null,
        override val isUpdateable: Boolean? = null,
        val values: List<StateBundleElement>? = null,
    ) : BundleResponse

    @SerialName("EnumBundle")
    @Serializable
    data class EnumBundle(
        override val id: String? = null,
        override val isUpdateable: Boolean? = null,
        val values: List<EnumBundleElement>? = null,
    ) : BundleResponse

    @SerialName("UserBundle")
    @Serializable
    data class UserBundle(
        override val id: String? = null,
        override val isUpdateable: Boolean? = null,
        val groups: List<UserGroupResponse>? = null,
        val individuals: List<UserResponse>? = null,
        val aggregatedUsers: List<UserResponse>? = null,
    ) : BundleResponse

    @SerialName("OwnedBundle")
    @Serializable
    data class OwnedBundle(
        override val id: String? = null,
        override val isUpdateable: Boolean? = null,
        val values: List<BundleElementResponse>? = null,
    ) : BundleResponse

    @SerialName("VersionBundle")
    @Serializable
    data class VersionBundle(
        override val id: String? = null,
        override val isUpdateable: Boolean? = null,
        val values: List<BundleElementResponse>? = null,
    ) : BundleResponse

    @SerialName("BuildBundle")
    @Serializable
    data class BuildBundle(
        override val id: String? = null,
        override val isUpdateable: Boolean? = null,
        val values: List<BundleElementResponse>? = null,
    ) : BundleResponse
}
