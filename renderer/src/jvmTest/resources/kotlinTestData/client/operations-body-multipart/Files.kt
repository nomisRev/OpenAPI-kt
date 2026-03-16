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
  public val post: Post

  public interface Post {
    public suspend operator fun invoke(`file`: ByteArray, purpose: String)
  }
}

internal class KtorFiles(
  private val client: HttpClient,
) : Files {
  override val post: Files.Post = object : Files.Post {
    override suspend operator fun invoke(`file`: ByteArray, purpose: String) {
      client.post("/files") {
        setBody(MultiPartFormDataContent(formData {
          append("file", file)
          append("purpose", purpose)
        }))
      }
    }
  }
}
