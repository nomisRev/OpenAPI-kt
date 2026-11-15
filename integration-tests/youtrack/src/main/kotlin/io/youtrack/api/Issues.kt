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
      customFields: String? = null,
      fields: String? = "${'$'}type,created,customFields(${'$'}type,id,name,value(${'$'}type,id,name)),description,id,idReadable,links(${'$'}type,direction,id,linkType(${'$'}type,id,localizedName,name)),numberInProject,project(${'$'}type,id,name,shortName),reporter(${'$'}type,id,login,ringId),resolved,summary,updated,updater(${'$'}type,id,login,ringId),visibility(${'$'}type,id,permittedGroups(${'$'}type,id,name,ringId),permittedUsers(${'$'}type,id,login,ringId))",
      skip: Int? = null,
      top: Int? = null,
    ): List<IssueRead> {
      val response = __client.get("issues") {
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
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke(
      draftId: String? = null,
      muteUpdateNotifications: Boolean? = null,
      fields: String? = "${'$'}type,created,customFields(${'$'}type,id,name,value(${'$'}type,id,name)),description,id,idReadable,links(${'$'}type,direction,id,linkType(${'$'}type,id,localizedName,name)),numberInProject,project(${'$'}type,id,name,shortName),reporter(${'$'}type,id,login,ringId),resolved,summary,updated,updater(${'$'}type,id,login,ringId),visibility(${'$'}type,id,permittedGroups(${'$'}type,id,name,ringId),permittedUsers(${'$'}type,id,login,ringId))",
      body: IssueWrite? = null,
    ): IssueRead {
      val response = __client.post("issues") {
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
    private val __client: HttpClient,
    private val _id: String,
  ) {
    public val delete: Delete = Delete(__client, _id)

    public val `get`: Get = Get(__client, _id)

    public val post: Post = Post(__client, _id)

    public val activities: Activities = Activities(__client, _id)

    public val activitiesPage: ActivitiesPage = ActivitiesPage(__client, _id)

    public val attachments: Attachments = Attachments(__client, _id)

    public val comments: Comments = Comments(__client, _id)

    public val customFields: CustomFields = CustomFields(__client, _id)

    public val links: Links = Links(__client, _id)

    public val project: Project = Project(__client, _id)

    public val sprints: Sprints = Sprints(__client, _id)

    public val tags: Tags = Tags(__client, _id)

    public val timeTracking: TimeTracking = TimeTracking(__client, _id)

    public val vcsChanges: VcsChanges = VcsChanges(__client, _id)

    public class Delete internal constructor(
      private val __client: HttpClient,
      private val _id: String,
    ) {
      public suspend operator fun invoke() {
        val response = __client.delete("issues/${this._id.encodeURLParameter()}")
        if (response.status.value != 200) throw ResponseException(response, "")
      }
    }

    public class Get internal constructor(
      private val __client: HttpClient,
      private val _id: String,
    ) {
      public suspend operator fun invoke(fields: String? = "${'$'}type,created,customFields(${'$'}type,id,name,value(${'$'}type,id,name)),description,id,idReadable,links(${'$'}type,direction,id,linkType(${'$'}type,id,localizedName,name)),numberInProject,project(${'$'}type,id,name,shortName),reporter(${'$'}type,id,login,ringId),resolved,summary,updated,updater(${'$'}type,id,login,ringId),visibility(${'$'}type,id,permittedGroups(${'$'}type,id,name,ringId),permittedUsers(${'$'}type,id,login,ringId))"): IssueRead {
        val response = __client.get("issues/${this._id.encodeURLParameter()}") {
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
      public suspend operator fun invoke(
        muteUpdateNotifications: Boolean? = null,
        fields: String? = "${'$'}type,created,customFields(${'$'}type,id,name,value(${'$'}type,id,name)),description,id,idReadable,links(${'$'}type,direction,id,linkType(${'$'}type,id,localizedName,name)),numberInProject,project(${'$'}type,id,name,shortName),reporter(${'$'}type,id,login,ringId),resolved,summary,updated,updater(${'$'}type,id,login,ringId),visibility(${'$'}type,id,permittedGroups(${'$'}type,id,name,ringId),permittedUsers(${'$'}type,id,login,ringId))",
        body: IssueWrite? = null,
      ): IssueRead {
        val response = __client.post("issues/${this._id.encodeURLParameter()}") {
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
      private val __client: HttpClient,
      private val _id: String,
    ) {
      public val `get`: Get = Get(__client, _id)

      public fun activityItemId(activityItemId: String): ActivityItemIdPath = ActivityItemIdPath(__client, _id, activityItemId)

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _id: String,
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
          val response = __client.get("issues/${this._id.encodeURLParameter()}/activities") {
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
        private val __client: HttpClient,
        private val _id: String,
        private val _activityItemId: String,
      ) {
        public val `get`: Get = Get(__client, _id, _activityItemId)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _id: String,
          private val _activityItemId: String,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,added,author(${'$'}type,id,login,ringId),category(${'$'}type,id),field(${'$'}type,customField(${'$'}type,fieldType(${'$'}type,id),id,localizedName,name),id,name),id,removed,target,targetMember,timestamp"): ActivityItem {
            val response = __client.get("issues/${this._id.encodeURLParameter()}/activities/${this._activityItemId.encodeURLParameter()}") {
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
      private val __client: HttpClient,
      private val _id: String,
    ) {
      public val `get`: Get = Get(__client, _id)

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _id: String,
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
          val response = __client.get("issues/${this._id.encodeURLParameter()}/activitiesPage") {
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
      private val __client: HttpClient,
      private val _id: String,
    ) {
      public val `get`: Get = Get(__client, _id)

      public val post: Post = Post(__client, _id)

      public fun issueAttachmentId(issueAttachmentId: String): IssueAttachmentIdPath = IssueAttachmentIdPath(__client, _id, issueAttachmentId)

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _id: String,
      ) {
        public suspend operator fun invoke(
          fields: String? = "${'$'}type,author(${'$'}type,id,login,ringId),charset,created,extension,id,metaData,mimeType,name,size,updated,url",
          skip: Int? = null,
          top: Int? = null,
        ): List<IssueAttachmentRead> {
          val response = __client.get("issues/${this._id.encodeURLParameter()}/attachments") {
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
        public suspend operator fun invoke(muteUpdateNotifications: Boolean? = null, fields: String? = "${'$'}type,author(${'$'}type,id,login,ringId),charset,created,extension,id,metaData,mimeType,name,size,updated,url"): List<IssueAttachmentRead> {
          val response = __client.post("issues/${this._id.encodeURLParameter()}/attachments") {
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
          val response = __client.post("issues/${this._id.encodeURLParameter()}/attachments") {
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
        private val __client: HttpClient,
        private val _id: String,
        private val _issueAttachmentId: String,
      ) {
        public val delete: Delete = Delete(__client, _id, _issueAttachmentId)

        public val `get`: Get = Get(__client, _id, _issueAttachmentId)

        public val post: Post = Post(__client, _id, _issueAttachmentId)

        public class Delete internal constructor(
          private val __client: HttpClient,
          private val _id: String,
          private val _issueAttachmentId: String,
        ) {
          public suspend operator fun invoke() {
            val response = __client.delete("issues/${this._id.encodeURLParameter()}/attachments/${this._issueAttachmentId.encodeURLParameter()}")
            if (response.status.value != 200) throw ResponseException(response, "")
          }
        }

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _id: String,
          private val _issueAttachmentId: String,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,author(${'$'}type,id,login,ringId),charset,created,extension,id,metaData,mimeType,name,size,updated,url"): IssueAttachmentRead {
            val response = __client.get("issues/${this._id.encodeURLParameter()}/attachments/${this._issueAttachmentId.encodeURLParameter()}") {
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
          private val _issueAttachmentId: String,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,author(${'$'}type,id,login,ringId),charset,created,extension,id,metaData,mimeType,name,size,updated,url", body: IssueAttachmentWrite? = null): IssueAttachmentRead {
            val response = __client.post("issues/${this._id.encodeURLParameter()}/attachments/${this._issueAttachmentId.encodeURLParameter()}") {
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
      private val __client: HttpClient,
      private val _id: String,
    ) {
      public val `get`: Get = Get(__client, _id)

      public val post: Post = Post(__client, _id)

      public fun issueCommentId(issueCommentId: String): IssueCommentIdPath = IssueCommentIdPath(__client, _id, issueCommentId)

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _id: String,
      ) {
        public suspend operator fun invoke(
          fields: String? = "${'$'}type,attachments(${'$'}type,id),author(${'$'}type,id,login,ringId),created,deleted,id,text,updated,visibility(${'$'}type,id,permittedGroups(${'$'}type,id,name,ringId),permittedUsers(${'$'}type,id,login,ringId))",
          skip: Int? = null,
          top: Int? = null,
        ): List<IssueCommentRead> {
          val response = __client.get("issues/${this._id.encodeURLParameter()}/comments") {
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
          draftId: String? = null,
          muteUpdateNotifications: Boolean? = null,
          fields: String? = "${'$'}type,attachments(${'$'}type,id),author(${'$'}type,id,login,ringId),created,deleted,id,text,updated,visibility(${'$'}type,id,permittedGroups(${'$'}type,id,name,ringId),permittedUsers(${'$'}type,id,login,ringId))",
          body: IssueCommentWrite? = null,
        ): IssueCommentRead {
          val response = __client.post("issues/${this._id.encodeURLParameter()}/comments") {
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
        private val __client: HttpClient,
        private val _id: String,
        private val _issueCommentId: String,
      ) {
        public val delete: Delete = Delete(__client, _id, _issueCommentId)

        public val `get`: Get = Get(__client, _id, _issueCommentId)

        public val post: Post = Post(__client, _id, _issueCommentId)

        public val reactions: Reactions = Reactions(__client, _id, _issueCommentId)

        public class Delete internal constructor(
          private val __client: HttpClient,
          private val _id: String,
          private val _issueCommentId: String,
        ) {
          public suspend operator fun invoke() {
            val response = __client.delete("issues/${this._id.encodeURLParameter()}/comments/${this._issueCommentId.encodeURLParameter()}")
            if (response.status.value != 200) throw ResponseException(response, "")
          }
        }

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _id: String,
          private val _issueCommentId: String,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,attachments(${'$'}type,id),author(${'$'}type,id,login,ringId),created,deleted,id,text,updated,visibility(${'$'}type,id,permittedGroups(${'$'}type,id,name,ringId),permittedUsers(${'$'}type,id,login,ringId))"): IssueCommentRead {
            val response = __client.get("issues/${this._id.encodeURLParameter()}/comments/${this._issueCommentId.encodeURLParameter()}") {
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
          private val _issueCommentId: String,
        ) {
          public suspend operator fun invoke(
            muteUpdateNotifications: Boolean? = null,
            fields: String? = "${'$'}type,attachments(${'$'}type,id),author(${'$'}type,id,login,ringId),created,deleted,id,text,updated,visibility(${'$'}type,id,permittedGroups(${'$'}type,id,name,ringId),permittedUsers(${'$'}type,id,login,ringId))",
            body: IssueCommentWrite? = null,
          ): IssueCommentRead {
            val response = __client.post("issues/${this._id.encodeURLParameter()}/comments/${this._issueCommentId.encodeURLParameter()}") {
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
          private val __client: HttpClient,
          private val _id: String,
          private val _issueCommentId: String,
        ) {
          public val `get`: Get = Get(__client, _id, _issueCommentId)

          public val post: Post = Post(__client, _id, _issueCommentId)

          public fun reactionId(reactionId: String): ReactionIdPath = ReactionIdPath(__client, _id, _issueCommentId, reactionId)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _id: String,
            private val _issueCommentId: String,
          ) {
            public suspend operator fun invoke(
              fields: String? = "${'$'}type,id",
              skip: Int? = null,
              top: Int? = null,
            ): List<ReactionRead> {
              val response = __client.get("issues/${this._id.encodeURLParameter()}/comments/${this._issueCommentId.encodeURLParameter()}/reactions") {
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
            private val _issueCommentId: String,
          ) {
            public suspend operator fun invoke(fields: String? = "${'$'}type,id", body: ReactionWrite? = null): ReactionRead {
              val response = __client.post("issues/${this._id.encodeURLParameter()}/comments/${this._issueCommentId.encodeURLParameter()}/reactions") {
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
            private val __client: HttpClient,
            private val _id: String,
            private val _issueCommentId: String,
            private val _reactionId: String,
          ) {
            public val delete: Delete = Delete(__client, _id, _issueCommentId, _reactionId)

            public val `get`: Get = Get(__client, _id, _issueCommentId, _reactionId)

            public class Delete internal constructor(
              private val __client: HttpClient,
              private val _id: String,
              private val _issueCommentId: String,
              private val _reactionId: String,
            ) {
              public suspend operator fun invoke() {
                val response = __client.delete("issues/${this._id.encodeURLParameter()}/comments/${this._issueCommentId.encodeURLParameter()}/reactions/${this._reactionId.encodeURLParameter()}")
                if (response.status.value != 200) throw ResponseException(response, "")
              }
            }

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _id: String,
              private val _issueCommentId: String,
              private val _reactionId: String,
            ) {
              public suspend operator fun invoke(fields: String? = "${'$'}type,id"): ReactionRead {
                val response = __client.get("issues/${this._id.encodeURLParameter()}/comments/${this._issueCommentId.encodeURLParameter()}/reactions/${this._reactionId.encodeURLParameter()}") {
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
      private val __client: HttpClient,
      private val _id: String,
    ) {
      public val `get`: Get = Get(__client, _id)

      public fun issueCustomFieldId(issueCustomFieldId: String): IssueCustomFieldIdPath = IssueCustomFieldIdPath(__client, _id, issueCustomFieldId)

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _id: String,
      ) {
        public suspend operator fun invoke(
          fields: String? = "${'$'}type,id,name,value(${'$'}type,id,name)",
          skip: Int? = null,
          top: Int? = null,
        ): List<IssueCustomFieldRead> {
          val response = __client.get("issues/${this._id.encodeURLParameter()}/customFields") {
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
        private val __client: HttpClient,
        private val _id: String,
        private val _issueCustomFieldId: String,
      ) {
        public val `get`: Get = Get(__client, _id, _issueCustomFieldId)

        public val post: Post = Post(__client, _id, _issueCustomFieldId)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _id: String,
          private val _issueCustomFieldId: String,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,id,name,value(${'$'}type,id,name)"): IssueCustomFieldRead {
            val response = __client.get("issues/${this._id.encodeURLParameter()}/customFields/${this._issueCustomFieldId.encodeURLParameter()}") {
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
          private val _issueCustomFieldId: String,
        ) {
          public suspend operator fun invoke(
            muteUpdateNotifications: Boolean? = null,
            fields: String? = "${'$'}type,id,name,value(${'$'}type,id,name)",
            body: IssueCustomFieldWrite? = null,
          ): IssueCustomFieldRead {
            val response = __client.post("issues/${this._id.encodeURLParameter()}/customFields/${this._issueCustomFieldId.encodeURLParameter()}") {
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
      private val __client: HttpClient,
      private val _id: String,
    ) {
      public val `get`: Get = Get(__client, _id)

      public fun issueLinkId(issueLinkId: String): IssueLinkIdPath = IssueLinkIdPath(__client, _id, issueLinkId)

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _id: String,
      ) {
        public suspend operator fun invoke(
          fields: String? = "${'$'}type,direction,id,linkType(${'$'}type,id,localizedName,name)",
          skip: Int? = null,
          top: Int? = null,
        ): List<IssueLink> {
          val response = __client.get("issues/${this._id.encodeURLParameter()}/links") {
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
        private val __client: HttpClient,
        private val _id: String,
        private val _issueLinkId: String,
      ) {
        public val `get`: Get = Get(__client, _id, _issueLinkId)

        public val issues: Issues = Issues(__client, _id, _issueLinkId)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _id: String,
          private val _issueLinkId: String,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,direction,id,linkType(${'$'}type,id,localizedName,name)"): IssueLink {
            val response = __client.get("issues/${this._id.encodeURLParameter()}/links/${this._issueLinkId.encodeURLParameter()}") {
              if (fields != null) {
                this.url.encodedParameters.append("fields", fields.encodeURLParameter())
              }
            }
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class Issues internal constructor(
          private val __client: HttpClient,
          private val _id: String,
          private val _issueLinkId: String,
        ) {
          public val `get`: Get = Get(__client, _id, _issueLinkId)

          public val post: Post = Post(__client, _id, _issueLinkId)

          public fun issueId(issueId: String): IssueIdPath = IssueIdPath(__client, _id, _issueLinkId, issueId)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _id: String,
            private val _issueLinkId: String,
          ) {
            public suspend operator fun invoke(
              fields: String? = "${'$'}type,created,customFields(${'$'}type,id,name,value(${'$'}type,id,name)),description,id,idReadable,links(${'$'}type,direction,id,linkType(${'$'}type,id,localizedName,name)),numberInProject,project(${'$'}type,id,name,shortName),reporter(${'$'}type,id,login,ringId),resolved,summary,updated,updater(${'$'}type,id,login,ringId),visibility(${'$'}type,id,permittedGroups(${'$'}type,id,name,ringId),permittedUsers(${'$'}type,id,login,ringId))",
              skip: Int? = null,
              top: Int? = null,
            ): List<IssueRead> {
              val response = __client.get("issues/${this._id.encodeURLParameter()}/links/${this._issueLinkId.encodeURLParameter()}/issues") {
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
            private val _issueLinkId: String,
          ) {
            public suspend operator fun invoke(
              muteUpdateNotifications: Boolean? = null,
              fields: String? = "${'$'}type,created,customFields(${'$'}type,id,name,value(${'$'}type,id,name)),description,id,idReadable,links(${'$'}type,direction,id,linkType(${'$'}type,id,localizedName,name)),numberInProject,project(${'$'}type,id,name,shortName),reporter(${'$'}type,id,login,ringId),resolved,summary,updated,updater(${'$'}type,id,login,ringId),visibility(${'$'}type,id,permittedGroups(${'$'}type,id,name,ringId),permittedUsers(${'$'}type,id,login,ringId))",
              body: IssueWrite? = null,
            ): IssueRead {
              val response = __client.post("issues/${this._id.encodeURLParameter()}/links/${this._issueLinkId.encodeURLParameter()}/issues") {
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
            private val __client: HttpClient,
            private val _id: String,
            private val _issueLinkId: String,
            private val _issueId: String,
          ) {
            public val delete: Delete = Delete(__client, _id, _issueLinkId, _issueId)

            public class Delete internal constructor(
              private val __client: HttpClient,
              private val _id: String,
              private val _issueLinkId: String,
              private val _issueId: String,
            ) {
              public suspend operator fun invoke() {
                val response = __client.delete("issues/${this._id.encodeURLParameter()}/links/${this._issueLinkId.encodeURLParameter()}/issues/${this._issueId.encodeURLParameter()}")
                if (response.status.value != 200) throw ResponseException(response, "")
              }
            }
          }
        }
      }
    }

    public class Project internal constructor(
      private val __client: HttpClient,
      private val _id: String,
    ) {
      public val `get`: Get = Get(__client, _id)

      public val post: Post = Post(__client, _id)

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _id: String,
      ) {
        public suspend operator fun invoke(fields: String? = "${'$'}type,archived,customFields,id,leader(${'$'}type,id,login,ringId),name,shortName"): IssueFolderRead.Project {
          val response = __client.get("issues/${this._id.encodeURLParameter()}/project") {
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
        public suspend operator fun invoke(
          muteUpdateNotifications: Boolean? = null,
          fields: String? = "${'$'}type,archived,customFields,id,leader(${'$'}type,id,login,ringId),name,shortName",
          body: IssueFolderWrite.Project? = null,
        ): IssueFolderRead.Project {
          val response = __client.post("issues/${this._id.encodeURLParameter()}/project") {
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
      private val __client: HttpClient,
      private val _id: String,
    ) {
      public val `get`: Get = Get(__client, _id)

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _id: String,
      ) {
        public suspend operator fun invoke(
          fields: String? = "${'$'}type,archived,finish,id,isDefault,name,start",
          skip: Int? = null,
          top: Int? = null,
        ): List<SprintRead> {
          val response = __client.get("issues/${this._id.encodeURLParameter()}/sprints") {
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

      public val post: Post = Post(__client, _id)

      public fun tagId(tagId: String): TagIdPath = TagIdPath(__client, _id, tagId)

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _id: String,
      ) {
        public suspend operator fun invoke(
          fields: String? = "${'$'}type,id,name,owner(${'$'}type,id,login,ringId)",
          skip: Int? = null,
          top: Int? = null,
        ): List<IssueFolderRead.Tag> {
          val response = __client.get("issues/${this._id.encodeURLParameter()}/tags") {
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
        public suspend operator fun invoke(fields: String? = "${'$'}type,id,name,owner(${'$'}type,id,login,ringId)", body: IssueFolderWrite.Tag? = null): IssueFolderRead.Tag {
          val response = __client.post("issues/${this._id.encodeURLParameter()}/tags") {
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
        private val __client: HttpClient,
        private val _id: String,
        private val _tagId: String,
      ) {
        public val delete: Delete = Delete(__client, _id, _tagId)

        public val `get`: Get = Get(__client, _id, _tagId)

        public class Delete internal constructor(
          private val __client: HttpClient,
          private val _id: String,
          private val _tagId: String,
        ) {
          public suspend operator fun invoke() {
            val response = __client.delete("issues/${this._id.encodeURLParameter()}/tags/${this._tagId.encodeURLParameter()}")
            if (response.status.value != 200) throw ResponseException(response, "")
          }
        }

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _id: String,
          private val _tagId: String,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,id,name,owner(${'$'}type,id,login,ringId)"): IssueFolderRead.Tag {
            val response = __client.get("issues/${this._id.encodeURLParameter()}/tags/${this._tagId.encodeURLParameter()}") {
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
      private val __client: HttpClient,
      private val _id: String,
    ) {
      public val `get`: Get = Get(__client, _id)

      public val workItems: WorkItems = WorkItems(__client, _id)

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _id: String,
      ) {
        public suspend operator fun invoke(fields: String? = "${'$'}type,enabled,id"): IssueTimeTracker {
          val response = __client.get("issues/${this._id.encodeURLParameter()}/timeTracking") {
            if (fields != null) {
              this.url.encodedParameters.append("fields", fields.encodeURLParameter())
            }
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
      }

      public class WorkItems internal constructor(
        private val __client: HttpClient,
        private val _id: String,
      ) {
        public val `get`: Get = Get(__client, _id)

        public val post: Post = Post(__client, _id)

        public fun issueWorkItemId(issueWorkItemId: String): IssueWorkItemIdPath = IssueWorkItemIdPath(__client, _id, issueWorkItemId)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _id: String,
        ) {
          public suspend operator fun invoke(
            fields: String? = "${'$'}type,author(${'$'}type,id,login,ringId),created,creator(${'$'}type,id,login,ringId),date,duration(${'$'}type,id),id,text,updated",
            skip: Int? = null,
            top: Int? = null,
          ): List<BaseWorkItemRead.IssueWorkItem> {
            val response = __client.get("issues/${this._id.encodeURLParameter()}/timeTracking/workItems") {
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
            fields: String? = "${'$'}type,author(${'$'}type,id,login,ringId),created,creator(${'$'}type,id,login,ringId),date,duration(${'$'}type,id),id,text,updated",
            body: BaseWorkItemWrite.IssueWorkItem? = null,
          ): BaseWorkItemRead.IssueWorkItem {
            val response = __client.post("issues/${this._id.encodeURLParameter()}/timeTracking/workItems") {
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
          private val __client: HttpClient,
          private val _id: String,
          private val _issueWorkItemId: String,
        ) {
          public val delete: Delete = Delete(__client, _id, _issueWorkItemId)

          public val `get`: Get = Get(__client, _id, _issueWorkItemId)

          public val post: Post = Post(__client, _id, _issueWorkItemId)

          public class Delete internal constructor(
            private val __client: HttpClient,
            private val _id: String,
            private val _issueWorkItemId: String,
          ) {
            public suspend operator fun invoke() {
              val response = __client.delete("issues/${this._id.encodeURLParameter()}/timeTracking/workItems/${this._issueWorkItemId.encodeURLParameter()}")
              if (response.status.value != 200) throw ResponseException(response, "")
            }
          }

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _id: String,
            private val _issueWorkItemId: String,
          ) {
            public suspend operator fun invoke(fields: String? = "${'$'}type,author(${'$'}type,id,login,ringId),created,creator(${'$'}type,id,login,ringId),date,duration(${'$'}type,id),id,text,updated"): BaseWorkItemRead.IssueWorkItem {
              val response = __client.get("issues/${this._id.encodeURLParameter()}/timeTracking/workItems/${this._issueWorkItemId.encodeURLParameter()}") {
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
            private val _issueWorkItemId: String,
          ) {
            public suspend operator fun invoke(
              muteUpdateNotifications: Boolean? = null,
              fields: String? = "${'$'}type,author(${'$'}type,id,login,ringId),created,creator(${'$'}type,id,login,ringId),date,duration(${'$'}type,id),id,text,updated",
              body: BaseWorkItemWrite.IssueWorkItem? = null,
            ): BaseWorkItemRead.IssueWorkItem {
              val response = __client.post("issues/${this._id.encodeURLParameter()}/timeTracking/workItems/${this._issueWorkItemId.encodeURLParameter()}") {
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
      private val __client: HttpClient,
      private val _id: String,
    ) {
      public val `get`: Get = Get(__client, _id)

      public val post: Post = Post(__client, _id)

      public fun vcsChangeId(vcsChangeId: String): VcsChangeIdPath = VcsChangeIdPath(__client, _id, vcsChangeId)

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _id: String,
      ) {
        public suspend operator fun invoke(
          fields: String? = "${'$'}type,id",
          skip: Int? = null,
          top: Int? = null,
        ): List<VcsChangeRead> {
          val response = __client.get("issues/${this._id.encodeURLParameter()}/vcsChanges") {
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
          fields: String? = "${'$'}type,id",
          body: VcsChangeWrite? = null,
        ): VcsChangeRead {
          val response = __client.post("issues/${this._id.encodeURLParameter()}/vcsChanges") {
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
        private val __client: HttpClient,
        private val _id: String,
        private val _vcsChangeId: String,
      ) {
        public val delete: Delete = Delete(__client, _id, _vcsChangeId)

        public val `get`: Get = Get(__client, _id, _vcsChangeId)

        public val post: Post = Post(__client, _id, _vcsChangeId)

        public class Delete internal constructor(
          private val __client: HttpClient,
          private val _id: String,
          private val _vcsChangeId: String,
        ) {
          public suspend operator fun invoke() {
            val response = __client.delete("issues/${this._id.encodeURLParameter()}/vcsChanges/${this._vcsChangeId.encodeURLParameter()}")
            if (response.status.value != 200) throw ResponseException(response, "")
          }
        }

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _id: String,
          private val _vcsChangeId: String,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,id"): VcsChangeRead {
            val response = __client.get("issues/${this._id.encodeURLParameter()}/vcsChanges/${this._vcsChangeId.encodeURLParameter()}") {
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
          private val _vcsChangeId: String,
        ) {
          public suspend operator fun invoke(
            muteUpdateNotifications: Boolean? = null,
            fields: String? = "${'$'}type,id",
            body: VcsChangeWrite? = null,
          ): VcsChangeRead {
            val response = __client.post("issues/${this._id.encodeURLParameter()}/vcsChanges/${this._vcsChangeId.encodeURLParameter()}") {
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
