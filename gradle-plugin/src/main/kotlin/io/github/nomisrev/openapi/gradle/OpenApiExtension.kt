package io.github.nomisrev.openapi.gradle

import javax.inject.Inject
import org.gradle.api.model.ObjectFactory
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty

abstract class OpenApiExtension @Inject constructor(objects: ObjectFactory) {
    val specFile: RegularFileProperty = objects.fileProperty()
    val modelPackage: Property<String> = objects.property(String::class.java)
    val apiPackage: Property<String> = objects.property(String::class.java)
    val targets: SetProperty<String> = objects.setProperty(String::class.java)
}
