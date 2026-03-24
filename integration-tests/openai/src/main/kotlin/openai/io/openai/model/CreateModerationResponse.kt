package io.openai.model

import kotlin.Boolean
import kotlin.Double
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents if a given text input is potentially harmful.
 */
@Serializable
public data class CreateModerationResponse(
  public val id: String,
  public val model: String,
  public val results: List<Results>,
) {
  @Serializable
  public data class Results(
    public val flagged: Boolean,
    public val categories: Categories,
    @SerialName("category_scores")
    public val categoryScores: CategoryScores,
    @SerialName("category_applied_input_types")
    public val categoryAppliedInputTypes: CategoryAppliedInputTypes,
  ) {
    /**
     * A list of the categories, and whether they are flagged or not.
     */
    @Serializable
    public data class Categories(
      public val hate: Boolean,
      @SerialName("hate/threatening")
      public val hateThreatening: Boolean,
      public val harassment: Boolean,
      @SerialName("harassment/threatening")
      public val harassmentThreatening: Boolean,
      public val illicit: Boolean?,
      @SerialName("illicit/violent")
      public val illicitViolent: Boolean?,
      @SerialName("self-harm")
      public val selfHarm: Boolean,
      @SerialName("self-harm/intent")
      public val selfHarmIntent: Boolean,
      @SerialName("self-harm/instructions")
      public val selfHarmInstructions: Boolean,
      public val sexual: Boolean,
      @SerialName("sexual/minors")
      public val sexualMinors: Boolean,
      public val violence: Boolean,
      @SerialName("violence/graphic")
      public val violenceGraphic: Boolean,
    )

    /**
     * A list of the categories along with the input type(s) that the score applies to.
     */
    @Serializable
    public data class CategoryAppliedInputTypes(
      public val hate: List<Hate>,
      @SerialName("hate/threatening")
      public val hateThreatening: List<HateThreatening>,
      public val harassment: List<Harassment>,
      @SerialName("harassment/threatening")
      public val harassmentThreatening: List<HarassmentThreatening>,
      public val illicit: List<Illicit>,
      @SerialName("illicit/violent")
      public val illicitViolent: List<IllicitViolent>,
      @SerialName("self-harm")
      public val selfHarm: List<SelfHarm>,
      @SerialName("self-harm/intent")
      public val selfHarmIntent: List<SelfHarmIntent>,
      @SerialName("self-harm/instructions")
      public val selfHarmInstructions: List<SelfHarmInstructions>,
      public val sexual: List<Sexual>,
      @SerialName("sexual/minors")
      public val sexualMinors: List<SexualMinors>,
      public val violence: List<Violence>,
      @SerialName("violence/graphic")
      public val violenceGraphic: List<ViolenceGraphic>,
    ) {
      @Serializable
      public enum class Harassment(
        public val `value`: String,
      ) {
        @SerialName("text")
        Text("text"),
        ;
      }

      @Serializable
      public enum class HarassmentThreatening(
        public val `value`: String,
      ) {
        @SerialName("text")
        Text("text"),
        ;
      }

      @Serializable
      public enum class Hate(
        public val `value`: String,
      ) {
        @SerialName("text")
        Text("text"),
        ;
      }

      @Serializable
      public enum class HateThreatening(
        public val `value`: String,
      ) {
        @SerialName("text")
        Text("text"),
        ;
      }

      @Serializable
      public enum class Illicit(
        public val `value`: String,
      ) {
        @SerialName("text")
        Text("text"),
        ;
      }

      @Serializable
      public enum class IllicitViolent(
        public val `value`: String,
      ) {
        @SerialName("text")
        Text("text"),
        ;
      }

      @Serializable
      public enum class SelfHarm(
        public val `value`: String,
      ) {
        @SerialName("text")
        Text("text"),
        @SerialName("image")
        Image("image"),
        ;
      }

      @Serializable
      public enum class SelfHarmInstructions(
        public val `value`: String,
      ) {
        @SerialName("text")
        Text("text"),
        @SerialName("image")
        Image("image"),
        ;
      }

      @Serializable
      public enum class SelfHarmIntent(
        public val `value`: String,
      ) {
        @SerialName("text")
        Text("text"),
        @SerialName("image")
        Image("image"),
        ;
      }

      @Serializable
      public enum class Sexual(
        public val `value`: String,
      ) {
        @SerialName("text")
        Text("text"),
        @SerialName("image")
        Image("image"),
        ;
      }

      @Serializable
      public enum class SexualMinors(
        public val `value`: String,
      ) {
        @SerialName("text")
        Text("text"),
        ;
      }

      @Serializable
      public enum class Violence(
        public val `value`: String,
      ) {
        @SerialName("text")
        Text("text"),
        @SerialName("image")
        Image("image"),
        ;
      }

      @Serializable
      public enum class ViolenceGraphic(
        public val `value`: String,
      ) {
        @SerialName("text")
        Text("text"),
        @SerialName("image")
        Image("image"),
        ;
      }
    }

    /**
     * A list of the categories along with their scores as predicted by model.
     */
    @Serializable
    public data class CategoryScores(
      public val hate: Double,
      @SerialName("hate/threatening")
      public val hateThreatening: Double,
      public val harassment: Double,
      @SerialName("harassment/threatening")
      public val harassmentThreatening: Double,
      public val illicit: Double,
      @SerialName("illicit/violent")
      public val illicitViolent: Double,
      @SerialName("self-harm")
      public val selfHarm: Double,
      @SerialName("self-harm/intent")
      public val selfHarmIntent: Double,
      @SerialName("self-harm/instructions")
      public val selfHarmInstructions: Double,
      public val sexual: Double,
      @SerialName("sexual/minors")
      public val sexualMinors: Double,
      public val violence: Double,
      @SerialName("violence/graphic")
      public val violenceGraphic: Double,
    )
  }
}
