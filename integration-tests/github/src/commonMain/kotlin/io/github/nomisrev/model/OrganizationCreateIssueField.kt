package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class OrganizationCreateIssueField(
    val name: String,
    val description: String? = null,
    @SerialName("data_type") val dataType: DataType,
    val visibility: Visibility? = null,
    val options: List<Options>? = null,
) {
    @Serializable
    enum class DataType {
        @SerialName("text")
        Text,
        @SerialName("date")
        Date,
        @SerialName("single_select")
        SingleSelect,
        @SerialName("number")
        Number;
    }

    @Serializable
    enum class Visibility {
        @SerialName("organization_members_only") OrganizationMembersOnly, @SerialName("all") All;
    }

    @Serializable
    data class Options(val name: String, val description: String? = null, val color: Color, val priority: Long) {
        @Serializable
        enum class Color {
            @SerialName("gray")
            Gray,
            @SerialName("blue")
            Blue,
            @SerialName("green")
            Green,
            @SerialName("yellow")
            Yellow,
            @SerialName("orange")
            Orange,
            @SerialName("red")
            Red,
            @SerialName("pink")
            Pink,
            @SerialName("purple")
            Purple;
        }
    }
}
