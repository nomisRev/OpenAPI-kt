package io.openai.model

import kotlinx.serialization.Serializable

@Serializable
public enum class ContainerMemoryLimit {
  `1g`,
  `4g`,
  `16g`,
  `64g`,
}
