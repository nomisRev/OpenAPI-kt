package io.github.model

import kotlin.Boolean
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A schema for the SPDX JSON format returned by the Dependency Graph.
 */
@Serializable
public data class DependencyGraphSpdxSbom(
  public val sbom: Sbom,
) {
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Sbom(
    @SerialName("SPDXID")
    public val spdxid: String,
    public val spdxVersion: String,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val comment: String? = null,
    public val creationInfo: CreationInfo,
    public val name: String,
    public val dataLicense: String,
    public val documentNamespace: String,
    public val packages: List<Packages>,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val relationships: List<Relationships>? = null,
  ) {
    @Serializable
    public data class CreationInfo(
      public val created: String,
      public val creators: List<String>,
    )

    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class Packages(
      @SerialName("SPDXID")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val spdxid: String? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val name: String? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val versionInfo: String? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val downloadLocation: String? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val filesAnalyzed: Boolean? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val licenseConcluded: String? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val licenseDeclared: String? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val supplier: String? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val copyrightText: String? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val externalRefs: List<ExternalRefs>? = null,
    ) {
      @Serializable
      public data class ExternalRefs(
        public val referenceCategory: String,
        public val referenceLocator: String,
        public val referenceType: String,
      )
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class Relationships(
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val relationshipType: String? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val spdxElementId: String? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val relatedSpdxElement: String? = null,
    )
  }
}
