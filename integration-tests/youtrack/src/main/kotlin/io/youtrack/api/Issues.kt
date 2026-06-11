package io.youtrack.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.`get`
import io.ktor.client.request.delete
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
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
    ): List<IssueRead> = client.get("/issues") {
      query?.let { parameter("query", it) }
      customFields?.let { parameter("customFields", it) }
      fields?.let { parameter("fields", it) }
      skip?.let { parameter("${'$'}skip", it) }
      top?.let { parameter("${'$'}top", it) }
    }.body()
  }

  public class Post internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(
      draftId: String? = null,
      muteUpdateNotifications: Boolean? = null,
      fields: String? = "${'$'}type,created,customFields(${'$'}type,id,name,value(${'$'}type,id,name)),description,id,idReadable,links(${'$'}type,direction,id,linkType(${'$'}type,id,localizedName,name)),numberInProject,project(${'$'}type,id,name,shortName),reporter(${'$'}type,id,login,ringId),resolved,summary,updated,updater(${'$'}type,id,login,ringId),visibility(${'$'}type,id,permittedGroups(${'$'}type,id,name,ringId),permittedUsers(${'$'}type,id,login,ringId))",
      body: IssueWrite? = null,
    ): IssueRead = client.post("/issues") {
      draftId?.let { parameter("draftId", it) }
      muteUpdateNotifications?.let { parameter("muteUpdateNotifications", it) }
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
        client.delete("/issues/$id")
      }
    }

    public class Get internal constructor(
      private val client: HttpClient,
      private val id: String,
    ) {
      public suspend operator fun invoke(fields: String? = "${'$'}type,created,customFields(${'$'}type,id,name,value(${'$'}type,id,name)),description,id,idReadable,links(${'$'}type,direction,id,linkType(${'$'}type,id,localizedName,name)),numberInProject,project(${'$'}type,id,name,shortName),reporter(${'$'}type,id,login,ringId),resolved,summary,updated,updater(${'$'}type,id,login,ringId),visibility(${'$'}type,id,permittedGroups(${'$'}type,id,name,ringId),permittedUsers(${'$'}type,id,login,ringId))"): IssueRead = client.get("/issues/$id") {
        fields?.let { parameter("fields", it) }
      }.body()
    }

    public class Post internal constructor(
      private val client: HttpClient,
      private val id: String,
    ) {
      public suspend operator fun invoke(
        muteUpdateNotifications: Boolean? = null,
        fields: String? = "${'$'}type,created,customFields(${'$'}type,id,name,value(${'$'}type,id,name)),description,id,idReadable,links(${'$'}type,direction,id,linkType(${'$'}type,id,localizedName,name)),numberInProject,project(${'$'}type,id,name,shortName),reporter(${'$'}type,id,login,ringId),resolved,summary,updated,updater(${'$'}type,id,login,ringId),visibility(${'$'}type,id,permittedGroups(${'$'}type,id,name,ringId),permittedUsers(${'$'}type,id,login,ringId))",
        body: IssueWrite? = null,
      ): IssueRead = client.post("/issues/$id") {
        muteUpdateNotifications?.let { parameter("muteUpdateNotifications", it) }
        fields?.let { parameter("fields", it) }
        body?.let {
          contentType(ContentType.Application.Json)
          setBody(it)
        }
      }.body()
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
        ): List<ActivityItem> = client.get("/issues/$id/activities") {
          categories?.let { parameter("categories", it) }
          reverse?.let { parameter("reverse", it) }
          start?.let { parameter("start", it) }
          end?.let { parameter("end", it) }
          author?.let { parameter("author", it) }
          fields?.let { parameter("fields", it) }
          skip?.let { parameter("${'$'}skip", it) }
          top?.let { parameter("${'$'}top", it) }
        }.body()
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
          public suspend operator fun invoke(fields: String? = "${'$'}type,added,author(${'$'}type,id,login,ringId),category(${'$'}type,id),field(${'$'}type,customField(${'$'}type,fieldType(${'$'}type,id),id,localizedName,name),id,name),id,removed,target,targetMember,timestamp"): ActivityItem = client.get("/issues/$id/activities/$activityItemId") {
            fields?.let { parameter("fields", it) }
          }.body()
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
        ): ActivityCursorPage = client.get("/issues/$id/activitiesPage") {
          categories?.let { parameter("categories", it) }
          reverse?.let { parameter("reverse", it) }
          start?.let { parameter("start", it) }
          end?.let { parameter("end", it) }
          author?.let { parameter("author", it) }
          cursor?.let { parameter("cursor", it) }
          activityId?.let { parameter("activityId", it) }
          fields?.let { parameter("fields", it) }
        }.body()
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
        ): List<IssueAttachmentRead> = client.get("/issues/$id/attachments") {
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
          fields: String? = "${'$'}type,author(${'$'}type,id,login,ringId),charset,created,extension,id,metaData,mimeType,name,size,updated,url",
          files0: ByteArray? = null,
        ): List<IssueAttachmentRead> = client.post("/issues/$id/attachments") {
          muteUpdateNotifications?.let { parameter("muteUpdateNotifications", it) }
          fields?.let { parameter("fields", it) }
          setBody(MultiPartFormDataContent(formData {
            if (files0 != null) {
              append("files[0]", files0)
            }
          }))
        }.body()
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
            client.delete("/issues/$id/attachments/$issueAttachmentId")
          }
        }

        public class Get internal constructor(
          private val client: HttpClient,
          private val id: String,
          private val issueAttachmentId: String,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,author(${'$'}type,id,login,ringId),charset,created,extension,id,metaData,mimeType,name,size,updated,url"): IssueAttachmentRead = client.get("/issues/$id/attachments/$issueAttachmentId") {
            fields?.let { parameter("fields", it) }
          }.body()
        }

        public class Post internal constructor(
          private val client: HttpClient,
          private val id: String,
          private val issueAttachmentId: String,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,author(${'$'}type,id,login,ringId),charset,created,extension,id,metaData,mimeType,name,size,updated,url", body: IssueAttachmentWrite? = null): IssueAttachmentRead = client.post("/issues/$id/attachments/$issueAttachmentId") {
            fields?.let { parameter("fields", it) }
            body?.let {
              contentType(ContentType.Application.Json)
              setBody(it)
            }
          }.body()
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
        ): List<IssueCommentRead> = client.get("/issues/$id/comments") {
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
          draftId: String? = null,
          muteUpdateNotifications: Boolean? = null,
          fields: String? = "${'$'}type,attachments(${'$'}type,id),author(${'$'}type,id,login,ringId),created,deleted,id,text,updated,visibility(${'$'}type,id,permittedGroups(${'$'}type,id,name,ringId),permittedUsers(${'$'}type,id,login,ringId))",
          body: IssueCommentWrite? = null,
        ): IssueCommentRead = client.post("/issues/$id/comments") {
          draftId?.let { parameter("draftId", it) }
          muteUpdateNotifications?.let { parameter("muteUpdateNotifications", it) }
          fields?.let { parameter("fields", it) }
          body?.let {
            contentType(ContentType.Application.Json)
            setBody(it)
          }
        }.body()
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
            client.delete("/issues/$id/comments/$issueCommentId")
          }
        }

        public class Get internal constructor(
          private val client: HttpClient,
          private val id: String,
          private val issueCommentId: String,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,attachments(${'$'}type,id),author(${'$'}type,id,login,ringId),created,deleted,id,text,updated,visibility(${'$'}type,id,permittedGroups(${'$'}type,id,name,ringId),permittedUsers(${'$'}type,id,login,ringId))"): IssueCommentRead = client.get("/issues/$id/comments/$issueCommentId") {
            fields?.let { parameter("fields", it) }
          }.body()
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
          ): IssueCommentRead = client.post("/issues/$id/comments/$issueCommentId") {
            muteUpdateNotifications?.let { parameter("muteUpdateNotifications", it) }
            fields?.let { parameter("fields", it) }
            body?.let {
              contentType(ContentType.Application.Json)
              setBody(it)
            }
          }.body()
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
            ): List<ReactionRead> = client.get("/issues/$id/comments/$issueCommentId/reactions") {
              fields?.let { parameter("fields", it) }
              skip?.let { parameter("${'$'}skip", it) }
              top?.let { parameter("${'$'}top", it) }
            }.body()
          }

          public class Post internal constructor(
            private val client: HttpClient,
            private val id: String,
            private val issueCommentId: String,
          ) {
            public suspend operator fun invoke(fields: String? = "${'$'}type,id", body: ReactionWrite? = null): ReactionRead = client.post("/issues/$id/comments/$issueCommentId/reactions") {
              fields?.let { parameter("fields", it) }
              body?.let {
                contentType(ContentType.Application.Json)
                setBody(it)
              }
            }.body()
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
                client.delete("/issues/$id/comments/$issueCommentId/reactions/$reactionId")
              }
            }

            public class Get internal constructor(
              private val client: HttpClient,
              private val id: String,
              private val issueCommentId: String,
              private val reactionId: String,
            ) {
              public suspend operator fun invoke(fields: String? = "${'$'}type,id"): ReactionRead = client.get("/issues/$id/comments/$issueCommentId/reactions/$reactionId") {
                fields?.let { parameter("fields", it) }
              }.body()
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
        ): List<IssueCustomFieldRead> = client.get("/issues/$id/customFields") {
          fields?.let { parameter("fields", it) }
          skip?.let { parameter("${'$'}skip", it) }
          top?.let { parameter("${'$'}top", it) }
        }.body()
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
          public suspend operator fun invoke(fields: String? = "${'$'}type,id,name,value(${'$'}type,id,name)"): IssueCustomFieldRead = client.get("/issues/$id/customFields/$issueCustomFieldId") {
            fields?.let { parameter("fields", it) }
          }.body()
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
          ): IssueCustomFieldRead = client.post("/issues/$id/customFields/$issueCustomFieldId") {
            muteUpdateNotifications?.let { parameter("muteUpdateNotifications", it) }
            fields?.let { parameter("fields", it) }
            body?.let {
              contentType(ContentType.Application.Json)
              setBody(it)
            }
          }.body()
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
        ): List<IssueLink> = client.get("/issues/$id/links") {
          fields?.let { parameter("fields", it) }
          skip?.let { parameter("${'$'}skip", it) }
          top?.let { parameter("${'$'}top", it) }
        }.body()
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
          public suspend operator fun invoke(fields: String? = "${'$'}type,direction,id,linkType(${'$'}type,id,localizedName,name)"): IssueLink = client.get("/issues/$id/links/$issueLinkId") {
            fields?.let { parameter("fields", it) }
          }.body()
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
            ): List<IssueRead> = client.get("/issues/$id/links/$issueLinkId/issues") {
              fields?.let { parameter("fields", it) }
              skip?.let { parameter("${'$'}skip", it) }
              top?.let { parameter("${'$'}top", it) }
            }.body()
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
            ): IssueRead = client.post("/issues/$id/links/$issueLinkId/issues") {
              muteUpdateNotifications?.let { parameter("muteUpdateNotifications", it) }
              fields?.let { parameter("fields", it) }
              body?.let {
                contentType(ContentType.Application.Json)
                setBody(it)
              }
            }.body()
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
                client.delete("/issues/$id/links/$issueLinkId/issues/$issueId")
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
        public suspend operator fun invoke(fields: String? = "${'$'}type,archived,customFields,id,leader(${'$'}type,id,login,ringId),name,shortName"): IssueFolderRead.Project = client.get("/issues/$id/project") {
          fields?.let { parameter("fields", it) }
        }.body()
      }

      public class Post internal constructor(
        private val client: HttpClient,
        private val id: String,
      ) {
        public suspend operator fun invoke(
          muteUpdateNotifications: Boolean? = null,
          fields: String? = "${'$'}type,archived,customFields,id,leader(${'$'}type,id,login,ringId),name,shortName",
          body: IssueFolderWrite.Project? = null,
        ): IssueFolderRead.Project = client.post("/issues/$id/project") {
          muteUpdateNotifications?.let { parameter("muteUpdateNotifications", it) }
          fields?.let { parameter("fields", it) }
          body?.let {
            contentType(ContentType.Application.Json)
            setBody(it)
          }
        }.body()
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
        ): List<SprintRead> = client.get("/issues/$id/sprints") {
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
        ): List<IssueFolderRead.Tag> = client.get("/issues/$id/tags") {
          fields?.let { parameter("fields", it) }
          skip?.let { parameter("${'$'}skip", it) }
          top?.let { parameter("${'$'}top", it) }
        }.body()
      }

      public class Post internal constructor(
        private val client: HttpClient,
        private val id: String,
      ) {
        public suspend operator fun invoke(fields: String? = "${'$'}type,id,name,owner(${'$'}type,id,login,ringId)", body: IssueFolderWrite.Tag? = null): IssueFolderRead.Tag = client.post("/issues/$id/tags") {
          fields?.let { parameter("fields", it) }
          body?.let {
            contentType(ContentType.Application.Json)
            setBody(it)
          }
        }.body()
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
            client.delete("/issues/$id/tags/$tagId")
          }
        }

        public class Get internal constructor(
          private val client: HttpClient,
          private val id: String,
          private val tagId: String,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,id,name,owner(${'$'}type,id,login,ringId)"): IssueFolderRead.Tag = client.get("/issues/$id/tags/$tagId") {
            fields?.let { parameter("fields", it) }
          }.body()
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
        public suspend operator fun invoke(fields: String? = "${'$'}type,enabled,id"): IssueTimeTracker = client.get("/issues/$id/timeTracking") {
          fields?.let { parameter("fields", it) }
        }.body()
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
          ): List<BaseWorkItemRead.IssueWorkItem> = client.get("/issues/$id/timeTracking/workItems") {
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
            fields: String? = "${'$'}type,author(${'$'}type,id,login,ringId),created,creator(${'$'}type,id,login,ringId),date,duration(${'$'}type,id),id,text,updated",
            body: BaseWorkItemWrite.IssueWorkItem? = null,
          ): BaseWorkItemRead.IssueWorkItem = client.post("/issues/$id/timeTracking/workItems") {
            muteUpdateNotifications?.let { parameter("muteUpdateNotifications", it) }
            fields?.let { parameter("fields", it) }
            body?.let {
              contentType(ContentType.Application.Json)
              setBody(it)
            }
          }.body()
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
              client.delete("/issues/$id/timeTracking/workItems/$issueWorkItemId")
            }
          }

          public class Get internal constructor(
            private val client: HttpClient,
            private val id: String,
            private val issueWorkItemId: String,
          ) {
            public suspend operator fun invoke(fields: String? = "${'$'}type,author(${'$'}type,id,login,ringId),created,creator(${'$'}type,id,login,ringId),date,duration(${'$'}type,id),id,text,updated"): BaseWorkItemRead.IssueWorkItem = client.get("/issues/$id/timeTracking/workItems/$issueWorkItemId") {
              fields?.let { parameter("fields", it) }
            }.body()
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
            ): BaseWorkItemRead.IssueWorkItem = client.post("/issues/$id/timeTracking/workItems/$issueWorkItemId") {
              muteUpdateNotifications?.let { parameter("muteUpdateNotifications", it) }
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
        ): List<VcsChangeRead> = client.get("/issues/$id/vcsChanges") {
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
          fields: String? = "${'$'}type,id",
          body: VcsChangeWrite? = null,
        ): VcsChangeRead = client.post("/issues/$id/vcsChanges") {
          muteUpdateNotifications?.let { parameter("muteUpdateNotifications", it) }
          fields?.let { parameter("fields", it) }
          body?.let {
            contentType(ContentType.Application.Json)
            setBody(it)
          }
        }.body()
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
            client.delete("/issues/$id/vcsChanges/$vcsChangeId")
          }
        }

        public class Get internal constructor(
          private val client: HttpClient,
          private val id: String,
          private val vcsChangeId: String,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,id"): VcsChangeRead = client.get("/issues/$id/vcsChanges/$vcsChangeId") {
            fields?.let { parameter("fields", it) }
          }.body()
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
          ): VcsChangeRead = client.post("/issues/$id/vcsChanges/$vcsChangeId") {
            muteUpdateNotifications?.let { parameter("muteUpdateNotifications", it) }
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
