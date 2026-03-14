package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
enum class CodeScanningVariantAnalysisLanguage {
    @SerialName("actions")
    Actions,
    @SerialName("cpp")
    Cpp,
    @SerialName("csharp")
    Csharp,
    @SerialName("go")
    Go,
    @SerialName("java")
    Java,
    @SerialName("javascript")
    Javascript,
    @SerialName("python")
    Python,
    @SerialName("ruby")
    Ruby,
    @SerialName("rust")
    Rust,
    @SerialName("swift")
    Swift;
}
