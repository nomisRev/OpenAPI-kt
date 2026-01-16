package io.github.nomisrev.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator($$"$type")
@Serializable
sealed interface BundleRequest {


    @SerialName("Default")
    @Serializable
    data object Default : BundleRequest

    @SerialName("BaseBundle")
    @Serializable
    @JvmInline
    value class BaseBundle(val values: List<BundleElementRequest>? = null) : BundleRequest

    @SerialName("StateBundle")
    @Serializable
    @JvmInline
    value class StateBundle(val values: List<StateBundleElement>? = null) : BundleRequest

    @SerialName("EnumBundle")
    @Serializable
    @JvmInline
    value class EnumBundle(val values: List<EnumBundleElement>? = null) : BundleRequest

    @SerialName("UserBundle")
    @Serializable
    data class UserBundle(
        val groups: List<UserGroupRequest>? = null,
        val individuals: List<UserRequest>? = null,
    ) : BundleRequest

    @SerialName("OwnedBundle")
    @Serializable
    @JvmInline
    value class OwnedBundle(val values: List<BundleElementRequest>? = null) : BundleRequest

    @SerialName("VersionBundle")
    @Serializable
    @JvmInline
    value class VersionBundle(val values: List<BundleElementRequest>? = null) : BundleRequest

    @SerialName("BuildBundle")
    @Serializable
    @JvmInline
    value class BuildBundle(val values: List<BundleElementRequest>? = null) : BundleRequest
}
