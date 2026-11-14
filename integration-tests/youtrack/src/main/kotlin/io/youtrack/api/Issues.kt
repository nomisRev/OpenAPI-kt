package io.youtrack.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.delete
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.encodeURLParameter
import io.youtrack.model.ActivityCursorPage
import io.youtrack.model.ActivityItem
import io.youtrack.model.BaseWorkItemRead
import io.youtrack.model.BaseWorkItemWrite
import io.youtrack.model.IssueAttachmentRead
import io.youtrack.model.IssueAttachmentWrite
import io.youtrack.model.IssueCommentRead
import io.youtrack.model.IssueCommentWrite
import io.youtrack.model.IssueCustomFieldRead
import io.youtrack.model.IssueCustomFieldWrite
import io.youtrack.model.IssueFolderRead
import io.youtrack.model.IssueFolderWrite
import io.youtrack.model.IssueLink
import io.youtrack.model.IssueRead
import io.youtrack.model.IssueTimeTracker
import io.youtrack.model.IssueWrite
import io.youtrack.model.ReactionRead
import io.youtrack.model.ReactionWrite
import io.youtrack.model.SprintRead
import io.youtrack.model.VcsChangeRead
import io.youtrack.model.VcsChangeWrite
import kotlin.Boolean
import kotlin.ByteArray
import kotlin.Int
import kotlin.String
import kotlin.collections.List

public class Issues internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public val post: Post = Post(client)

  public fun id(id: String): IdPath = IdPath(client, id)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(
      query: String? = null,
      customFields: String? = null,
      fields: String? = "${'$'}type,created,customFields(${'$'}type,id,name,value(${'$'}type,id,name)),description,id,idReadable,links(${'$'}type,direction,id,linkType(${'$'}type,id,localizedName,name)),numberInProject,project(${'$'}type,id,name,shortName),reporter(${'$'}type,id,login,ringId),resolved,summary,updated,updater(${'$'}type,id,login,ringId),visibility(${'$'}type,id,permittedGroups(${'$'}type,id,name,ringId),permittedUsers(${'$'}type,id,login,ringId))",
      skip: Int? = null,
      top: Int? = null,
    ): List<IssueRead> {
      val response = client.get("issues") {
        if (query != null) {
          this.url.encodedParameters.append("query", query.encodeURLParameter())
        }
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

  public class Post internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(
      draftId: String? = null,
      muteUpdateNotifications: Boolean? = null,
      fields: String? = "${'$'}type,created,customFields(${'$'}type,id,name,value(${'$'}type,id,name)),description,id,idReadable,links(${'$'}type,direction,id,linkType(${'$'}type,id,localizedName,name)),numberInProject,project(${'$'}type,id,name,shortName),reporter(${'$'}type,id,login,ringId),resolved,summary,updated,updater(${'$'}type,id,login,ringId),visibility(${'$'}type,id,permittedGroups(${'$'}type,id,name,ringId),permittedUsers(${'$'}type,id,login,ringId))",
      body: IssueWrite? = null,
    ): IssueRead {
      val response = client.post("issues") {
        if (draftId != null) {
          this.url.encodedParameters.append("draftId", draftId.encodeURLParameter())
        }
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

  public class IdPath internal constructor(
    private val client: HttpClient,
    private val id: String,
  ) {
    public val delete: Delete = Delete(client, id)

    public val `get`: Get = Get(client, id)

    public val post: Post = Post(client, id)

    public val activities: Activities = Activities(client, id)

    public val activitiesPage: ActivitiesPage = ActivitiesPage(client, id)

    public val attachments: Attachments = Attachments(client, id)

    public val comments: Comments = Comments(client, id)

    public val customFields: CustomFields = CustomFields(client, id)

    public val links: Links = Links(client, id)

    public val project: Project = Project(client, id)

    public val sprints: Sprints = Sprints(client, id)

    public val tags: Tags = Tags(client, id)

    public val timeTracking: TimeTracking = TimeTracking(client, id)

    public val vcsChanges: VcsChanges = VcsChanges(client, id)

    public class Delete internal constructor(
      private val client: HttpClient,
      private val id: String,
    ) {
      public suspend operator fun invoke() {
        val response = client.delete("issues/${this.id.encodeURLParameter()}")
        if (response.status.value != 200) throw ResponseException(response, "")
      }
    }

    public class Get internal constructor(
      private val client: HttpClient,
      private val id: String,
    ) {
      public suspend operator fun invoke(fields: String? = "${'$'}type,created,customFields(${'$'}type,id,name,value(${'$'}type,id,name)),description,id,idReadable,links(${'$'}type,direction,id,linkType(${'$'}type,id,localizedName,name)),numberInProject,project(${'$'}type,id,name,shortName),reporter(${'$'}type,id,login,ringId),resolved,summary,updated,updater(${'$'}type,id,login,ringId),visibility(${'$'}type,id,permittedGroups(${'$'}type,id,name,ringId),permittedUsers(${'$'}type,id,login,ringId))"): IssueRead {
        val response = client.get("issues/${this.id.encodeURLParameter()}") {
          if (fields != null) {
            this.url.encodedParameters.append("fields", fields.encodeURLParameter())
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
      public suspend operator fun invoke(
        muteUpdateNotifications: Boolean? = null,
        fields: String? = "${'$'}type,created,customFields(${'$'}type,id,name,value(${'$'}type,id,name)),description,id,idReadable,links(${'$'}type,direction,id,linkType(${'$'}type,id,localizedName,name)),numberInProject,project(${'$'}type,id,name,shortName),reporter(${'$'}type,id,login,ringId),resolved,summary,updated,updater(${'$'}type,id,login,ringId),visibility(${'$'}type,id,permittedGroups(${'$'}type,id,name,ringId),permittedUsers(${'$'}type,id,login,ringId))",
        body: IssueWrite? = null,
      ): IssueRead {
        val response = client.post("issues/${this.id.encodeURLParameter()}") {
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

    public class Activities internal constructor(
      private val client: HttpClient,
      private val id: String,
    ) {
      public val `get`: Get = Get(client, id)

      public fun activityItemId(activityItemId: String): ActivityItemIdPath = ActivityItemIdPath(client, id, activityItemId)

      public class Get internal constructor(
        private val client: HttpClient,
        private val id: String,
      ) {
        public suspend operator fun invoke(
          categories: String? = null,
          reverse: Boolean? = null,
          start: String? = null,
          end: String? = null,
          author: String? = null,
          fields: String? = "${'$'}type,added,author(${'$'}type,id,login,ringId),category(${'$'}type,id),field(${'$'}type,customField(${'$'}type,fieldType(${'$'}type,id),id,localizedName,name),id,name),id,removed,target,targetMember,timestamp",
          skip: Int? = null,
          top: Int? = null,
        ): List<ActivityItem> {
          val response = client.get("issues/${this.id.encodeURLParameter()}/activities") {
            if (categories != null) {
              this.url.encodedParameters.append("categories", categories.encodeURLParameter())
            }
            if (reverse != null) {
              this.url.encodedParameters.append("reverse", reverse.toString())
            }
            if (start != null) {
              this.url.encodedParameters.append("start", start.encodeURLParameter())
            }
            if (end != null) {
              this.url.encodedParameters.append("end", end.encodeURLParameter())
            }
            if (author != null) {
              this.url.encodedParameters.append("author", author.encodeURLParameter())
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

      public class ActivityItemIdPath internal constructor(
        private val client: HttpClient,
        private val id: String,
        private val activityItemId: String,
      ) {
        public val `get`: Get = Get(client, id, activityItemId)

        public class Get internal constructor(
          private val client: HttpClient,
          private val id: String,
          private val activityItemId: String,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,added,author(${'$'}type,id,login,ringId),category(${'$'}type,id),field(${'$'}type,customField(${'$'}type,fieldType(${'$'}type,id),id,localizedName,name),id,name),id,removed,target,targetMember,timestamp"): ActivityItem {
            val response = client.get("issues/${this.id.encodeURLParameter()}/activities/${this.activityItemId.encodeURLParameter()}") {
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

    public class ActivitiesPage internal constructor(
      private val client: HttpClient,
      private val id: String,
    ) {
      public val `get`: Get = Get(client, id)

      public class Get internal constructor(
        private val client: HttpClient,
        private val id: String,
      ) {
        public suspend operator fun invoke(
          categories: String? = null,
          reverse: Boolean? = null,
          start: String? = null,
          end: String? = null,
          author: String? = null,
          cursor: String? = null,
          activityId: String? = null,
          fields: String? = "${'$'}type,activities(${'$'}type,added,author(${'$'}type,id,login,ringId),category(${'$'}type,id),field(${'$'}type,customField(${'$'}type,fieldType(${'$'}type,id),id,localizedName,name),id,name),id,removed,target,targetMember,timestamp),afterCursor,beforeCursor,hasAfter,hasBefore,id",
        ): ActivityCursorPage {
          val response = client.get("issues/${this.id.encodeURLParameter()}/activitiesPage") {
            if (categories != null) {
              this.url.encodedParameters.append("categories", categories.encodeURLParameter())
            }
            if (reverse != null) {
              this.url.encodedParameters.append("reverse", reverse.toString())
            }
            if (start != null) {
              this.url.encodedParameters.append("start", start.encodeURLParameter())
            }
            if (end != null) {
              this.url.encodedParameters.append("end", end.encodeURLParameter())
            }
            if (author != null) {
              this.url.encodedParameters.append("author", author.encodeURLParameter())
            }
            if (cursor != null) {
              this.url.encodedParameters.append("cursor", cursor.encodeURLParameter())
            }
            if (activityId != null) {
              this.url.encodedParameters.append("activityId", activityId.encodeURLParameter())
            }
            if (fields != null) {
              this.url.encodedParameters.append("fields", fields.encodeURLParameter())
            }
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
      }
    }

    public class Attachments internal constructor(
      private val client: HttpClient,
      private val id: String,
    ) {
      public val `get`: Get = Get(client, id)

      public val post: Post = Post(client, id)

      public fun issueAttachmentId(issueAttachmentId: String): IssueAttachmentIdPath = IssueAttachmentIdPath(client, id, issueAttachmentId)

      public class Get internal constructor(
        private val client: HttpClient,
        private val id: String,
      ) {
        public suspend operator fun invoke(
          fields: String? = "${'$'}type,author(${'$'}type,id,login,ringId),charset,created,extension,id,metaData,mimeType,name,size,updated,url",
          skip: Int? = null,
          top: Int? = null,
        ): List<IssueAttachmentRead> {
          val response = client.get("issues/${this.id.encodeURLParameter()}/attachments") {
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
        private val client: HttpClient,
        private val id: String,
      ) {
        public suspend operator fun invoke(muteUpdateNotifications: Boolean? = null, fields: String? = "${'$'}type,author(${'$'}type,id,login,ringId),charset,created,extension,id,metaData,mimeType,name,size,updated,url"): List<IssueAttachmentRead> {
          val response = client.post("issues/${this.id.encodeURLParameter()}/attachments") {
            if (muteUpdateNotifications != null) {
              this.url.encodedParameters.append("muteUpdateNotifications", muteUpdateNotifications.toString())
            }
            if (fields != null) {
              this.url.encodedParameters.append("fields", fields.encodeURLParameter())
            }
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }

        public suspend operator fun invoke(
          muteUpdateNotifications: Boolean? = null,
          fields: String? = "${'$'}type,author(${'$'}type,id,login,ringId),charset,created,extension,id,metaData,mimeType,name,size,updated,url",
          files0: ByteArray? = null,
        ): List<IssueAttachmentRead> {
          val response = client.post("issues/${this.id.encodeURLParameter()}/attachments") {
            if (muteUpdateNotifications != null) {
              this.url.encodedParameters.append("muteUpdateNotifications", muteUpdateNotifications.toString())
            }
            if (fields != null) {
              this.url.encodedParameters.append("fields", fields.encodeURLParameter())
            }
            setBody(MultiPartFormDataContent(formData {
              if (files0 != null) {
                append("files[0]", files0)
              }
            }))
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
      }

      public class IssueAttachmentIdPath internal constructor(
        private val client: HttpClient,
        private val id: String,
        private val issueAttachmentId: String,
      ) {
        public val delete: Delete = Delete(client, id, issueAttachmentId)

        public val `get`: Get = Get(client, id, issueAttachmentId)

        public val post: Post = Post(client, id, issueAttachmentId)

        public class Delete internal constructor(
          private val client: HttpClient,
          private val id: String,
          private val issueAttachmentId: String,
        ) {
          public suspend operator fun invoke() {
            val response = client.delete("issues/${this.id.encodeURLParameter()}/attachments/${this.issueAttachmentId.encodeURLParameter()}")
            if (response.status.value != 200) throw ResponseException(response, "")
          }
        }

        public class Get internal constructor(
          private val client: HttpClient,
          private val id: String,
          private val issueAttachmentId: String,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,author(${'$'}type,id,login,ringId),charset,created,extension,id,metaData,mimeType,name,size,updated,url"): IssueAttachmentRead {
            val response = client.get("issues/${this.id.encodeURLParameter()}/attachments/${this.issueAttachmentId.encodeURLParameter()}") {
              if (fields != null) {
                this.url.encodedParameters.append("fields", fields.encodeURLParameter())
              }
            }
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class Post internal constructor(
          private val client: HttpClient,
          private val id: String,
          private val issueAttachmentId: String,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,author(${'$'}type,id,login,ringId),charset,created,extension,id,metaData,mimeType,name,size,updated,url", body: IssueAttachmentWrite? = null): IssueAttachmentRead {
            val response = client.post("issues/${this.id.encodeURLParameter()}/attachments/${this.issueAttachmentId.encodeURLParameter()}") {
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

    public class Comments internal constructor(
      private val client: HttpClient,
      private val id: String,
    ) {
      public val `get`: Get = Get(client, id)

      public val post: Post = Post(client, id)

      public fun issueCommentId(issueCommentId: String): IssueCommentIdPath = IssueCommentIdPath(client, id, issueCommentId)

      public class Get internal constructor(
        private val client: HttpClient,
        private val id: String,
      ) {
        public suspend operator fun invoke(
          fields: String? = "${'$'}type,attachments(${'$'}type,id),author(${'$'}type,id,login,ringId),created,deleted,id,text,updated,visibility(${'$'}type,id,permittedGroups(${'$'}type,id,name,ringId),permittedUsers(${'$'}type,id,login,ringId))",
          skip: Int? = null,
          top: Int? = null,
        ): List<IssueCommentRead> {
          val response = client.get("issues/${this.id.encodeURLParameter()}/comments") {
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
        private val client: HttpClient,
        private val id: String,
      ) {
        public suspend operator fun invoke(
          draftId: String? = null,
          muteUpdateNotifications: Boolean? = null,
          fields: String? = "${'$'}type,attachments(${'$'}type,id),author(${'$'}type,id,login,ringId),created,deleted,id,text,updated,visibility(${'$'}type,id,permittedGroups(${'$'}type,id,name,ringId),permittedUsers(${'$'}type,id,login,ringId))",
          body: IssueCommentWrite? = null,
        ): IssueCommentRead {
          val response = client.post("issues/${this.id.encodeURLParameter()}/comments") {
            if (draftId != null) {
              this.url.encodedParameters.append("draftId", draftId.encodeURLParameter())
            }
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

      public class IssueCommentIdPath internal constructor(
        private val client: HttpClient,
        private val id: String,
        private val issueCommentId: String,
      ) {
        public val delete: Delete = Delete(client, id, issueCommentId)

        public val `get`: Get = Get(client, id, issueCommentId)

        public val post: Post = Post(client, id, issueCommentId)

        public val reactions: Reactions = Reactions(client, id, issueCommentId)

        public class Delete internal constructor(
          private val client: HttpClient,
          private val id: String,
          private val issueCommentId: String,
        ) {
          public suspend operator fun invoke() {
            val response = client.delete("issues/${this.id.encodeURLParameter()}/comments/${this.issueCommentId.encodeURLParameter()}")
            if (response.status.value != 200) throw ResponseException(response, "")
          }
        }

        public class Get internal constructor(
          private val client: HttpClient,
          private val id: String,
          private val issueCommentId: String,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,attachments(${'$'}type,id),author(${'$'}type,id,login,ringId),created,deleted,id,text,updated,visibility(${'$'}type,id,permittedGroups(${'$'}type,id,name,ringId),permittedUsers(${'$'}type,id,login,ringId))"): IssueCommentRead {
            val response = client.get("issues/${this.id.encodeURLParameter()}/comments/${this.issueCommentId.encodeURLParameter()}") {
              if (fields != null) {
                this.url.encodedParameters.append("fields", fields.encodeURLParameter())
              }
            }
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class Post internal constructor(
          private val client: HttpClient,
          private val id: String,
          private val issueCommentId: String,
        ) {
          public suspend operator fun invoke(
            muteUpdateNotifications: Boolean? = null,
            fields: String? = "${'$'}type,attachments(${'$'}type,id),author(${'$'}type,id,login,ringId),created,deleted,id,text,updated,visibility(${'$'}type,id,permittedGroups(${'$'}type,id,name,ringId),permittedUsers(${'$'}type,id,login,ringId))",
            body: IssueCommentWrite? = null,
          ): IssueCommentRead {
            val response = client.post("issues/${this.id.encodeURLParameter()}/comments/${this.issueCommentId.encodeURLParameter()}") {
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

        public class Reactions internal constructor(
          private val client: HttpClient,
          private val id: String,
          private val issueCommentId: String,
        ) {
          public val `get`: Get = Get(client, id, issueCommentId)

          public val post: Post = Post(client, id, issueCommentId)

          public fun reactionId(reactionId: String): ReactionIdPath = ReactionIdPath(client, id, issueCommentId, reactionId)

          public class Get internal constructor(
            private val client: HttpClient,
            private val id: String,
            private val issueCommentId: String,
          ) {
            public suspend operator fun invoke(
              fields: String? = "${'$'}type,id",
              skip: Int? = null,
              top: Int? = null,
            ): List<ReactionRead> {
              val response = client.get("issues/${this.id.encodeURLParameter()}/comments/${this.issueCommentId.encodeURLParameter()}/reactions") {
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
            private val client: HttpClient,
            private val id: String,
            private val issueCommentId: String,
          ) {
            public suspend operator fun invoke(fields: String? = "${'$'}type,id", body: ReactionWrite? = null): ReactionRead {
              val response = client.post("issues/${this.id.encodeURLParameter()}/comments/${this.issueCommentId.encodeURLParameter()}/reactions") {
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

          public class ReactionIdPath internal constructor(
            private val client: HttpClient,
            private val id: String,
            private val issueCommentId: String,
            private val reactionId: String,
          ) {
            public val delete: Delete = Delete(client, id, issueCommentId, reactionId)

            public val `get`: Get = Get(client, id, issueCommentId, reactionId)

            public class Delete internal constructor(
              private val client: HttpClient,
              private val id: String,
              private val issueCommentId: String,
              private val reactionId: String,
            ) {
              public suspend operator fun invoke() {
                val response = client.delete("issues/${this.id.encodeURLParameter()}/comments/${this.issueCommentId.encodeURLParameter()}/reactions/${this.reactionId.encodeURLParameter()}")
                if (response.status.value != 200) throw ResponseException(response, "")
              }
            }

            public class Get internal constructor(
              private val client: HttpClient,
              private val id: String,
              private val issueCommentId: String,
              private val reactionId: String,
            ) {
              public suspend operator fun invoke(fields: String? = "${'$'}type,id"): ReactionRead {
                val response = client.get("issues/${this.id.encodeURLParameter()}/comments/${this.issueCommentId.encodeURLParameter()}/reactions/${this.reactionId.encodeURLParameter()}") {
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
      }
    }

    public class CustomFields internal constructor(
      private val client: HttpClient,
      private val id: String,
    ) {
      public val `get`: Get = Get(client, id)

      public fun issueCustomFieldId(issueCustomFieldId: String): IssueCustomFieldIdPath = IssueCustomFieldIdPath(client, id, issueCustomFieldId)

      public class Get internal constructor(
        private val client: HttpClient,
        private val id: String,
      ) {
        public suspend operator fun invoke(
          fields: String? = "${'$'}type,id,name,value(${'$'}type,id,name)",
          skip: Int? = null,
          top: Int? = null,
        ): List<IssueCustomFieldRead> {
          val response = client.get("issues/${this.id.encodeURLParameter()}/customFields") {
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

      public class IssueCustomFieldIdPath internal constructor(
        private val client: HttpClient,
        private val id: String,
        private val issueCustomFieldId: String,
      ) {
        public val `get`: Get = Get(client, id, issueCustomFieldId)

        public val post: Post = Post(client, id, issueCustomFieldId)

        public class Get internal constructor(
          private val client: HttpClient,
          private val id: String,
          private val issueCustomFieldId: String,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,id,name,value(${'$'}type,id,name)"): IssueCustomFieldRead {
            val response = client.get("issues/${this.id.encodeURLParameter()}/customFields/${this.issueCustomFieldId.encodeURLParameter()}") {
              if (fields != null) {
                this.url.encodedParameters.append("fields", fields.encodeURLParameter())
              }
            }
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class Post internal constructor(
          private val client: HttpClient,
          private val id: String,
          private val issueCustomFieldId: String,
        ) {
          public suspend operator fun invoke(
            muteUpdateNotifications: Boolean? = null,
            fields: String? = "${'$'}type,id,name,value(${'$'}type,id,name)",
            body: IssueCustomFieldWrite? = null,
          ): IssueCustomFieldRead {
            val response = client.post("issues/${this.id.encodeURLParameter()}/customFields/${this.issueCustomFieldId.encodeURLParameter()}") {
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
      }
    }

    public class Links internal constructor(
      private val client: HttpClient,
      private val id: String,
    ) {
      public val `get`: Get = Get(client, id)

      public fun issueLinkId(issueLinkId: String): IssueLinkIdPath = IssueLinkIdPath(client, id, issueLinkId)

      public class Get internal constructor(
        private val client: HttpClient,
        private val id: String,
      ) {
        public suspend operator fun invoke(
          fields: String? = "${'$'}type,direction,id,linkType(${'$'}type,id,localizedName,name)",
          skip: Int? = null,
          top: Int? = null,
        ): List<IssueLink> {
          val response = client.get("issues/${this.id.encodeURLParameter()}/links") {
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

      public class IssueLinkIdPath internal constructor(
        private val client: HttpClient,
        private val id: String,
        private val issueLinkId: String,
      ) {
        public val `get`: Get = Get(client, id, issueLinkId)

        public val issues: Issues = Issues(client, id, issueLinkId)

        public class Get internal constructor(
          private val client: HttpClient,
          private val id: String,
          private val issueLinkId: String,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,direction,id,linkType(${'$'}type,id,localizedName,name)"): IssueLink {
            val response = client.get("issues/${this.id.encodeURLParameter()}/links/${this.issueLinkId.encodeURLParameter()}") {
              if (fields != null) {
                this.url.encodedParameters.append("fields", fields.encodeURLParameter())
              }
            }
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class Issues internal constructor(
          private val client: HttpClient,
          private val id: String,
          private val issueLinkId: String,
        ) {
          public val `get`: Get = Get(client, id, issueLinkId)

          public val post: Post = Post(client, id, issueLinkId)

          public fun issueId(issueId: String): IssueIdPath = IssueIdPath(client, id, issueLinkId, issueId)

          public class Get internal constructor(
            private val client: HttpClient,
            private val id: String,
            private val issueLinkId: String,
          ) {
            public suspend operator fun invoke(
              fields: String? = "${'$'}type,created,customFields(${'$'}type,id,name,value(${'$'}type,id,name)),description,id,idReadable,links(${'$'}type,direction,id,linkType(${'$'}type,id,localizedName,name)),numberInProject,project(${'$'}type,id,name,shortName),reporter(${'$'}type,id,login,ringId),resolved,summary,updated,updater(${'$'}type,id,login,ringId),visibility(${'$'}type,id,permittedGroups(${'$'}type,id,name,ringId),permittedUsers(${'$'}type,id,login,ringId))",
              skip: Int? = null,
              top: Int? = null,
            ): List<IssueRead> {
              val response = client.get("issues/${this.id.encodeURLParameter()}/links/${this.issueLinkId.encodeURLParameter()}/issues") {
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
            private val client: HttpClient,
            private val id: String,
            private val issueLinkId: String,
          ) {
            public suspend operator fun invoke(
              muteUpdateNotifications: Boolean? = null,
              fields: String? = "${'$'}type,created,customFields(${'$'}type,id,name,value(${'$'}type,id,name)),description,id,idReadable,links(${'$'}type,direction,id,linkType(${'$'}type,id,localizedName,name)),numberInProject,project(${'$'}type,id,name,shortName),reporter(${'$'}type,id,login,ringId),resolved,summary,updated,updater(${'$'}type,id,login,ringId),visibility(${'$'}type,id,permittedGroups(${'$'}type,id,name,ringId),permittedUsers(${'$'}type,id,login,ringId))",
              body: IssueWrite? = null,
            ): IssueRead {
              val response = client.post("issues/${this.id.encodeURLParameter()}/links/${this.issueLinkId.encodeURLParameter()}/issues") {
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

          public class IssueIdPath internal constructor(
            private val client: HttpClient,
            private val id: String,
            private val issueLinkId: String,
            private val issueId: String,
          ) {
            public val delete: Delete = Delete(client, id, issueLinkId, issueId)

            public class Delete internal constructor(
              private val client: HttpClient,
              private val id: String,
              private val issueLinkId: String,
              private val issueId: String,
            ) {
              public suspend operator fun invoke() {
                val response = client.delete("issues/${this.id.encodeURLParameter()}/links/${this.issueLinkId.encodeURLParameter()}/issues/${this.issueId.encodeURLParameter()}")
                if (response.status.value != 200) throw ResponseException(response, "")
              }
            }
          }
        }
      }
    }

    public class Project internal constructor(
      private val client: HttpClient,
      private val id: String,
    ) {
      public val `get`: Get = Get(client, id)

      public val post: Post = Post(client, id)

      public class Get internal constructor(
        private val client: HttpClient,
        private val id: String,
      ) {
        public suspend operator fun invoke(fields: String? = "${'$'}type,archived,customFields,id,leader(${'$'}type,id,login,ringId),name,shortName"): IssueFolderRead.Project {
          val response = client.get("issues/${this.id.encodeURLParameter()}/project") {
            if (fields != null) {
              this.url.encodedParameters.append("fields", fields.encodeURLParameter())
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
        public suspend operator fun invoke(
          muteUpdateNotifications: Boolean? = null,
          fields: String? = "${'$'}type,archived,customFields,id,leader(${'$'}type,id,login,ringId),name,shortName",
          body: IssueFolderWrite.Project? = null,
        ): IssueFolderRead.Project {
          val response = client.post("issues/${this.id.encodeURLParameter()}/project") {
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
    }

    public class Sprints internal constructor(
      private val client: HttpClient,
      private val id: String,
    ) {
      public val `get`: Get = Get(client, id)

      public class Get internal constructor(
        private val client: HttpClient,
        private val id: String,
      ) {
        public suspend operator fun invoke(
          fields: String? = "${'$'}type,archived,finish,id,isDefault,name,start",
          skip: Int? = null,
          top: Int? = null,
        ): List<SprintRead> {
          val response = client.get("issues/${this.id.encodeURLParameter()}/sprints") {
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
      private val client: HttpClient,
      private val id: String,
    ) {
      public val `get`: Get = Get(client, id)

      public val post: Post = Post(client, id)

      public fun tagId(tagId: String): TagIdPath = TagIdPath(client, id, tagId)

      public class Get internal constructor(
        private val client: HttpClient,
        private val id: String,
      ) {
        public suspend operator fun invoke(
          fields: String? = "${'$'}type,id,name,owner(${'$'}type,id,login,ringId)",
          skip: Int? = null,
          top: Int? = null,
        ): List<IssueFolderRead.Tag> {
          val response = client.get("issues/${this.id.encodeURLParameter()}/tags") {
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
        private val client: HttpClient,
        private val id: String,
      ) {
        public suspend operator fun invoke(fields: String? = "${'$'}type,id,name,owner(${'$'}type,id,login,ringId)", body: IssueFolderWrite.Tag? = null): IssueFolderRead.Tag {
          val response = client.post("issues/${this.id.encodeURLParameter()}/tags") {
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

      public class TagIdPath internal constructor(
        private val client: HttpClient,
        private val id: String,
        private val tagId: String,
      ) {
        public val delete: Delete = Delete(client, id, tagId)

        public val `get`: Get = Get(client, id, tagId)

        public class Delete internal constructor(
          private val client: HttpClient,
          private val id: String,
          private val tagId: String,
        ) {
          public suspend operator fun invoke() {
            val response = client.delete("issues/${this.id.encodeURLParameter()}/tags/${this.tagId.encodeURLParameter()}")
            if (response.status.value != 200) throw ResponseException(response, "")
          }
        }

        public class Get internal constructor(
          private val client: HttpClient,
          private val id: String,
          private val tagId: String,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,id,name,owner(${'$'}type,id,login,ringId)"): IssueFolderRead.Tag {
            val response = client.get("issues/${this.id.encodeURLParameter()}/tags/${this.tagId.encodeURLParameter()}") {
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

    public class TimeTracking internal constructor(
      private val client: HttpClient,
      private val id: String,
    ) {
      public val `get`: Get = Get(client, id)

      public val workItems: WorkItems = WorkItems(client, id)

      public class Get internal constructor(
        private val client: HttpClient,
        private val id: String,
      ) {
        public suspend operator fun invoke(fields: String? = "${'$'}type,enabled,id"): IssueTimeTracker {
          val response = client.get("issues/${this.id.encodeURLParameter()}/timeTracking") {
            if (fields != null) {
              this.url.encodedParameters.append("fields", fields.encodeURLParameter())
            }
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
      }

      public class WorkItems internal constructor(
        private val client: HttpClient,
        private val id: String,
      ) {
        public val `get`: Get = Get(client, id)

        public val post: Post = Post(client, id)

        public fun issueWorkItemId(issueWorkItemId: String): IssueWorkItemIdPath = IssueWorkItemIdPath(client, id, issueWorkItemId)

        public class Get internal constructor(
          private val client: HttpClient,
          private val id: String,
        ) {
          public suspend operator fun invoke(
            fields: String? = "${'$'}type,author(${'$'}type,id,login,ringId),created,creator(${'$'}type,id,login,ringId),date,duration(${'$'}type,id),id,text,updated",
            skip: Int? = null,
            top: Int? = null,
          ): List<BaseWorkItemRead.IssueWorkItem> {
            val response = client.get("issues/${this.id.encodeURLParameter()}/timeTracking/workItems") {
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
          private val client: HttpClient,
          private val id: String,
        ) {
          public suspend operator fun invoke(
            muteUpdateNotifications: Boolean? = null,
            fields: String? = "${'$'}type,author(${'$'}type,id,login,ringId),created,creator(${'$'}type,id,login,ringId),date,duration(${'$'}type,id),id,text,updated",
            body: BaseWorkItemWrite.IssueWorkItem? = null,
          ): BaseWorkItemRead.IssueWorkItem {
            val response = client.post("issues/${this.id.encodeURLParameter()}/timeTracking/workItems") {
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

        public class IssueWorkItemIdPath internal constructor(
          private val client: HttpClient,
          private val id: String,
          private val issueWorkItemId: String,
        ) {
          public val delete: Delete = Delete(client, id, issueWorkItemId)

          public val `get`: Get = Get(client, id, issueWorkItemId)

          public val post: Post = Post(client, id, issueWorkItemId)

          public class Delete internal constructor(
            private val client: HttpClient,
            private val id: String,
            private val issueWorkItemId: String,
          ) {
            public suspend operator fun invoke() {
              val response = client.delete("issues/${this.id.encodeURLParameter()}/timeTracking/workItems/${this.issueWorkItemId.encodeURLParameter()}")
              if (response.status.value != 200) throw ResponseException(response, "")
            }
          }

          public class Get internal constructor(
            private val client: HttpClient,
            private val id: String,
            private val issueWorkItemId: String,
          ) {
            public suspend operator fun invoke(fields: String? = "${'$'}type,author(${'$'}type,id,login,ringId),created,creator(${'$'}type,id,login,ringId),date,duration(${'$'}type,id),id,text,updated"): BaseWorkItemRead.IssueWorkItem {
              val response = client.get("issues/${this.id.encodeURLParameter()}/timeTracking/workItems/${this.issueWorkItemId.encodeURLParameter()}") {
                if (fields != null) {
                  this.url.encodedParameters.append("fields", fields.encodeURLParameter())
                }
              }
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }
          }

          public class Post internal constructor(
            private val client: HttpClient,
            private val id: String,
            private val issueWorkItemId: String,
          ) {
            public suspend operator fun invoke(
              muteUpdateNotifications: Boolean? = null,
              fields: String? = "${'$'}type,author(${'$'}type,id,login,ringId),created,creator(${'$'}type,id,login,ringId),date,duration(${'$'}type,id),id,text,updated",
              body: BaseWorkItemWrite.IssueWorkItem? = null,
            ): BaseWorkItemRead.IssueWorkItem {
              val response = client.post("issues/${this.id.encodeURLParameter()}/timeTracking/workItems/${this.issueWorkItemId.encodeURLParameter()}") {
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
        }
      }
    }

    public class VcsChanges internal constructor(
      private val client: HttpClient,
      private val id: String,
    ) {
      public val `get`: Get = Get(client, id)

      public val post: Post = Post(client, id)

      public fun vcsChangeId(vcsChangeId: String): VcsChangeIdPath = VcsChangeIdPath(client, id, vcsChangeId)

      public class Get internal constructor(
        private val client: HttpClient,
        private val id: String,
      ) {
        public suspend operator fun invoke(
          fields: String? = "${'$'}type,id",
          skip: Int? = null,
          top: Int? = null,
        ): List<VcsChangeRead> {
          val response = client.get("issues/${this.id.encodeURLParameter()}/vcsChanges") {
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
        private val client: HttpClient,
        private val id: String,
      ) {
        public suspend operator fun invoke(
          muteUpdateNotifications: Boolean? = null,
          fields: String? = "${'$'}type,id",
          body: VcsChangeWrite? = null,
        ): VcsChangeRead {
          val response = client.post("issues/${this.id.encodeURLParameter()}/vcsChanges") {
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

      public class VcsChangeIdPath internal constructor(
        private val client: HttpClient,
        private val id: String,
        private val vcsChangeId: String,
      ) {
        public val delete: Delete = Delete(client, id, vcsChangeId)

        public val `get`: Get = Get(client, id, vcsChangeId)

        public val post: Post = Post(client, id, vcsChangeId)

        public class Delete internal constructor(
          private val client: HttpClient,
          private val id: String,
          private val vcsChangeId: String,
        ) {
          public suspend operator fun invoke() {
            val response = client.delete("issues/${this.id.encodeURLParameter()}/vcsChanges/${this.vcsChangeId.encodeURLParameter()}")
            if (response.status.value != 200) throw ResponseException(response, "")
          }
        }

        public class Get internal constructor(
          private val client: HttpClient,
          private val id: String,
          private val vcsChangeId: String,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,id"): VcsChangeRead {
            val response = client.get("issues/${this.id.encodeURLParameter()}/vcsChanges/${this.vcsChangeId.encodeURLParameter()}") {
              if (fields != null) {
                this.url.encodedParameters.append("fields", fields.encodeURLParameter())
              }
            }
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class Post internal constructor(
          private val client: HttpClient,
          private val id: String,
          private val vcsChangeId: String,
        ) {
          public suspend operator fun invoke(
            muteUpdateNotifications: Boolean? = null,
            fields: String? = "${'$'}type,id",
            body: VcsChangeWrite? = null,
          ): VcsChangeRead {
            val response = client.post("issues/${this.id.encodeURLParameter()}/vcsChanges/${this.vcsChangeId.encodeURLParameter()}") {
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
      }
    }
  }
}
