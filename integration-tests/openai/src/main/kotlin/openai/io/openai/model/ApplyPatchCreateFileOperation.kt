package io.openai.model

import kotlin.String
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Instruction describing how to create a file via the apply_patch tool.
 */
@Serializable
public data class ApplyPatchCreateFileOperation(
  @Required
  public val type: Type = Type.CreateFile,
  public val path: String,
  public val diff: String,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("create_file")
    CreateFile("create_file"),
    ;
  }
}
