package io.github.nomisrev

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
sealed interface Union {
    @Serializable
    @JvmInline
    value class CaseString(val value: String) : Union

    @Serializable
    enum class AscOrDesc : Union {
        @SerialName("asc")
        Asc, @SerialName("desc")
        Desc;
    }
}
