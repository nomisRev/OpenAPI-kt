package io.github.nomisrev.openapi

import kotlinx.serialization.Serializable

@Serializable
sealed interface Model {
    val description: kotlin.String?
    val isNullable: kotlin.Boolean

    /**
     * Reference to a named component type. Used to preserve structure and avoid recursively expanding
     * schemas during transformation while still allowing code generation to refer to the correct
     * Kotlin type.
     */
    @Serializable
    data class Reference(
        val context: NamingContext,
        override val description: kotlin.String?,
        override val isNullable: kotlin.Boolean
    ) : Model

    sealed interface Default<out A : Any> {
        data class Value<A : Any>(val value: A) : Default<A>

        /**
         * Guaranteed to never be set for a `!isNullable` schema. Leniency mode decides error mode.
         *
         *
         * Alternative is to complicate `isNullable` to include defaults, and/or split nullable and non-null hierarchy.
         * Both would complicate the codebase and have been decided against.
         */
        data object Null : Default<Nothing> {
            override fun toString(): String = "null"
        }

        companion object
    }

    @Serializable
    sealed interface Primitive : Model {
        @Serializable
        data class Int(
            val default: Default<kotlin.Int>?,
            override val description: kotlin.String?,
            val constraint: Constraints.Number?,
            override val isNullable: kotlin.Boolean
        ) : Primitive

        @Serializable
        data class Long(
            val default: Default<kotlin.Long>?,
            override val description: kotlin.String?,
            val constraint: Constraints.Number?,
            override val isNullable: kotlin.Boolean
        ) : Primitive

        @Serializable
        data class Float(
            val default: Default<kotlin.Float>?,
            override val description: kotlin.String?,
            val constraint: Constraints.Number?,
            override val isNullable: kotlin.Boolean
        ) : Primitive

        @Serializable
        data class Double(
            val default: Default<kotlin.Double>?,
            override val description: kotlin.String?,
            val constraint: Constraints.Number?,
            override val isNullable: kotlin.Boolean
        ) : Primitive

        // TODO: What about Boolean? with default = null.
        @Serializable
        data class Boolean(
            val default: Default<kotlin.Boolean>?,
            override val description: kotlin.String?,
            override val isNullable: kotlin.Boolean
        ) : Primitive

        @Serializable
        data class String(
            val default: Default<kotlin.String>?,
            override val description: kotlin.String?,
            val constraint: Constraints.Text?,
            override val isNullable: kotlin.Boolean
        ) : Primitive

        @Serializable
        data class Unit(
            override val description: kotlin.String?,
            override val isNullable: kotlin.Boolean
        ) : Primitive

        fun default(): kotlin.String? =
            when (this) {
                is Int -> default?.toString()
                is Double -> default?.toString()
                is Boolean -> default?.toString()
                is Float -> default?.toString()
                is Long -> default?.toString()
                is String -> default?.let { "\"$it\"" }
                is Unit -> null
            }

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
        override val description: kotlin.String?,
        val constraint: Constraints.Object?,
        override val isNullable: kotlin.Boolean
    ) : Model

    @Serializable
    sealed interface Collection : Model {
        val inner: Model
        val constraint: Constraints.Collection?

        @Serializable
        data class List(
            override val inner: Model,
            val default: kotlin.collections.List<String>?,
            override val description: kotlin.String?,
            override val constraint: Constraints.Collection?,
            override val isNullable: kotlin.Boolean
        ) : Collection

        @Serializable
        data class Map(
            override val inner: Model,
            override val description: kotlin.String?,
            override val constraint: Constraints.Collection?,
            override val isNullable: kotlin.Boolean
        ) : Collection {
            val key = Primitive.String(null, null, null, false)
        }

        companion object
    }

    @Serializable
    data class Object(
        val context: NamingContext,
        override val description: kotlin.String?,
        val properties: List<Property>,
        val inline: Set<Model>,
        /** When true, indicates additionalProperties: true (Any JSON) is allowed and should be handled. */
        val additionalProperties: kotlin.Boolean = false,
        override val isNullable: kotlin.Boolean
    ) : Model {
        @Serializable
        data class Property(
            val baseName: kotlin.String,
            val model: Model,
            /**
             * isRequired != not-null. This means the value _has to be included_ in the payload.
             * The nullability of the value itself is encoded in [model.isNullable].
             */
            val isRequired: kotlin.Boolean,
            val description: kotlin.String?,
        )

        companion object
    }

    @Serializable
    data class Union(
        val context: NamingContext,
        val cases: List<Case>,
        val default: String?,
        override val description: kotlin.String?,
        val inline: Set<Model>,
        val discriminator: Discriminator?,
        override val isNullable: kotlin.Boolean
    ) : Model {
        // TODO remove `context`, and flatten `Case` into just `Model`
        @Serializable
        data class Case(val context: NamingContext, val model: Model)
    }

    /**
     * Represents a discriminated object pattern - an inheritance-based type system where:
     * - A base schema defines common properties and a discriminator field
     * - Subtype schemas use allOf to inherit from the base and add their own properties
     * - The discriminator mapping identifies which subtype to use based on a property value
     *
     * This is distinct from Union (oneOf/anyOf) as it represents true inheritance with
     * shared base properties rather than a choice between unrelated alternatives.
     */
    @Serializable
    data class DiscriminatedObject(
        val context: NamingContext,
        /** The base object model with common properties */
        val baseObject: Object,
        /** Subtype cases that inherit from the base */
        val subtypes: List<Subtype>,
        val default: kotlin.String?,
        override val description: kotlin.String?,
        val discriminator: Discriminator,
        val selfReference: kotlin.Boolean,
        override val isNullable: kotlin.Boolean
    ) : Model {
        @Serializable
        data class Subtype(
            val context: NamingContext,
            val model: Model,
            /** The discriminator value that identifies this subtype */
            val discriminatorValue: String
        )
    }

    @Serializable
    data class Discriminator(val propertyName: String, val mapping: Map<String, String>?)

    @Serializable
    sealed interface Enum : Model {
        val context: NamingContext
        val values: List<String>
        val default: kotlin.String?
        override val description: kotlin.String?

        @Serializable
        data class Closed(
            override val context: NamingContext,
            val inner: Model,
            override val values: List<String>,
            override val default: kotlin.String?,
            override val description: kotlin.String?,
            override val isNullable: kotlin.Boolean
        ) : Enum

        @Serializable
        data class Open(
            override val context: NamingContext,
            override val values: List<String>,
            override val default: kotlin.String?,
            override val description: kotlin.String?,
            override val isNullable: kotlin.Boolean
        ) : Enum
    }

    companion object
}
