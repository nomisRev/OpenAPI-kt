package io.github.nomisrev.render.test.client.operations.body.multipart

import io.ktor.client.HttpClient
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.append
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlin.ByteArray
import kotlin.String

public interface Files {
  public suspend fun post(`file`: ByteArray, purpose: String)
}

internal class KtorFiles(
  private val client: HttpClient,
) : Files {
  override suspend fun post(`file`: ByteArray, purpose: String) {
    client.post("/files") {
      setBody(MultiPartFormDataContent(formData {
        append("file", file)
        append("purpose", purpose)
      }))
    }
  }
}
