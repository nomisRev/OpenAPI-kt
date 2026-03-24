package io.openai.model

import kotlinx.serialization.Serializable

@Serializable
public enum class VideoSize {
  `720x1280`,
  `1280x720`,
  `1024x1792`,
  `1792x1024`,
}
