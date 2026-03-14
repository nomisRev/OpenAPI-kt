package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
enum class SecurityAdvisoryEcosystems {
    @SerialName("rubygems")
    Rubygems,
    @SerialName("npm")
    Npm,
    @SerialName("pip")
    Pip,
    @SerialName("maven")
    Maven,
    @SerialName("nuget")
    Nuget,
    @SerialName("composer")
    Composer,
    @SerialName("go")
    Go,
    @SerialName("rust")
    Rust,
    @SerialName("erlang")
    Erlang,
    @SerialName("actions")
    Actions,
    @SerialName("pub")
    Pub,
    @SerialName("other")
    Other,
    @SerialName("swift")
    Swift;
}
