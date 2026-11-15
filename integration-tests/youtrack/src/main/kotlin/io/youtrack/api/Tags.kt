package io.youtrack.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.delete
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.encodeURLParameter
import io.youtrack.model.IssueFolderRead
import io.youtrack.model.IssueFolderWrite
import io.youtrack.model.IssueRead
import kotlin.Int
import kotlin.String
import kotlin.collections.List

public class Tags internal constructor(
  private val __client: HttpClient,
) {
  public val `get`: Get = Get(__client)

  public val post: Post = Post(__client)

  public fun id(id: String): IdPath = IdPath(__client, id)

  public class Get internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke(
      query: String? = null,
      fields: String? = "${'$'}type,id,name,owner(${'$'}type,id,login,ringId)",
      skip: Int? = null,
      top: Int? = null,
    ): List<IssueFolderRead.Tag> {
      val response = __client.get("tags") {
        if (query != null) {
          this.url.encodedParameters.append("query", query.encodeURLParameter())
        }
        if (fields != null) {
          this.url.encodedParameters.append("fields", fields.encodeURLParameter())
        }
        if (skip != null) {
          this.url.encodedParameters.append("%24skip", skip.toString())
        }
        if (top != null) {
          this.url.encodedParameters.append("%24top", top.toString())
        }
      }
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body()
    }
  }

  public class Post internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke(fields: String? = "${'$'}type,id,name,owner(${'$'}type,id,login,ringId)", body: IssueFolderWrite.Tag? = null): IssueFolderRead.Tag {
      val response = __client.post("tags") {
        if (fields != null) {
          this.url.encodedParameters.append("fields", fields.encodeURLParameter())
        }
        body?.let {
          contentType(ContentType.Application.Json)
          setBody(it)
        }
      }
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body()
    }
  }

  public class IdPath internal constructor(
    private val __client: HttpClient,
    private val _id: String,
  ) {
    public val delete: Delete = Delete(__client, _id)

    public val `get`: Get = Get(__client, _id)

    public val post: Post = Post(__client, _id)

    public val issues: Issues = Issues(__client, _id)

    public class Delete internal constructor(
      private val __client: HttpClient,
      private val _id: String,
    ) {
      public suspend operator fun invoke() {
        val response = __client.delete("tags/${this._id.encodeURLParameter()}")
        if (response.status.value != 200) throw ResponseException(response, "")
      }
    }

    public class Get internal constructor(
      private val __client: HttpClient,
      private val _id: String,
    ) {
      public suspend operator fun invoke(fields: String? = "${'$'}type,id,name,owner(${'$'}type,id,login,ringId)"): IssueFolderRead.Tag {
        val response = __client.get("tags/${this._id.encodeURLParameter()}") {
          if (fields != null) {
            this.url.encodedParameters.append("fields", fields.encodeURLParameter())
          }
        }
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }

    public class Post internal constructor(
      private val __client: HttpClient,
      private val _id: String,
    ) {
      public suspend operator fun invoke(fields: String? = "${'$'}type,id,name,owner(${'$'}type,id,login,ringId)", body: IssueFolderWrite.Tag? = null): IssueFolderRead.Tag {
        val response = __client.post("tags/${this._id.encodeURLParameter()}") {
          if (fields != null) {
            this.url.encodedParameters.append("fields", fields.encodeURLParameter())
          }
          body?.let {
            contentType(ContentType.Application.Json)
            setBody(it)
          }
        }
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }

    public class Issues internal constructor(
      private val __client: HttpClient,
      private val _id: String,
    ) {
      public val `get`: Get = Get(__client, _id)

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _id: String,
      ) {
        public suspend operator fun invoke(
          customFields: String? = null,
          fields: String? = "${'$'}type,created,customFields(${'$'}type,id,name,value(${'$'}type,id,name)),description,id,idReadable,links(${'$'}type,direction,id,linkType(${'$'}type,id,localizedName,name)),numberInProject,project(${'$'}type,id,name,shortName),reporter(${'$'}type,id,login,ringId),resolved,summary,updated,updater(${'$'}type,id,login,ringId),visibility(${'$'}type,id,permittedGroups(${'$'}type,id,name,ringId),permittedUsers(${'$'}type,id,login,ringId))",
          skip: Int? = null,
          top: Int? = null,
        ): List<IssueRead> {
          val response = __client.get("tags/${this._id.encodeURLParameter()}/issues") {
            if (customFields != null) {
              this.url.encodedParameters.append("customFields", customFields.encodeURLParameter())
            }
            if (fields != null) {
              this.url.encodedParameters.append("fields", fields.encodeURLParameter())
            }
            if (skip != null) {
              this.url.encodedParameters.append("%24skip", skip.toString())
            }
            if (top != null) {
              this.url.encodedParameters.append("%24top", top.toString())
            }
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
      }
    }
  }
}
