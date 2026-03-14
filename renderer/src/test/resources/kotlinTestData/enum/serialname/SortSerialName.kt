package enum.serialname.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
enum class SortSerialName {
    @SerialName("very_long_enum_value_1")
    VeryLongEnumValue1,
    @SerialName("very_long_enum_value_2")
    VeryLongEnumValue2,
    @SerialName("very_long_enum_value_3")
    VeryLongEnumValue3,
    @SerialName("very_long_enum_value_4")
    VeryLongEnumValue4,
    @SerialName("very_long_enum_value_5")
    VeryLongEnumValue5;
}
