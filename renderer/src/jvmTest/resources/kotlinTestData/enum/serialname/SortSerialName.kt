package io.github.nomisrev.render.test.`enum`.serialname

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class SortSerialName(
  public val `value`: String,
) {
  @SerialName("very_long_enum_value_1")
  VeryLongEnumValue1("very_long_enum_value_1"),
  @SerialName("very_long_enum_value_2")
  VeryLongEnumValue2("very_long_enum_value_2"),
  @SerialName("very_long_enum_value_3")
  VeryLongEnumValue3("very_long_enum_value_3"),
  @SerialName("very_long_enum_value_4")
  VeryLongEnumValue4("very_long_enum_value_4"),
  @SerialName("very_long_enum_value_5")
  VeryLongEnumValue5("very_long_enum_value_5"),
  ;
}
