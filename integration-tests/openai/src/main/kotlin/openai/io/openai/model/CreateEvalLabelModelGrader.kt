package io.openai.model

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A LabelModelGrader object which uses a model to assign labels to each item
 * in the evaluation.
 *
 */
@Serializable
public data class CreateEvalLabelModelGrader(
  public val type: Type,
  public val name: String,
  public val model: String,
  public val input: List<CreateEvalItem>,
  public val labels: List<String>,
  @SerialName("passing_labels")
  public val passingLabels: List<String>,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("label_model")
    LabelModel("label_model"),
    ;
  }
}
