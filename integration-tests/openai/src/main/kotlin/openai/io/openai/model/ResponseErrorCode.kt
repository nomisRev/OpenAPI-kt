package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class ResponseErrorCode(
  public val `value`: String,
) {
  @SerialName("server_error")
  ServerError("server_error"),
  @SerialName("rate_limit_exceeded")
  RateLimitExceeded("rate_limit_exceeded"),
  @SerialName("invalid_prompt")
  InvalidPrompt("invalid_prompt"),
  @SerialName("vector_store_timeout")
  VectorStoreTimeout("vector_store_timeout"),
  @SerialName("invalid_image")
  InvalidImage("invalid_image"),
  @SerialName("invalid_image_format")
  InvalidImageFormat("invalid_image_format"),
  @SerialName("invalid_base64_image")
  InvalidBase64Image("invalid_base64_image"),
  @SerialName("invalid_image_url")
  InvalidImageUrl("invalid_image_url"),
  @SerialName("image_too_large")
  ImageTooLarge("image_too_large"),
  @SerialName("image_too_small")
  ImageTooSmall("image_too_small"),
  @SerialName("image_parse_error")
  ImageParseError("image_parse_error"),
  @SerialName("image_content_policy_violation")
  ImageContentPolicyViolation("image_content_policy_violation"),
  @SerialName("invalid_image_mode")
  InvalidImageMode("invalid_image_mode"),
  @SerialName("image_file_too_large")
  ImageFileTooLarge("image_file_too_large"),
  @SerialName("unsupported_image_media_type")
  UnsupportedImageMediaType("unsupported_image_media_type"),
  @SerialName("empty_image_file")
  EmptyImageFile("empty_image_file"),
  @SerialName("failed_to_download_image")
  FailedToDownloadImage("failed_to_download_image"),
  @SerialName("image_file_not_found")
  ImageFileNotFound("image_file_not_found"),
  ;
}
