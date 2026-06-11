package io.youtrack.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.`get`
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.youtrack.model.GeneralUserProfileRead
import io.youtrack.model.GeneralUserProfileWrite
import io.youtrack.model.IssueFolderRead
import io.youtrack.model.NotificationsUserProfileRead
import io.youtrack.model.NotificationsUserProfileWrite
import io.youtrack.model.TimeTrackingUserProfileRead
import io.youtrack.model.TimeTrackingUserProfileWrite
import io.youtrack.model.UserRead
import kotlin.Int
import kotlin.String
import kotlin.collections.List

public class Users internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public val me: Me = Me(client)

  public fun id(id: String): IdPath = IdPath(client, id)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(
      fields: String? = "${'$'}type,banned,email,fullName,guest,id,login,ringId",
      skip: Int? = null,
      top: Int? = null,
    ): List<UserRead> = client.get("/users") {
      fields?.let { parameter("fields", it) }
      skip?.let { parameter("${'$'}skip", it) }
      top?.let { parameter("${'$'}top", it) }
    }.body()
  }

  public class IdPath internal constructor(
    private val client: HttpClient,
    private val id: String,
  ) {
    public val `get`: Get = Get(client, id)

    public val profiles: Profiles = Profiles(client, id)

    public val savedQueries: SavedQueries = SavedQueries(client, id)

    public val tags: Tags = Tags(client, id)

    public class Get internal constructor(
      private val client: HttpClient,
      private val id: String,
    ) {
      public suspend operator fun invoke(fields: String? = "${'$'}type,banned,email,fullName,guest,id,login,ringId"): UserRead = client.get("/users/$id") {
        fields?.let { parameter("fields", it) }
      }.body()
    }

    public class Profiles internal constructor(
      private val client: HttpClient,
      private val id: String,
    ) {
      public val general: General = General(client, id)

      public val notifications: Notifications = Notifications(client, id)

      public val timetracking: Timetracking = Timetracking(client, id)

      public class General internal constructor(
        private val client: HttpClient,
        private val id: String,
      ) {
        public val `get`: Get = Get(client, id)

        public val post: Post = Post(client, id)

        public class Get internal constructor(
          private val client: HttpClient,
          private val id: String,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,dateFieldFormat(${'$'}type,datePattern,id,pattern,presentation),id,locale(${'$'}type,community,id,language,locale,name),timezone(${'$'}type,id,offset,presentation)"): GeneralUserProfileRead = client.get("/users/$id/profiles/general") {
            fields?.let { parameter("fields", it) }
          }.body()
        }

        public class Post internal constructor(
          private val client: HttpClient,
          private val id: String,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,dateFieldFormat(${'$'}type,datePattern,id,pattern,presentation),id,locale(${'$'}type,community,id,language,locale,name),timezone(${'$'}type,id,offset,presentation)", body: GeneralUserProfileWrite? = null): GeneralUserProfileRead = client.post("/users/$id/profiles/general") {
            fields?.let { parameter("fields", it) }
            body?.let {
              contentType(ContentType.Application.Json)
              setBody(it)
            }
          }.body()
        }
      }

      public class Notifications internal constructor(
        private val client: HttpClient,
        private val id: String,
      ) {
        public val `get`: Get = Get(client, id)

        public val post: Post = Post(client, id)

        public class Get internal constructor(
          private val client: HttpClient,
          private val id: String,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,id"): NotificationsUserProfileRead = client.get("/users/$id/profiles/notifications") {
            fields?.let { parameter("fields", it) }
          }.body()
        }

        public class Post internal constructor(
          private val client: HttpClient,
          private val id: String,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,id", body: NotificationsUserProfileWrite? = null): NotificationsUserProfileRead = client.post("/users/$id/profiles/notifications") {
            fields?.let { parameter("fields", it) }
            body?.let {
              contentType(ContentType.Application.Json)
              setBody(it)
            }
          }.body()
        }
      }

      public class Timetracking internal constructor(
        private val client: HttpClient,
        private val id: String,
      ) {
        public val `get`: Get = Get(client, id)

        public val post: Post = Post(client, id)

        public class Get internal constructor(
          private val client: HttpClient,
          private val id: String,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,id,periodFormat(${'$'}type,id)"): TimeTrackingUserProfileRead = client.get("/users/$id/profiles/timetracking") {
            fields?.let { parameter("fields", it) }
          }.body()
        }

        public class Post internal constructor(
          private val client: HttpClient,
          private val id: String,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,id,periodFormat(${'$'}type,id)", body: TimeTrackingUserProfileWrite? = null): TimeTrackingUserProfileRead = client.post("/users/$id/profiles/timetracking") {
            fields?.let { parameter("fields", it) }
            body?.let {
              contentType(ContentType.Application.Json)
              setBody(it)
            }
          }.body()
        }
      }
    }

    public class SavedQueries internal constructor(
      private val client: HttpClient,
      private val id: String,
    ) {
      public val `get`: Get = Get(client, id)

      public class Get internal constructor(
        private val client: HttpClient,
        private val id: String,
      ) {
        public suspend operator fun invoke(
          fields: String? = "${'$'}type,id,name,owner(${'$'}type,id,login,ringId),query",
          skip: Int? = null,
          top: Int? = null,
        ): List<IssueFolderRead.SavedQuery> = client.get("/users/$id/savedQueries") {
          fields?.let { parameter("fields", it) }
          skip?.let { parameter("${'$'}skip", it) }
          top?.let { parameter("${'$'}top", it) }
        }.body()
      }
    }

    public class Tags internal constructor(
      private val client: HttpClient,
      private val id: String,
    ) {
      public val `get`: Get = Get(client, id)

      public class Get internal constructor(
        private val client: HttpClient,
        private val id: String,
      ) {
        public suspend operator fun invoke(
          fields: String? = "${'$'}type,id,name,owner(${'$'}type,id,login,ringId)",
          skip: Int? = null,
          top: Int? = null,
        ): List<IssueFolderRead.Tag> = client.get("/users/$id/tags") {
          fields?.let { parameter("fields", it) }
          skip?.let { parameter("${'$'}skip", it) }
          top?.let { parameter("${'$'}top", it) }
        }.body()
      }
    }
  }

  public class Me internal constructor(
    private val client: HttpClient,
  ) {
    public val `get`: Get = Get(client)

    public class Get internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(fields: String? = "${'$'}type,banned,email,fullName,guest,id,login,ringId"): UserRead.Me = client.get("/users/me") {
        fields?.let { parameter("fields", it) }
      }.body()
    }
  }
}
