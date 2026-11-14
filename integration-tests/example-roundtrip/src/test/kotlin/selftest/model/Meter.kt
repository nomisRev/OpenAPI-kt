package selftest.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Rendered by hand the way the generator renders an integer enum today. */
@Serializable
public enum class MeterRate {
    @SerialName("300") `300`,
    @SerialName("600") `600`,
}

@Serializable
public data class Meter(
    @SerialName("name") val name: String,
    @SerialName("rate") val rate: MeterRate? = null,
)
