package io.github.nomisrev.openapi

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

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
        is Enum -> copy(description = description, isNullable = isNullable)
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
        data object Null : Default<Nothing>
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
    }

    @SerialName("Object")
    @Serializable
    data class Object(
        val context: NamingContext,
        override val description: String?,
        val properties: List<Property>,
        val inline: Set<Model>,
        val additionalProperties: AdditionalProperties,
        override val isNullable: Boolean
    ) : Model {
        constructor(
            context: NamingContext,
            description: String?,
            properties: List<Property>,
            inline: Set<Model>,
            additionalProperties: Boolean,
            isNullable: Boolean
        ) : this(
            context,
            description,
            properties,
            inline,
            AdditionalProperties.Allowed(additionalProperties),
            isNullable
        )

        @Serializable
        sealed interface AdditionalProperties {
            @Serializable
            @SerialName("Allowed")
            @JvmInline
            value class Allowed(val value: Boolean) : AdditionalProperties

            @Serializable
            @SerialName("Schema")
            @JvmInline
            value class Schema(val value: Model) : AdditionalProperties
        }

        @SerialName("Property")
        @Serializable
        data class Property(val baseName: String, val model: Model, val isRequired: Boolean)

        companion object {
            // TODO write proper tests for this
            fun value(context: NamingContext.Reference, property: Model, inline: Set<Model> = emptySet()) = Object(
                context,
                property.description,
                listOf(
                    Property(
                        "value",
                        property.with(description = null, isNullable = false),
                        true
                    )
                ),
                inline,
                additionalProperties = AdditionalProperties.Allowed(false),
                property.isNullable
            )
        }
    }

    @SerialName("Union")
    @Serializable
    data class Union(
        val context: NamingContext,
        val cases: List<Case>,
        val default: Default<String>?,
        override val description: String?,
        val inline: Set<Model>,
        val discriminator: String?,
        override val isNullable: Boolean
    ) : Model {
        @Serializable
        data class Case(val model: Model, val discriminator: String?)
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
    @SerialName("DiscriminatedObject")
    @Serializable
    data class DiscriminatedObject(
        val context: NamingContext,
        val baseObject: Object,
        val subtypes: List<Subtype>,
        val default: String?,
        override val description: String?,
        val discriminator: String?,
        val selfReference: Boolean,
        override val isNullable: Boolean
    ) : Model {
        @Serializable
        data class Subtype(val context: NamingContext, val model: Model, val discriminator: String?)
    }

    @SerialName("Enum")
    @Serializable
    data class Enum(
        val context: NamingContext,
        val inner: Model,
        val values: List<String?>,
        val default: Default<String>?,
        val isOpen: Boolean,
        override val description: String?,
        override val isNullable: Boolean,
    ) : Model
}
