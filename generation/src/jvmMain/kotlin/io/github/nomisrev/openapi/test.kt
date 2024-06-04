package io.github.nomisrev.openapi


sealed interface CreateChatCompletionRequestModel {
    val value: String

    data class Custom(override val value: String) : CreateChatCompletionRequestModel
    enum class Enum(override val value: String) : CreateChatCompletionRequestModel {
        Gpt_4_0125_Preview("gpt-4-0125-preview"),
        Gpt_4_Turbo_Preview("gpt-4-turbo-preview"),
        Gpt_4_Turbo_1106_Preview("gpt-4-1106-preview");
    }
}