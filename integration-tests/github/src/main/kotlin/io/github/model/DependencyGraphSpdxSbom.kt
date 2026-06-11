package io.github.model

import kotlin.Boolean
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A schema for the SPDX JSON format returned by the Dependency Graph.
 */
@JvmInline
@Serializable
public value class DependencyGraphSpdxSbom(
  public val sbom: Sbom,
) {
  @Serializable
  public data class Sbom(
    @SerialName("SPDXID")
    public val spdxid: String,
    public val spdxVersion: String,
    public val comment: String? = null,
    public val creationInfo: CreationInfo,
    public val name: String,
    public val dataLicense: String,
    public val documentNamespace: String,
    public val packages: List<Packages>,
    public val relationships: List<Relationships>? = null,
  ) {
    @Serializable
    public data class CreationInfo(
      public val created: String,
      public val creators: List<String>,
    )

    @Serializable
    public data class Packages(
      @SerialName("SPDXID")
      public val spdxid: String? = null,
      public val name: String? = null,
      public val versionInfo: String? = null,
      public val downloadLocation: String? = null,
      public val filesAnalyzed: Boolean? = null,
      public val licenseConcluded: String? = null,
      public val licenseDeclared: String? = null,
      public val supplier: String? = null,
      public val copyrightText: String? = null,
      public val externalRefs: List<ExternalRefs>? = null,
    ) {
      @Serializable
      public data class ExternalRefs(
        public val referenceCategory: String,
        public val referenceLocator: String,
        public val referenceType: String,
      )
    }

    @Serializable
    public data class Relationships(
      public val relationshipType: String? = null,
      public val spdxElementId: String? = null,
      public val relatedSpdxElement: String? = null,
    )
  }
}
