package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName

@Serializable
@JvmInline
value class CodeSecurityDefaultConfigurations(val items: List<Item>) {
    @Serializable
    data class Item(
        @SerialName("default_for_new_repos") val defaultForNewRepos: DefaultForNewRepos? = null,
        val configuration: CodeSecurityConfiguration? = null,
    ) {
        @Serializable
        enum class DefaultForNewRepos {
            @SerialName("public") Public, @SerialName("private_and_internal") PrivateAndInternal, @SerialName("all") All;
        }
    }
}
