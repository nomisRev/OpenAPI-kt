package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.transformers.nestedOrNull
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
sealed interface Model {
    val description: String?
    val title: String?
    val isNullable: Boolean

    sealed interface ContextHolder {
        val context: NamingContext
    }

    fun with(
        description: String? = this.description,
        isNullable: Boolean = this.isNullable,
        title: String? = this.title
    ) = when (this) {
        is ByteArray -> copy(description = description, isNullable = isNullable, title = title)
        is Collection -> copy(description = description, isNullable = isNullable, title = title)
        is Date -> copy(description = description, isNullable = isNullable, title = title)
        is DateTime -> copy(description = description, isNullable = isNullable, title = title)
        is DiscriminatedObject -> copy(description = description, isNullable = isNullable, title = title)
        is Enum -> copy(description = description, isNullable = isNullable, title = title)
        is FreeFormJson -> copy(description = description, isNullable = isNullable, title = title)
        is Object -> copy(description = description, isNullable = isNullable, title = title)
        is Primitive.Boolean -> copy(description = description, isNullable = isNullable, title = title)
        is Primitive.Double -> copy(description = description, isNullable = isNullable, title = title)
        is Primitive.Float -> copy(description = description, isNullable = isNullable, title = title)
        is Primitive.Int -> copy(description = description, isNullable = isNullable, title = title)
        is Primitive.Long -> copy(description = description, isNullable = isNullable, title = title)
        is Primitive.String -> copy(description = description, isNullable = isNullable, title = title)
        is Primitive.Unit -> copy(description = description, isNullable = isNullable, title = title)
        is Reference -> copy(description = description, isNullable = isNullable, title = title)
        is Union -> copy(description = description, isNullable = isNullable, title = title)
        is Uuid -> copy(description = description, isNullable = isNullable, title = title)
    }

    /**
     * Reference to a named component type. Used to preserve structure and avoid recursively expanding
     * schemas during transformation while still allowing code generation to refer to the correct
     * Kotlin type.
     */
    @Serializable
    data class Reference(
        override val context: NamingContext,
        override val description: String?,
        override val isNullable: Boolean,
        override val title: String?
    ) : Model, ContextHolder

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
            override val isNullable: kotlin.Boolean,
            override val title: kotlin.String?
        ) : Primitive

        @SerialName("Long")
        @Serializable
        data class Long(
            val default: Default<kotlin.Long>?,
            override val description: kotlin.String?,
            val constraint: Constraints.Number?,
            override val isNullable: kotlin.Boolean,
            override val title: kotlin.String?
        ) : Primitive

        @SerialName("Float")
        @Serializable
        data class Float(
            val default: Default<kotlin.Float>?,
            override val description: kotlin.String?,
            val constraint: Constraints.Number?,
            override val isNullable: kotlin.Boolean,
            override val title: kotlin.String?
        ) : Primitive

        @SerialName("Double")
        @Serializable
        data class Double(
            val default: Default<kotlin.Double>?,
            override val description: kotlin.String?,
            val constraint: Constraints.Number?,
            override val isNullable: kotlin.Boolean,
            override val title: kotlin.String?
        ) : Primitive

        @SerialName("Boolean")
        @Serializable
        data class Boolean(
            val default: Default<kotlin.Boolean>?,
            override val description: kotlin.String?,
            override val isNullable: kotlin.Boolean,
            override val title: kotlin.String?
        ) : Primitive

        @SerialName("String")
        @Serializable
        data class String(
            val default: Default<kotlin.String>?,
            override val description: kotlin.String?,
            val constraint: Constraints.Text?,
            override val isNullable: kotlin.Boolean,
            override val title: kotlin.String?
        ) : Primitive

        @SerialName("Unit")
        @Serializable
        data class Unit(
            override val description: kotlin.String?,
            override val isNullable: kotlin.Boolean,
            override val title: kotlin.String?
        ) : Primitive

        companion object
    }

    @Serializable
    data class ByteArray(
        override val description: String?,
        override val isNullable: Boolean,
        override val title: String?
    ) : Model

    @Serializable
    data class Uuid(
        override val description: String?,
        override val isNullable: Boolean,
        override val title: String?
    ) : Model

    @Serializable
    data class Date(
        override val description: String?,
        override val isNullable: Boolean,
        override val title: String?
    ) : Model

    @Serializable
    data class DateTime(
        override val description: String?,
        override val isNullable: Boolean,
        override val title: String?
    ) : Model

    @Serializable
    data class FreeFormJson(
        override val description: String?,
        val constraint: Constraints.Object?,
        override val isNullable: Boolean,
        override val title: String?,
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

    @SerialName("List")
    @Serializable
    data class Collection(
        val inner: Model,
        val default: Default<List<String>>?,
        override val description: String?,
        val constraint: Constraints.Collection?,
        override val isNullable: Boolean,
        override val title: String?
    ) : Model

    @SerialName("Object")
    @Serializable
    data class Object(
        override val context: NamingContext,
        override val description: String?,
        override val title: String?,
        val properties: Map<String, Property>,
        val additionalProperties: AdditionalProperties,
        override val isNullable: Boolean,
        /**
         * `true` when the schema had properties in the spec but they were all stripped away by the
         * Read/Write context split (readOnly / writeOnly stripping). This flag distinguishes a
         * "schema with no properties by design" (should render as `data object`) from a "schema
         * whose properties were stripped" (must render as `data class`, possibly with zero
         * constructor parameters, to preserve correct serialization semantics).
         */
        val hadPropertiesBeforeStripping: Boolean = false,
        /**
         * `true` when this object is a generated wrapper around a referenced scalar schema.
         * The renderer keeps the wrapper model for request/response bodies, but may flatten it
         * at public input boundaries such as path/query/header/cookie parameters.
         */
        val isScalarWrapper: Boolean = false,
    ) : Model, ContextHolder {
        val inline: Set<Model> =
            properties.mapNotNullTo(mutableSetOf()) { (_, prop) -> prop.model.nestedOrNull() } +
                    setOfNotNull((additionalProperties as? AdditionalProperties.Schema)?.value?.nestedOrNull())

        constructor(
            context: NamingContext,
            description: String?,
            title: String?,
            properties: Map<String, Property>,
            additionalProperties: Boolean,
            isNullable: Boolean,
            isScalarWrapper: Boolean = false,
            hadPropertiesBeforeStripping: Boolean = false,
        ) : this(
            context,
            description,
            title,
            properties,
            AdditionalProperties.Allowed(additionalProperties),
            isNullable,
            hadPropertiesBeforeStripping,
            isScalarWrapper,
        )

        @Serializable
        sealed interface AdditionalProperties {
            @Serializable
            @SerialName("Allowed")
            data class Allowed(val value: Boolean) : AdditionalProperties

            @Serializable
            @SerialName("Schema")
            @JvmInline
            value class Schema(val value: Model) : AdditionalProperties

            companion object {
                val False = Allowed(false)
            }
        }

        @SerialName("Property")
        @Serializable
        data class Property(val model: Model, val isRequired: Boolean)

        companion object {
            fun value(
                context: NamingContext.Reference,
                property: Model,
                title: String? = null,
                isScalarWrapper: Boolean = false,
            ) = Object(
                NamingContext(context, emptyList()),
                property.description,
                title,
                mapOf("value" to Property(property.with(description = null, isNullable = false), true)),
                additionalProperties = AdditionalProperties.Allowed(false),
                property.isNullable,
                isScalarWrapper = isScalarWrapper,
            )
        }
    }

    @SerialName("Union")
    @Serializable
    data class Union(
        override val context: NamingContext,
        val cases: List<Case>,
        val default: Default<String>?,
        override val description: String?,
        override val title: String?,
        val discriminator: String?,
        override val isNullable: Boolean
    ) : Model, ContextHolder {
        val inline: Set<Model> = cases.mapNotNullTo(mutableSetOf()) { it.model.nestedOrNull() }

        // Introduce NamingContext?? Duplicated with inner NamingContext?
        // Alternative: compute naming
        @Serializable
        data class Case(
            val model: Model,
            val discriminator: String?
        )
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
        override val context: NamingContext,
        val abstractProperties: Map<String, Object.Property>,
        val subtypes: List<Object>,
        override val description: String?,
        override val title: String?,
        val discriminator: String,
        override val isNullable: Boolean
    ) : Model, ContextHolder

    @SerialName("Enum")
    @Serializable
    data class Enum(
        override val context: NamingContext,
        val inner: Model,
        val values: List<String?>,
        val default: Default<String>?,
        override val description: String?,
        override val title: String?,
        override val isNullable: Boolean,
    ) : Model, ContextHolder
}
