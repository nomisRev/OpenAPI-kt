package io.github.nomisrev.openapi

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface Model {
    val description: String?
    val isNullable: Boolean

    fun with(
        description: String? = this.description,
        isNullable: Boolean = this.isNullable,
    ) = when (this) {
        is ByteArray -> copy(description = description, isNullable = isNullable)
        is Collection.List -> copy(description = description, isNullable = isNullable)
        is Collection.Map -> copy(description = description, isNullable = isNullable)
        is Date -> copy(description = description, isNullable = isNullable)
        is DateTime -> copy(description = description, isNullable = isNullable)
        is DiscriminatedObject -> copy(description = description, isNullable = isNullable)
        is Enum.Closed -> copy(description = description, isNullable = isNullable)
        is Enum.Open -> copy(description = description, isNullable = isNullable)
        is FreeFormJson -> copy(description = description, isNullable = isNullable)
        is Object -> copy(description = description, isNullable = isNullable)
        is Primitive.Boolean -> copy(description = description, isNullable = isNullable)
        is Primitive.Double -> copy(description = description, isNullable = isNullable)
        is Primitive.Float -> copy(description = description, isNullable = isNullable)
        is Primitive.Int -> copy(description = description, isNullable = isNullable)
        is Primitive.Long -> copy(description = description, isNullable = isNullable)
        is Primitive.String -> copy(description = description, isNullable = isNullable)
        is Primitive.Unit -> copy(description = description, isNullable = isNullable)
        is Reference -> copy(description = description, isNullable = isNullable)
        is Union -> copy(description = description, isNullable = isNullable)
        is Uuid -> copy(description = description, isNullable = isNullable)
    }

    /**
     * Reference to a named component type. Used to preserve structure and avoid recursively expanding
     * schemas during transformation while still allowing code generation to refer to the correct
     * Kotlin type.
     */
    @Serializable
    data class Reference(
        val context: NamingContext,
        override val description: String?,
        override val isNullable: Boolean
    ) : Model

    @Serializable
    sealed interface Default<out A : Any> {
        @Serializable
        @SerialName("Value")
        data class Value<A : Any>(val value: A) : Default<A>

        /**
         * Guaranteed to never be set for a `!isNullable` schema. Leniency mode decides error mode.
         *
         * Alternative is to complicate `isNullable` to include defaults, and/or split nullable and non-null hierarchy.
         * Both would complicate the codebase and have been decided against.
         */
        @Serializable
        @SerialName("Null")
        data object Null : Default<Nothing> {
            override fun toString(): String = "null"
        }

        companion object
    }

    @Serializable
    sealed interface Primitive : Model {
        @SerialName("Int")
        @Serializable
        data class Int(
            val default: Default<kotlin.Int>?,
            override val description: kotlin.String?,
            val constraint: Constraints.Number?,
            override val isNullable: kotlin.Boolean
        ) : Primitive

        @SerialName("Long")
        @Serializable
        data class Long(
            val default: Default<kotlin.Long>?,
            override val description: kotlin.String?,
            val constraint: Constraints.Number?,
            override val isNullable: kotlin.Boolean
        ) : Primitive

        @SerialName("Float")
        @Serializable
        data class Float(
            val default: Default<kotlin.Float>?,
            override val description: kotlin.String?,
            val constraint: Constraints.Number?,
            override val isNullable: kotlin.Boolean
        ) : Primitive

        @SerialName("Double")
        @Serializable
        data class Double(
            val default: Default<kotlin.Double>?,
            override val description: kotlin.String?,
            val constraint: Constraints.Number?,
            override val isNullable: kotlin.Boolean
        ) : Primitive

        @SerialName("Boolean")
        @Serializable
        data class Boolean(
            val default: Default<kotlin.Boolean>?,
            override val description: kotlin.String?,
            override val isNullable: kotlin.Boolean
        ) : Primitive

        @SerialName("String")
        @Serializable
        data class String(
            val default: Default<kotlin.String>?,
            override val description: kotlin.String?,
            val constraint: Constraints.Text?,
            override val isNullable: kotlin.Boolean
        ) : Primitive

        @SerialName("Unit")
        @Serializable
        data class Unit(
            override val description: kotlin.String?,
            override val isNullable: kotlin.Boolean
        ) : Primitive

        companion object
    }

    @Serializable
    data class ByteArray(
        override val description: String?,
        override val isNullable: Boolean
    ) : Model

    @Serializable
    data class Uuid(
        override val description: String?,
        override val isNullable: Boolean
    ) : Model

    @Serializable
    data class Date(
        override val description: String?,
        override val isNullable: Boolean
    ) : Model

    @Serializable
    data class DateTime(
        override val description: String?,
        override val isNullable: Boolean
    ) : Model

    @Serializable
    data class FreeFormJson(
        override val description: String?,
        val constraint: Constraints.Object?,
        override val isNullable: Boolean,
        //  val default: Default<String>?
    ) : Model {
        // TODO support default values
        //     val value: JsonElement?
        //         get() = when (default) {
        //             Default.Null -> JsonNull
        //             is Default.Value<String> -> Json.parseToJsonElement(default.value)
        //             null -> null
        //         }
    }

    @Serializable
    sealed interface Collection : Model {
        val inner: Model
        val constraint: Constraints.Collection?

        @SerialName("List")
        @Serializable
        data class List(
            override val inner: Model,
            val default: Default<kotlin.collections.List<String>>?,
            override val description: String?,
            override val constraint: Constraints.Collection?,
            override val isNullable: Boolean
        ) : Collection

        @SerialName("Map")
        @Serializable
        data class Map(
            override val inner: Model,
            override val description: String?,
            override val constraint: Constraints.Collection?,
            override val isNullable: Boolean
        ) : Collection {
            val key = Primitive.String(null, null, null, false)
        }

        companion object
    }

    @SerialName("Object")
    @Serializable
    data class Object(
        val context: NamingContext,
        override val description: String?,
        val properties: List<Property>,
        val inline: Set<Model>,
        /** When true, indicates additionalProperties: true (Any JSON) is allowed and should be handled. */
        val additionalProperties: Boolean = false,
        override val isNullable: Boolean
    ) : Model {
        @SerialName("Property")
        @Serializable
        data class Property(
            val baseName: String,
            val model: Model,
            val isRequired: Boolean,
            val description: String?,
        )

        companion object {
            // TODO write proper tests for this
            fun value(context: NamingContext.Reference, property: Model, inline: Set<Model> = emptySet()) = Object(
                context,
                property.description,
                listOf(
                    Property(
                        "value",
                        property.with(description = null, isNullable = false),
                        true,
                        null
                    )
                ),
                inline,
                additionalProperties = false,
                property.isNullable
            )
        }
    }

    @SerialName("Union")
    @Serializable
    data class Union(
        val context: NamingContext,
        val cases: List<Model>,
        val default: Default<String>?,
        override val description: String?,
        val inline: Set<Model>,
        val discriminator: Discriminator?,
        override val isNullable: Boolean
    ) : Model

    /**
     * Represents a discriminated object pattern - an inheritance-based type system where:
     * - A base schema defines common properties and a discriminator field
     * - Subtype schemas use allOf to inherit from the base and add their own properties
     * - The discriminator mapping identifies which subtype to use based on a property value
     *
     * This is distinct from Union (oneOf/anyOf) as it represents true inheritance with
     * shared base properties rather than a choice between unrelated alternatives.
     */
    @SerialName("DiscriminatedObject")
    @Serializable
    data class DiscriminatedObject(
        val context: NamingContext,
        val baseObject: Object,
        val subtypes: List<Subtype>,
        val default: String?,
        override val description: String?,
        val discriminator: Discriminator,
        val selfReference: Boolean,
        override val isNullable: Boolean
    ) : Model {
        @Serializable
        data class Subtype(val context: NamingContext, val model: Model, val discriminatorValue: String)
    }

    @SerialName("Discriminator")
    @Serializable
    data class Discriminator(val propertyName: String, val mapping: Map<String, String>?)

    @Serializable
    sealed interface Enum : Model {
        val context: NamingContext
        val values: List<String?>
        val default: Default<String>?
        override val description: String?

        @SerialName("ClosedEnum")
        @Serializable
        data class Closed(
            override val context: NamingContext,
            val inner: Model,
            override val values: List<String?>,
            override val default: Model.Default<String>?,
            override val description: String?,
            override val isNullable: Boolean
        ) : Enum

        @SerialName("EnumOpen")
        @Serializable
        data class Open(
            override val context: NamingContext,
            override val values: List<String>,
            override val default: Default<String>?,
            override val description: String?,
            override val isNullable: Boolean
        ) : Enum
    }
    companion object
}
