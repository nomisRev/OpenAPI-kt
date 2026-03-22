package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeSpec
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Returns true if this enum has at least one entry whose raw wire value differs from its
 * Kotlin entry name, meaning we need a `value: String` constructor parameter so that the
 * wire value can be retrieved without relying on `@SerialName` (which is only respected by
 * kotlinx.serialization, not by Ktor's `parameter()` / form-data `append()`).
 */
fun Model.Enum.needsValueProperty(): Boolean =
    values.any { rawValue ->
        val v = rawValue ?: "null"
        v != toEnumValueName(v).unescapeBackticks()
    }

fun Model.Enum.toTypeSpec(
    config: RenderConfig,
    parentInterface: ClassName? = null,
    serialName: String? = null,
    nameOverride: String? = null,
    overrideValueProperty: Boolean = false,
): TypeSpec {
    val className = context.toClassName(config)
    val withValue = needsValueProperty() || overrideValueProperty
    return TypeSpec.enumBuilder(nameOverride ?: className.simpleName)
        .addAnnotation(Serializable::class)
        .apply {
            if (withValue) {
                primaryConstructor(
                    FunSpec.constructorBuilder()
                        .addParameter(ParameterSpec.builder("value", STRING).build())
                        .build()
                )
                addProperty(
                    PropertySpec.builder("value", STRING)
                        .apply {
                            if (overrideValueProperty) {
                                addModifiers(KModifier.OVERRIDE)
                            }
                        }
                        .initializer("value")
                        .build()
                )
            }

            serialName?.let { value ->
                addAnnotation(
                    AnnotationSpec.builder(SerialName::class)
                        .addMember("%S", value)
                        .build()
                )
            }
            parentInterface?.let(::addSuperinterface)

            values.forEach { value ->
                val rawValue = value ?: "null"
                val entryName = toEnumValueName(rawValue)
                val entry = TypeSpec.anonymousClassBuilder()

                if (rawValue != entryName.unescapeBackticks()) {
                    entry.addAnnotation(AnnotationSpec.builder(SerialName::class).addMember("%S", rawValue).build())
                }
                // When the enum has a value constructor, every entry must supply the wire value,
                // even those whose rawValue matches their Kotlin entry name.
                if (withValue) {
                    entry.addSuperclassConstructorParameter("%S", rawValue)
                }

                if (KmpTarget.JS in config.targets && entryName.needsJsName()) {
                    entry.addAnnotation(
                        AnnotationSpec.builder(ClassName("kotlin.js", "JsName"))
                            .addMember("%S", entryName.toJsNameValue())
                            .build()
                    )
                }

                addEnumConstant(entryName, entry.build())
            }
        }
        .build()
}

fun Model.Enum.toFileSpec(config: RenderConfig): FileSpec {
    val className = context.toClassName(config)
    return FileSpec.builder(className.packageName, className.simpleName)
        .addType(toTypeSpec(config))
        .build()
}

