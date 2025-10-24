package io.github.nomisrev.openapi

/**
 * Strategy for resolving schema references during model transformation.
 * Different strategies are needed depending on the context:
 * - Component initialization: Create Model.Reference for component schemas
 * - Inline processing: Use ComponentRegistry to resolve to actual models
 */
sealed interface SchemaResolutionStrategy {

  /**
   * Strategy used during component registry initialization.
   * Creates Model.Reference entries for component schemas to avoid infinite recursion.
   */
  data object ForComponents : SchemaResolutionStrategy

  /**
   * Strategy used when processing inline schemas in routes.
   * Uses ComponentRegistry to resolve references to actual models when available.
   */
  data object ForInline : SchemaResolutionStrategy
}

/**
 * Resolution context that includes both naming context and resolution strategy.
 */
data class ModelResolutionContext(
  val naming: NamingContext,
  val strategy: SchemaResolutionStrategy,
  val currentAnchor: Pair<String, Schema>? = null
)
