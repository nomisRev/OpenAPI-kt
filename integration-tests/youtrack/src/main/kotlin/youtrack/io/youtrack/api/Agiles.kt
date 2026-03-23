package io.youtrack.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.`get`
import io.ktor.client.request.delete
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.youtrack.model.AgileRead
import io.youtrack.model.AgileWrite
import io.youtrack.model.SprintRead
import io.youtrack.model.SprintWrite
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.collections.List

public class Agiles internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public val post: Post = Post(client)

  public fun id(id: String): IdPath = IdPath(client, id)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(
      fields: String? = "${'$'}type,columnSettings(${'$'}type,columns(${'$'}type,id),field(${'$'}type,fieldType(${'$'}type,id),id,localizedName,name),id),id,name,owner(${'$'}type,id,login,ringId),projects(${'$'}type,id,name,shortName),status(${'$'}type,id,valid),swimlaneSettings(${'$'}type,enabled,field(${'$'}type,customField(${'$'}type,fieldType(${'$'}type,id),id,localizedName,name),id,name),id,values(${'$'}type,id,name))",
      skip: Int? = null,
      top: Int? = null,
    ): List<AgileRead> = client.get("/agiles") {
      fields?.let { parameter("fields", it) }
      skip?.let { parameter("${'$'}skip", it) }
      top?.let { parameter("${'$'}top", it) }
    }.body()
  }

  public class Post internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(
      template: String? = null,
      fields: String? = "${'$'}type,columnSettings(${'$'}type,columns(${'$'}type,id),field(${'$'}type,fieldType(${'$'}type,id),id,localizedName,name),id),id,name,owner(${'$'}type,id,login,ringId),projects(${'$'}type,id,name,shortName),status(${'$'}type,id,valid),swimlaneSettings(${'$'}type,enabled,field(${'$'}type,customField(${'$'}type,fieldType(${'$'}type,id),id,localizedName,name),id,name),id,values(${'$'}type,id,name))",
      body: AgileWrite? = null,
    ): AgileRead = client.post("/agiles") {
      template?.let { parameter("template", it) }
      fields?.let { parameter("fields", it) }
      body?.let {
        contentType(ContentType.Application.Json)
        setBody(it)
      }
    }.body()
  }

  public class IdPath internal constructor(
    private val client: HttpClient,
    private val id: String,
  ) {
    public val delete: Delete = Delete(client, id)

    public val `get`: Get = Get(client, id)

    public val post: Post = Post(client, id)

    public val sprints: Sprints = Sprints(client, id)

    public class Delete internal constructor(
      private val client: HttpClient,
      private val id: String,
    ) {
      public suspend operator fun invoke() {
        client.delete("/agiles/$id")
      }
    }

    public class Get internal constructor(
      private val client: HttpClient,
      private val id: String,
    ) {
      public suspend operator fun invoke(fields: String? = "${'$'}type,columnSettings(${'$'}type,columns(${'$'}type,id),field(${'$'}type,fieldType(${'$'}type,id),id,localizedName,name),id),id,name,owner(${'$'}type,id,login,ringId),projects(${'$'}type,id,name,shortName),status(${'$'}type,id,valid),swimlaneSettings(${'$'}type,enabled,field(${'$'}type,customField(${'$'}type,fieldType(${'$'}type,id),id,localizedName,name),id,name),id,values(${'$'}type,id,name))"): AgileRead = client.get("/agiles/$id") {
        fields?.let { parameter("fields", it) }
      }.body()
    }

    public class Post internal constructor(
      private val client: HttpClient,
      private val id: String,
    ) {
      public suspend operator fun invoke(fields: String? = "${'$'}type,columnSettings(${'$'}type,columns(${'$'}type,id),field(${'$'}type,fieldType(${'$'}type,id),id,localizedName,name),id),id,name,owner(${'$'}type,id,login,ringId),projects(${'$'}type,id,name,shortName),status(${'$'}type,id,valid),swimlaneSettings(${'$'}type,enabled,field(${'$'}type,customField(${'$'}type,fieldType(${'$'}type,id),id,localizedName,name),id,name),id,values(${'$'}type,id,name))", body: AgileWrite? = null): AgileRead = client.post("/agiles/$id") {
        fields?.let { parameter("fields", it) }
        body?.let {
          contentType(ContentType.Application.Json)
          setBody(it)
        }
      }.body()
    }

    public class Sprints internal constructor(
      private val client: HttpClient,
      private val id: String,
    ) {
      public val `get`: Get = Get(client, id)

      public val post: Post = Post(client, id)

      public fun sprintId(sprintId: String): SprintIdPath = SprintIdPath(client, id, sprintId)

      public class Get internal constructor(
        private val client: HttpClient,
        private val id: String,
      ) {
        public suspend operator fun invoke(
          fields: String? = "${'$'}type,archived,finish,id,isDefault,name,start",
          skip: Int? = null,
          top: Int? = null,
        ): List<SprintRead> = client.get("/agiles/$id/sprints") {
          fields?.let { parameter("fields", it) }
          skip?.let { parameter("${'$'}skip", it) }
          top?.let { parameter("${'$'}top", it) }
        }.body()
      }

      public class Post internal constructor(
        private val client: HttpClient,
        private val id: String,
      ) {
        public suspend operator fun invoke(
          muteUpdateNotifications: Boolean? = null,
          fields: String? = "${'$'}type,archived,finish,id,isDefault,name,start",
          body: SprintWrite? = null,
        ): SprintRead = client.post("/agiles/$id/sprints") {
          muteUpdateNotifications?.let { parameter("muteUpdateNotifications", it) }
          fields?.let { parameter("fields", it) }
          body?.let {
            contentType(ContentType.Application.Json)
            setBody(it)
          }
        }.body()
      }

      public class SprintIdPath internal constructor(
        private val client: HttpClient,
        private val id: String,
        private val sprintId: String,
      ) {
        public val delete: Delete = Delete(client, id, sprintId)

        public val `get`: Get = Get(client, id, sprintId)

        public val post: Post = Post(client, id, sprintId)

        public class Delete internal constructor(
          private val client: HttpClient,
          private val id: String,
          private val sprintId: String,
        ) {
          public suspend operator fun invoke() {
            client.delete("/agiles/$id/sprints/$sprintId")
          }
        }

        public class Get internal constructor(
          private val client: HttpClient,
          private val id: String,
          private val sprintId: String,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,archived,finish,id,isDefault,name,start"): SprintRead = client.get("/agiles/$id/sprints/$sprintId") {
            fields?.let { parameter("fields", it) }
          }.body()
        }

        public class Post internal constructor(
          private val client: HttpClient,
          private val id: String,
          private val sprintId: String,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,archived,finish,id,isDefault,name,start", body: SprintWrite? = null): SprintRead = client.post("/agiles/$id/sprints/$sprintId") {
            fields?.let { parameter("fields", it) }
            body?.let {
              contentType(ContentType.Application.Json)
              setBody(it)
            }
          }.body()
        }
      }
    }
  }
}
