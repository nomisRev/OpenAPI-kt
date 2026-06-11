package io.openai.model

import kotlin.OptIn
import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

/**
 * One of the create_file, delete_file, or update_file operations supplied to the apply_patch tool.
 */
@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("type")
@Serializable
public sealed interface ApplyPatchOperationParam {
  /**
   * Instruction for creating a new file via the apply_patch tool.
   */
  @SerialName("create_file")
  @Serializable
  public data class CreateFile(
    public val path: String,
    public val diff: String,
  ) : ApplyPatchOperationParam

  /**
   * Instruction for deleting an existing file via the apply_patch tool.
   */
  @JvmInline
  @SerialName("delete_file")
  @Serializable
  public value class DeleteFile(
    public val path: String,
  ) : ApplyPatchOperationParam

  /**
   * Instruction for updating an existing file via the apply_patch tool.
   */
  @SerialName("update_file")
  @Serializable
  public data class UpdateFile(
    public val path: String,
    public val diff: String,
  ) : ApplyPatchOperationParam
}
