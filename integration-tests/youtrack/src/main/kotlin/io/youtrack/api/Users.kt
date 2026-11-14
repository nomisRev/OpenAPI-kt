package io.youtrack.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.encodeURLParameter
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
    ): List<UserRead> {
      val response = client.get("users") {
        if (fields != null) {
          url.encodedParameters.append("fields", fields.encodeURLParameter())
        }
        if (skip != null) {
          url.encodedParameters.append("%24skip", skip.toString())
        }
        if (top != null) {
          url.encodedParameters.append("%24top", top.toString())
        }
      }
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body()
    }
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
      public suspend operator fun invoke(fields: String? = "${'$'}type,banned,email,fullName,guest,id,login,ringId"): UserRead {
        val response = client.get("users/${id.encodeURLParameter()}") {
          if (fields != null) {
            url.encodedParameters.append("fields", fields.encodeURLParameter())
          }
        }
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
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
          public suspend operator fun invoke(fields: String? = "${'$'}type,dateFieldFormat(${'$'}type,datePattern,id,pattern,presentation),id,locale(${'$'}type,community,id,language,locale,name),timezone(${'$'}type,id,offset,presentation)"): GeneralUserProfileRead {
            val response = client.get("users/${id.encodeURLParameter()}/profiles/general") {
              if (fields != null) {
                url.encodedParameters.append("fields", fields.encodeURLParameter())
              }
            }
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class Post internal constructor(
          private val client: HttpClient,
          private val id: String,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,dateFieldFormat(${'$'}type,datePattern,id,pattern,presentation),id,locale(${'$'}type,community,id,language,locale,name),timezone(${'$'}type,id,offset,presentation)", body: GeneralUserProfileWrite? = null): GeneralUserProfileRead {
            val response = client.post("users/${id.encodeURLParameter()}/profiles/general") {
              if (fields != null) {
                url.encodedParameters.append("fields", fields.encodeURLParameter())
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
          public suspend operator fun invoke(fields: String? = "${'$'}type,id"): NotificationsUserProfileRead {
            val response = client.get("users/${id.encodeURLParameter()}/profiles/notifications") {
              if (fields != null) {
                url.encodedParameters.append("fields", fields.encodeURLParameter())
              }
            }
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class Post internal constructor(
          private val client: HttpClient,
          private val id: String,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,id", body: NotificationsUserProfileWrite? = null): NotificationsUserProfileRead {
            val response = client.post("users/${id.encodeURLParameter()}/profiles/notifications") {
              if (fields != null) {
                url.encodedParameters.append("fields", fields.encodeURLParameter())
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
          public suspend operator fun invoke(fields: String? = "${'$'}type,id,periodFormat(${'$'}type,id)"): TimeTrackingUserProfileRead {
            val response = client.get("users/${id.encodeURLParameter()}/profiles/timetracking") {
              if (fields != null) {
                url.encodedParameters.append("fields", fields.encodeURLParameter())
              }
            }
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class Post internal constructor(
          private val client: HttpClient,
          private val id: String,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,id,periodFormat(${'$'}type,id)", body: TimeTrackingUserProfileWrite? = null): TimeTrackingUserProfileRead {
            val response = client.post("users/${id.encodeURLParameter()}/profiles/timetracking") {
              if (fields != null) {
                url.encodedParameters.append("fields", fields.encodeURLParameter())
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
        ): List<IssueFolderRead.SavedQuery> {
          val response = client.get("users/${id.encodeURLParameter()}/savedQueries") {
            if (fields != null) {
              url.encodedParameters.append("fields", fields.encodeURLParameter())
            }
            if (skip != null) {
              url.encodedParameters.append("%24skip", skip.toString())
            }
            if (top != null) {
              url.encodedParameters.append("%24top", top.toString())
            }
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
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
        ): List<IssueFolderRead.Tag> {
          val response = client.get("users/${id.encodeURLParameter()}/tags") {
            if (fields != null) {
              url.encodedParameters.append("fields", fields.encodeURLParameter())
            }
            if (skip != null) {
              url.encodedParameters.append("%24skip", skip.toString())
            }
            if (top != null) {
              url.encodedParameters.append("%24top", top.toString())
            }
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
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
      public suspend operator fun invoke(fields: String? = "${'$'}type,banned,email,fullName,guest,id,login,ringId"): UserRead.Me {
        val response = client.get("users/me") {
          if (fields != null) {
            url.encodedParameters.append("fields", fields.encodeURLParameter())
          }
        }
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }
  }
}
