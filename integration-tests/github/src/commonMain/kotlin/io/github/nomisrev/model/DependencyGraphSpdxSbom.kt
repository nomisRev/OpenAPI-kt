package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName

@Serializable
@JvmInline
value class DependencyGraphSpdxSbom(val sbom: Sbom) {
    @Serializable
    data class Sbom(
        @SerialName("SPDXID") val sPDXID: String,
        val spdxVersion: String,
        val comment: String? = null,
        val creationInfo: CreationInfo,
        val name: String,
        val dataLicense: String,
        val documentNamespace: String,
        val packages: List<Packages>,
        val relationships: List<Relationships>? = null,
    ) {
        @Serializable
        data class CreationInfo(val created: String, val creators: List<String>)

        @Serializable
        data class Packages(
            @SerialName("SPDXID") val sPDXID: String? = null,
            val name: String? = null,
            val versionInfo: String? = null,
            val downloadLocation: String? = null,
            val filesAnalyzed: Boolean? = null,
            val licenseConcluded: String? = null,
            val licenseDeclared: String? = null,
            val supplier: String? = null,
            val copyrightText: String? = null,
            val externalRefs: List<ExternalRefs>? = null,
        ) {
            @Serializable
            data class ExternalRefs(val referenceCategory: String, val referenceLocator: String, val referenceType: String)
        }

        @Serializable
        data class Relationships(
            val relationshipType: String? = null,
            val spdxElementId: String? = null,
            val relatedSpdxElement: String? = null,
        )
    }
}
