package io.github.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class CodeScanningVariantAnalysisLanguageWrite(
  public val `value`: String,
) {
  @SerialName("actions")
  Actions("actions"),
  @SerialName("cpp")
  Cpp("cpp"),
  @SerialName("csharp")
  Csharp("csharp"),
  @SerialName("go")
  Go("go"),
  @SerialName("java")
  Java("java"),
  @SerialName("javascript")
  Javascript("javascript"),
  @SerialName("python")
  Python("python"),
  @SerialName("ruby")
  Ruby("ruby"),
  @SerialName("rust")
  Rust("rust"),
  @SerialName("swift")
  Swift("swift"),
  ;
}
