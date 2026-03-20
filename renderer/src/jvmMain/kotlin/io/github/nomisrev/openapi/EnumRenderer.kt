package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeSpec
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable



fun Model.Enum.toTypeSpec(
    config: RenderConfig,
    parentInterface: ClassName? = null,
    serialName: String? = null,
    nameOverride: String? = null,
): TypeSpec {
    val className = context.toClassName(config)
    return TypeSpec.enumBuilder(nameOverride ?: className.simpleName)
        .addAnnotation(Serializable::class)
        .apply {
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


