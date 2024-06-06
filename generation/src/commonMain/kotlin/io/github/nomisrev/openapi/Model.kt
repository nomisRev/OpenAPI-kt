package io.github.nomisrev.openapi

import io.exoquery.fansi.Str
import io.github.nomisrev.openapi.Schema.Type
import io.github.nomisrev.openapi.http.MediaType
import io.github.nomisrev.openapi.http.Method
import io.github.nomisrev.openapi.http.StatusCode
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

data class Route(
    val operation: Operation,
    val path: String,
    val method: Method,
    val body: Bodies,
    val input: List<Input>,
    val returnType: Returns,
    val extensions: Map<String, JsonElement>
) {

    data class Bodies(
        val types: Map<MediaType, Body>,
        val extensions: Map<String, JsonElement>
    ) : Map<MediaType, Body> by types

    // Required, isNullable
    sealed interface Body {
        val extensions: Map<String, JsonElement>

        data class OctetStream(override val extensions: Map<String, JsonElement>) : Body
        data class Json(val type: Model, override val extensions: Map<String, JsonElement>) : Body
        data class Xml(val type: Model, override val extensions: Map<String, JsonElement>) : Body

        data class Multipart(val parameters: List<FormData>, override val extensions: Map<String, JsonElement>) :
            Body, List<Multipart.FormData> by parameters {
            data class FormData(val name: String, val type: Model)
        }
    }

    // A Parameter can be isNullable, required while the model is not!
    sealed interface Input {
        val name: String
        val type: Model

        data class Query(override val name: String, override val type: Model) : Input
        data class Path(override val name: String, override val type: Model) : Input
        data class Header(override val name: String, override val type: Model) : Input
        data class Cookie(override val name: String, override val type: Model) : Input
    }

    data class Returns(
        val types: Map<StatusCode, ReturnType>,
        val extensions: Map<String, JsonElement>
    ) : Map<StatusCode, ReturnType> by types

    // Required, isNullable ???
    data class ReturnType(
        val type: Model,
        val extensions: Map<String, JsonElement>
    )
}

/**
 * Our own "Generated" oriented KModel.
 * The goal of this KModel is to make generation as easy as possible,
 * so we gather all information ahead of time.
 *
 * This KModel can/should be updated overtime to include all information we need for code generation.
 *
 * The naming mechanism forces the same ordering as defined in the OpenAPI Specification,
 * this gives us the best logical structure, and makes it easier to compare code and spec.
 * Every type that needs to generate a name has a [NamingContext], see [NamingContext] for more details.
 */
sealed interface Model {

    sealed interface Primitive : Model {
        data class Int(val schema: Schema, val default: kotlin.Int?) : Primitive
        data class Double(val schema: Schema, val default: kotlin.Double?) : Primitive
        data class Boolean(val schema: Schema, val default: kotlin.Boolean?) : Primitive
        data class String(val schema: Schema, val default: kotlin.String?) : Primitive
        data object Unit : Primitive

      fun default(): kotlin.String? =
        when (this) {
          is Int -> default?.toString()
          is Double -> default?.toString()
          is Boolean -> default?.toString()
          is String -> default?.let { "\"$it\"" }
          is Unit -> null
        }
    }

    data object Binary : Model
    data object FreeFormJson : Model

    sealed interface Collection : Model {
        val value: Model
        val schema: Schema

        data class List(
            override val schema: Schema,
            override val value: Model,
            val default: kotlin.collections.List<String>?
        ) : Collection

        data class Set(
            override val schema: Schema,
            override val value: Model,
            val default: kotlin.collections.List<String>?
        ) : Collection

        data class Map(
            override val schema: Schema,
            override val value: Model
        ) : Collection {
            val key = Primitive.String(Schema(type = Schema.Type.Basic.String), null)
        }
    }

    @Serializable
    data class Object(
        val schema: Schema,
        val context: NamingContext,
        val description: String?,
        val properties: List<Property>,
        val inline: List<Model>
    ) : Model {
        @Serializable
        data class Property(
            val baseName: String,
            val name: String,
            val type: Model,
            /**
             * isRequired != not-null.
             * This means the value _has to be included_ in the payload,
             * but it might be [isNullable].
             */
            val isRequired: Boolean,
            val isNullable: Boolean,
            val description: String?
        ) {
            sealed interface DefaultArgument {
                data class Enum(val enum: Model, val context: NamingContext, val value: String) : DefaultArgument
                data class Union(
                    val union: NamingContext,
                    val case: Model,
                    val value: String
                ) : DefaultArgument

                data class Double(val value: kotlin.Double) : DefaultArgument
                data class Int(val value: kotlin.Int) : DefaultArgument
                data class List(val value: kotlin.collections.List<DefaultArgument>) : DefaultArgument
                data class Other(val value: String) : DefaultArgument
            }
        }
    }

    sealed interface Union : Model {
        val schema: Schema
        val context: NamingContext
        val schemas: List<UnionEntry>
        val inline: List<Model>

        fun isOpenEnumeration(): Boolean =
            this is AnyOf &&
                    schemas.size == 2
                        && schemas.count { it.model is Enum } == 1
                        && schemas.count { it.model is Primitive.String } == 1

        data class UnionEntry(val context: NamingContext, val model: Model)

        /**
         * [OneOf] is an untagged union.
         * This is in Kotlin represented by a `sealed interface`.
         */
        data class OneOf(
            override val schema: Schema,
            override val context: NamingContext,
            override val schemas: List<UnionEntry>,
            override val inline: List<Model>,
            val default: String?
        ) : Union

        /**
         * [AnyOf] is an untagged union, with overlapping schema.
         *
         * Typically:
         *  - open enumeration: anyOf a `string`, and [Enum] (also type: `string`).
         *  - [Object] with a [FreeFormJson], where [FreeFormJson] has overlapping schema with the [Object].
         */
        data class AnyOf(
            override val schema: Schema,
            override val context: NamingContext,
            override val schemas: List<UnionEntry>,
            override val inline: List<Model>,
            val default: String?
        ) : Union

        /**
         * [TypeArray]
         */
        data class TypeArray(
            override val schema: Schema,
            override val context: NamingContext,
            override val schemas: List<UnionEntry>,
            override val inline: List<Model>
        ) : Union
    }

    data class Enum(
        val schema: Schema,
        val context: NamingContext,
        val inner: Model,
        val values: List<String>,
        val default: String?
    ) : Model
}
