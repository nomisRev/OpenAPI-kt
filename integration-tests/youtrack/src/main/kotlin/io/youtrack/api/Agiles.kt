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
import io.youtrack.model.AgileRead
import io.youtrack.model.AgileWrite
import io.youtrack.model.SprintRead
import io.youtrack.model.SprintWrite
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.collections.List

public class Agiles internal constructor(
  private val __client: HttpClient,
) {
  public val `get`: Get = Get(__client)

  public val post: Post = Post(__client)

  public fun id(id: String): IdPath = IdPath(__client, id)

  public class Get internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke(
      fields: String? = "${'$'}type,columnSettings(${'$'}type,columns(${'$'}type,id),field(${'$'}type,fieldType(${'$'}type,id),id,localizedName,name),id),id,name,owner(${'$'}type,id,login,ringId),projects(${'$'}type,id,name,shortName),status(${'$'}type,id,valid),swimlaneSettings(${'$'}type,enabled,field(${'$'}type,customField(${'$'}type,fieldType(${'$'}type,id),id,localizedName,name),id,name),id,values(${'$'}type,id,name))",
      skip: Int? = null,
      top: Int? = null,
    ): List<AgileRead> {
      val response = __client.get("agiles") {
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
    public suspend operator fun invoke(
      template: String? = null,
      fields: String? = "${'$'}type,columnSettings(${'$'}type,columns(${'$'}type,id),field(${'$'}type,fieldType(${'$'}type,id),id,localizedName,name),id),id,name,owner(${'$'}type,id,login,ringId),projects(${'$'}type,id,name,shortName),status(${'$'}type,id,valid),swimlaneSettings(${'$'}type,enabled,field(${'$'}type,customField(${'$'}type,fieldType(${'$'}type,id),id,localizedName,name),id,name),id,values(${'$'}type,id,name))",
      body: AgileWrite? = null,
    ): AgileRead {
      val response = __client.post("agiles") {
        if (template != null) {
          this.url.encodedParameters.append("template", template.encodeURLParameter())
        }
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

    public val sprints: Sprints = Sprints(__client, _id)

    public class Delete internal constructor(
      private val __client: HttpClient,
      private val _id: String,
    ) {
      public suspend operator fun invoke() {
        val response = __client.delete("agiles/${this._id.encodeURLParameter()}")
        if (response.status.value != 200) throw ResponseException(response, "")
      }
    }

    public class Get internal constructor(
      private val __client: HttpClient,
      private val _id: String,
    ) {
      public suspend operator fun invoke(fields: String? = "${'$'}type,columnSettings(${'$'}type,columns(${'$'}type,id),field(${'$'}type,fieldType(${'$'}type,id),id,localizedName,name),id),id,name,owner(${'$'}type,id,login,ringId),projects(${'$'}type,id,name,shortName),status(${'$'}type,id,valid),swimlaneSettings(${'$'}type,enabled,field(${'$'}type,customField(${'$'}type,fieldType(${'$'}type,id),id,localizedName,name),id,name),id,values(${'$'}type,id,name))"): AgileRead {
        val response = __client.get("agiles/${this._id.encodeURLParameter()}") {
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
      public suspend operator fun invoke(fields: String? = "${'$'}type,columnSettings(${'$'}type,columns(${'$'}type,id),field(${'$'}type,fieldType(${'$'}type,id),id,localizedName,name),id),id,name,owner(${'$'}type,id,login,ringId),projects(${'$'}type,id,name,shortName),status(${'$'}type,id,valid),swimlaneSettings(${'$'}type,enabled,field(${'$'}type,customField(${'$'}type,fieldType(${'$'}type,id),id,localizedName,name),id,name),id,values(${'$'}type,id,name))", body: AgileWrite? = null): AgileRead {
        val response = __client.post("agiles/${this._id.encodeURLParameter()}") {
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

    public class Sprints internal constructor(
      private val __client: HttpClient,
      private val _id: String,
    ) {
      public val `get`: Get = Get(__client, _id)

      public val post: Post = Post(__client, _id)

      public fun sprintId(sprintId: String): SprintIdPath = SprintIdPath(__client, _id, sprintId)

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _id: String,
      ) {
        public suspend operator fun invoke(
          fields: String? = "${'$'}type,archived,finish,id,isDefault,name,start",
          skip: Int? = null,
          top: Int? = null,
        ): List<SprintRead> {
          val response = __client.get("agiles/${this._id.encodeURLParameter()}/sprints") {
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
        private val _id: String,
      ) {
        public suspend operator fun invoke(
          muteUpdateNotifications: Boolean? = null,
          fields: String? = "${'$'}type,archived,finish,id,isDefault,name,start",
          body: SprintWrite? = null,
        ): SprintRead {
          val response = __client.post("agiles/${this._id.encodeURLParameter()}/sprints") {
            if (muteUpdateNotifications != null) {
              this.url.encodedParameters.append("muteUpdateNotifications", muteUpdateNotifications.toString())
            }
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

      public class SprintIdPath internal constructor(
        private val __client: HttpClient,
        private val _id: String,
        private val _sprintId: String,
      ) {
        public val delete: Delete = Delete(__client, _id, _sprintId)

        public val `get`: Get = Get(__client, _id, _sprintId)

        public val post: Post = Post(__client, _id, _sprintId)

        public class Delete internal constructor(
          private val __client: HttpClient,
          private val _id: String,
          private val _sprintId: String,
        ) {
          public suspend operator fun invoke() {
            val response = __client.delete("agiles/${this._id.encodeURLParameter()}/sprints/${this._sprintId.encodeURLParameter()}")
            if (response.status.value != 200) throw ResponseException(response, "")
          }
        }

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _id: String,
          private val _sprintId: String,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,archived,finish,id,isDefault,name,start"): SprintRead {
            val response = __client.get("agiles/${this._id.encodeURLParameter()}/sprints/${this._sprintId.encodeURLParameter()}") {
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
          private val _sprintId: String,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,archived,finish,id,isDefault,name,start", body: SprintWrite? = null): SprintRead {
            val response = __client.post("agiles/${this._id.encodeURLParameter()}/sprints/${this._sprintId.encodeURLParameter()}") {
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
      }
    }
  }
}
