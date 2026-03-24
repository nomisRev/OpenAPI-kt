package io.openai.model

import kotlin.String
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Instruction describing how to update a file via the apply_patch tool.
 */
@Serializable
public data class ApplyPatchUpdateFileOperation(
  @Required
  public val type: Type = Type.UpdateFile,
  public val path: String,
  public val diff: String,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("update_file")
    UpdateFile("update_file"),
    ;
  }
}
