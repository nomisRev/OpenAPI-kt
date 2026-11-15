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
  private val __client: HttpClient,
) {
  public val `get`: Get = Get(__client)

  public val me: Me = Me(__client)

  public fun id(id: String): IdPath = IdPath(__client, id)

  public class Get internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke(
      fields: String? = "${'$'}type,banned,email,fullName,guest,id,login,ringId",
      skip: Int? = null,
      top: Int? = null,
    ): List<UserRead> {
      val response = __client.get("users") {
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

  public class IdPath internal constructor(
    private val __client: HttpClient,
    private val _id: String,
  ) {
    public val `get`: Get = Get(__client, _id)

    public val profiles: Profiles = Profiles(__client, _id)

    public val savedQueries: SavedQueries = SavedQueries(__client, _id)

    public val tags: Tags = Tags(__client, _id)

    public class Get internal constructor(
      private val __client: HttpClient,
      private val _id: String,
    ) {
      public suspend operator fun invoke(fields: String? = "${'$'}type,banned,email,fullName,guest,id,login,ringId"): UserRead {
        val response = __client.get("users/${this._id.encodeURLParameter()}") {
          if (fields != null) {
            this.url.encodedParameters.append("fields", fields.encodeURLParameter())
          }
        }
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }

    public class Profiles internal constructor(
      private val __client: HttpClient,
      private val _id: String,
    ) {
      public val general: General = General(__client, _id)

      public val notifications: Notifications = Notifications(__client, _id)

      public val timetracking: Timetracking = Timetracking(__client, _id)

      public class General internal constructor(
        private val __client: HttpClient,
        private val _id: String,
      ) {
        public val `get`: Get = Get(__client, _id)

        public val post: Post = Post(__client, _id)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _id: String,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,dateFieldFormat(${'$'}type,datePattern,id,pattern,presentation),id,locale(${'$'}type,community,id,language,locale,name),timezone(${'$'}type,id,offset,presentation)"): GeneralUserProfileRead {
            val response = __client.get("users/${this._id.encodeURLParameter()}/profiles/general") {
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
          public suspend operator fun invoke(fields: String? = "${'$'}type,dateFieldFormat(${'$'}type,datePattern,id,pattern,presentation),id,locale(${'$'}type,community,id,language,locale,name),timezone(${'$'}type,id,offset,presentation)", body: GeneralUserProfileWrite? = null): GeneralUserProfileRead {
            val response = __client.post("users/${this._id.encodeURLParameter()}/profiles/general") {
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

      public class Notifications internal constructor(
        private val __client: HttpClient,
        private val _id: String,
      ) {
        public val `get`: Get = Get(__client, _id)

        public val post: Post = Post(__client, _id)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _id: String,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,id"): NotificationsUserProfileRead {
            val response = __client.get("users/${this._id.encodeURLParameter()}/profiles/notifications") {
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
          public suspend operator fun invoke(fields: String? = "${'$'}type,id", body: NotificationsUserProfileWrite? = null): NotificationsUserProfileRead {
            val response = __client.post("users/${this._id.encodeURLParameter()}/profiles/notifications") {
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

      public class Timetracking internal constructor(
        private val __client: HttpClient,
        private val _id: String,
      ) {
        public val `get`: Get = Get(__client, _id)

        public val post: Post = Post(__client, _id)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _id: String,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,id,periodFormat(${'$'}type,id)"): TimeTrackingUserProfileRead {
            val response = __client.get("users/${this._id.encodeURLParameter()}/profiles/timetracking") {
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
          public suspend operator fun invoke(fields: String? = "${'$'}type,id,periodFormat(${'$'}type,id)", body: TimeTrackingUserProfileWrite? = null): TimeTrackingUserProfileRead {
            val response = __client.post("users/${this._id.encodeURLParameter()}/profiles/timetracking") {
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

    public class SavedQueries internal constructor(
      private val __client: HttpClient,
      private val _id: String,
    ) {
      public val `get`: Get = Get(__client, _id)

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _id: String,
      ) {
        public suspend operator fun invoke(
          fields: String? = "${'$'}type,id,name,owner(${'$'}type,id,login,ringId),query",
          skip: Int? = null,
          top: Int? = null,
        ): List<IssueFolderRead.SavedQuery> {
          val response = __client.get("users/${this._id.encodeURLParameter()}/savedQueries") {
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

    public class Tags internal constructor(
      private val __client: HttpClient,
      private val _id: String,
    ) {
      public val `get`: Get = Get(__client, _id)

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _id: String,
      ) {
        public suspend operator fun invoke(
          fields: String? = "${'$'}type,id,name,owner(${'$'}type,id,login,ringId)",
          skip: Int? = null,
          top: Int? = null,
        ): List<IssueFolderRead.Tag> {
          val response = __client.get("users/${this._id.encodeURLParameter()}/tags") {
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

  public class Me internal constructor(
    private val __client: HttpClient,
  ) {
    public val `get`: Get = Get(__client)

    public class Get internal constructor(
      private val __client: HttpClient,
    ) {
      public suspend operator fun invoke(fields: String? = "${'$'}type,banned,email,fullName,guest,id,login,ringId"): UserRead.Me {
        val response = __client.get("users/me") {
          if (fields != null) {
            this.url.encodedParameters.append("fields", fields.encodeURLParameter())
          }
        }
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }
  }
}
