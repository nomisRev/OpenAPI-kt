# ISSUE AUTO DETECT DISCRIMINATOR

A 'type' discriminator can be discovered automatically for following schema.
We should allow automatically discovering such cases which will result in more idiomatic code (`data object Auto`),
and more stable deserialization.

```kotlin
@Serializable(with = ChunkingStrategy.Serializer::class)
        public sealed interface ChunkingStrategy {
          /**
           * The default strategy. This strategy currently uses a `max_chunk_size_tokens` of `800` and `chunk_overlap_tokens` of `400`.
           */
          @JvmInline
          @Serializable
          public value class Auto(
            public val type: Type,
          ) : ChunkingStrategy {
            @Serializable
            public enum class Type(
              public val `value`: String,
            ) {
              @SerialName("auto")
              Auto("auto"),
              ;
            }
          }

          @Serializable
          public data class Static(
            public val type: Type,
            public val static: Static,
          ) : ChunkingStrategy {
            @Serializable
            public data class Static(
              @SerialName("max_chunk_size_tokens")
              public val maxChunkSizeTokens: Long,
              @SerialName("chunk_overlap_tokens")
              public val chunkOverlapTokens: Long,
            )

            @Serializable
            public enum class Type(
              public val `value`: String,
            ) {
              @SerialName("static")
              Static("static"),
              ;
            }
          }
```

