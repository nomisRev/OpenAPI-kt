package io.github.nomisrev.openapi

sealed interface Model {
  val description: String?

  /**
   * Reference to a named component type. Used to preserve structure and avoid recursively expanding
   * schemas during transformation while still allowing code generation to refer to the correct
   * Kotlin type.
   */
  data class Reference(val context: NamingContext, override val description: String?) : Model

  sealed interface Primitive : Model {
    data class Int(
      val default: kotlin.Int?,
      override val description: kotlin.String?,
      val constraint: Constraints.Number?,
    ) : Primitive

    data class Double(
      val default: kotlin.Double?,
      override val description: kotlin.String?,
      val constraint: Constraints.Number?,
    ) : Primitive

    data class Boolean(val default: kotlin.Boolean?, override val description: kotlin.String?) :
      Primitive

    data class String(
      val default: kotlin.String?,
      override val description: kotlin.String?,
      val constraint: Constraints.Text?,
    ) : Primitive

    data class Unit(override val description: kotlin.String?) : Primitive

    fun default(): kotlin.String? =
      when (this) {
        is Int -> default?.toString()
        is Double -> default?.toString()
        is Boolean -> default?.toString()
        is String -> default?.let { "\"$it\"" }
        is Unit -> null
      }

    companion object
  }

  data class OctetStream(override val description: String?) : Model

  data class FreeFormJson(override val description: String?, val constraint: Constraints.Object?) :
    Model

  sealed interface Collection : Model {
    val inner: Model
    val constraint: Constraints.Collection?

    data class List(
      override val inner: Model,
      val default: kotlin.collections.List<String>?,
      override val description: String?,
      override val constraint: Constraints.Collection?,
    ) : Collection

    data class Map(
      override val inner: Model,
      override val description: String?,
      override val constraint: Constraints.Collection?,
    ) : Collection {
      val key = Primitive.String(null, null, null)
    }

    companion object
  }

  data class Object(
    val context: NamingContext,
    override val description: String?,
    val properties: List<Property>,
    val inline: List<Model>,
    /** When true, indicates additionalProperties: true (Any JSON) is allowed and should be handled. */
    val additionalProperties: Boolean = false,
  ) : Model {
    data class Property(
      val baseName: String,
      val model: Model,
      /**
       * isRequired != not-null. This means the value _has to be included_ in the payload, but it
       * might be [isNullable].
       */
      val isRequired: Boolean,
      val isNullable: Boolean,
      val description: String?,
    )

    companion object
  }

  data class Union(
    val context: NamingContext,
    val cases: List<Case>,
    val default: String?,
    override val description: String?,
    val inline: List<Model>,
    val discriminator: Discriminator?,
  ) : Model {
    data class Case(val context: NamingContext, val model: Model)
  }

  data class Discriminator(val propertyName: String, val mapping: Map<String, String>?)

  sealed interface Enum : Model {
    val context: NamingContext
    val values: List<String>
    val default: String?
    override val description: String?

    data class Closed(
      override val context: NamingContext,
      val inner: Model,
      override val values: List<String>,
      override val default: String?,
      override val description: String?,
    ) : Enum

    data class Open(
      override val context: NamingContext,
      override val values: List<String>,
      override val default: String?,
      override val description: String?,
    ) : Enum
  }

  companion object
}
