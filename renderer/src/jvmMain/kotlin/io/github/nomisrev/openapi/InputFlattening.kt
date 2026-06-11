package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.Dynamic
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.LambdaTypeName
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.TypeVariableName
import com.squareup.kotlinpoet.WildcardTypeName

internal fun Model.publicInputModelOrSelf(): Model =
    flattenedScalarInputModel() ?: this

internal fun Model.publicInputTypeName(config: RenderConfig): TypeName =
    publicInputModelOrSelf().toTypeName(config)

private fun Model.flattenedScalarInputModel(): Model? = when (this) {
    is Model.ByteArray,
    is Model.Date,
    is Model.DateTime,
    is Model.Primitive,
    is Model.Uuid -> this

    is Model.Object -> {
        if (!isScalarWrapper || properties.size != 1 || additionalProperties != Model.Object.AdditionalProperties.False) {
            return null
        }

        val [propertyName, property] = properties.entries.single()
        if (propertyName != "value" || !property.isRequired) return null

        property.model.flattenedScalarInputModel()?.with(
            description = description ?: property.model.description,
            isNullable = isNullable,
            title = title ?: property.model.title,
        )
    }

    is Model.Collection,
    is Model.DiscriminatedObject,
    is Model.Enum,
    is Model.FreeFormJson,
    is Model.Reference,
    is Model.Union -> null
}

private val UuidType = ClassName("kotlin.uuid", "Uuid")
private val ExperimentalUuidApiType = ClassName("kotlin.uuid", "ExperimentalUuidApi")
private val OptInType = ClassName("kotlin", "OptIn")

@IgnorableReturnValue
internal fun FunSpec.Builder.addExperimentalUuidOptInIfNeeded(type: TypeName) =
    addExperimentalUuidOptInIfNeeded(listOf(type))

@IgnorableReturnValue
internal fun FunSpec.Builder.addExperimentalUuidOptInIfNeeded(types: List<TypeName>): FunSpec.Builder = apply {
    if (types.any(TypeName::usesExperimentalUuid)) {
        addAnnotation(
            AnnotationSpec.builder(OptInType)
                .addMember("%T::class", ExperimentalUuidApiType)
                .build()
        )
    }
}

@IgnorableReturnValue
internal fun TypeSpec.Builder.addExperimentalUuidOptInIfNeeded(types: List<TypeName>): TypeSpec.Builder = apply {
    if (types.any(TypeName::usesExperimentalUuid)) {
        addAnnotation(
            AnnotationSpec.builder(OptInType)
                .addMember("%T::class", ExperimentalUuidApiType)
                .build()
        )
    }
}

internal fun TypeName.usesExperimentalUuid(): Boolean =
    when (this) {
        is ClassName -> copy(nullable = false) == UuidType
        is ParameterizedTypeName -> rawType == UuidType || typeArguments.any(TypeName::usesExperimentalUuid)
        is TypeVariableName -> bounds.any(TypeName::usesExperimentalUuid)
        is WildcardTypeName -> inTypes.any(TypeName::usesExperimentalUuid) || outTypes.any(TypeName::usesExperimentalUuid)
        Dynamic,
        is LambdaTypeName -> false
    }
