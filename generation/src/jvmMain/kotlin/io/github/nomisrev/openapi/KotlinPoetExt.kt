package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec

fun TypeName.nullable(): TypeName = copy(nullable = true)

inline fun <reified A : Annotation> annotationSpec(): AnnotationSpec =
  AnnotationSpec.builder(A::class).build()

fun TypeSpec.Builder.description(kdoc: String?): TypeSpec.Builder = apply {
  kdoc?.let { addKdoc("%L", it) }
}

fun ParameterSpec.Builder.description(kdoc: String?): ParameterSpec.Builder = apply {
  kdoc?.let { addKdoc("%L", it) }
}

fun TypeSpec.Companion.dataClassBuilder(
  className: ClassName,
  parameters: List<ParameterSpec>
): TypeSpec.Builder =
  classBuilder(className)
    .addModifiers(KModifier.DATA)
    .primaryConstructor(FunSpec.constructorBuilder().addParameters(parameters).build())
    .addProperties(
      parameters.map { param ->
        PropertySpec.builder(param.name, param.type).initializer(param.name).build()
      }
    )
