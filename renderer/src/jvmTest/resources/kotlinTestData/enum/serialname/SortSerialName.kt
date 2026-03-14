package io.github.nomisrev.render.test.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class SortSerialName {
  @SerialName("very_long_enum_value_1")
  VeryLongEnumValue1,
  @SerialName("very_long_enum_value_2")
  VeryLongEnumValue2,
  @SerialName("very_long_enum_value_3")
  VeryLongEnumValue3,
  @SerialName("very_long_enum_value_4")
  VeryLongEnumValue4,
  @SerialName("very_long_enum_value_5")
  VeryLongEnumValue5,
}
