package io.openai.model

import kotlin.String
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Instruction describing how to delete a file via the apply_patch tool.
 */
@Serializable
public data class ApplyPatchDeleteFileOperation(
  @Required
  public val type: Type = Type.DeleteFile,
  public val path: String,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("delete_file")
    DeleteFile("delete_file"),
    ;
  }
}
