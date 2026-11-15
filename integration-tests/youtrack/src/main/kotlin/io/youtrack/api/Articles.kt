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
import io.youtrack.model.ArticleAttachmentRead
import io.youtrack.model.ArticleAttachmentWrite
import io.youtrack.model.ArticleCommentRead
import io.youtrack.model.ArticleCommentWrite
import io.youtrack.model.BaseArticleRead
import io.youtrack.model.BaseArticleWrite
import io.youtrack.model.IssueFolderRead
import io.youtrack.model.IssueFolderWrite
import io.youtrack.model.ReactionRead
import io.youtrack.model.ReactionWrite
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.collections.List

public class Articles internal constructor(
  private val __client: HttpClient,
) {
  public val `get`: Get = Get(__client)

  public val post: Post = Post(__client)

  public fun id(id: String): IdPath = IdPath(__client, id)

  public class Get internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke(
      fields: String? = "${'$'}type,content,created,id,idReadable,parentArticle(${'$'}type,id,idReadable),project(${'$'}type,id,name,shortName),summary,updated,updatedBy(${'$'}type,id,login,ringId)",
      skip: Int? = null,
      top: Int? = null,
    ): List<BaseArticleRead.Article> {
      val response = __client.get("articles") {
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
      fields: String? = "${'$'}type,content,created,id,idReadable,parentArticle(${'$'}type,id,idReadable),project(${'$'}type,id,name,shortName),summary,updated,updatedBy(${'$'}type,id,login,ringId)",
      body: BaseArticleWrite.Article? = null,
    ): BaseArticleRead.Article {
      val response = __client.post("articles") {
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

    public val attachments: Attachments = Attachments(__client, _id)

    public val childArticles: ChildArticles = ChildArticles(__client, _id)

    public val comments: Comments = Comments(__client, _id)

    public val parentArticle: ParentArticle = ParentArticle(__client, _id)

    public val tags: Tags = Tags(__client, _id)

    public class Delete internal constructor(
      private val __client: HttpClient,
      private val _id: String,
    ) {
      public suspend operator fun invoke() {
        val response = __client.delete("articles/${this._id.encodeURLParameter()}")
        if (response.status.value != 200) throw ResponseException(response, "")
      }
    }

    public class Get internal constructor(
      private val __client: HttpClient,
      private val _id: String,
    ) {
      public suspend operator fun invoke(fields: String? = "${'$'}type,content,created,id,idReadable,parentArticle(${'$'}type,id,idReadable),project(${'$'}type,id,name,shortName),summary,updated,updatedBy(${'$'}type,id,login,ringId)"): BaseArticleRead.Article {
        val response = __client.get("articles/${this._id.encodeURLParameter()}") {
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
        fields: String? = "${'$'}type,content,created,id,idReadable,parentArticle(${'$'}type,id,idReadable),project(${'$'}type,id,name,shortName),summary,updated,updatedBy(${'$'}type,id,login,ringId)",
        body: BaseArticleWrite.Article? = null,
      ): BaseArticleRead.Article {
        val response = __client.post("articles/${this._id.encodeURLParameter()}") {
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

    public class Attachments internal constructor(
      private val __client: HttpClient,
      private val _id: String,
    ) {
      public val `get`: Get = Get(__client, _id)

      public val post: Post = Post(__client, _id)

      public fun articleAttachmentId(articleAttachmentId: String): ArticleAttachmentIdPath = ArticleAttachmentIdPath(__client, _id, articleAttachmentId)

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _id: String,
      ) {
        public suspend operator fun invoke(
          fields: String? = "${'$'}type,author(${'$'}type,id,login,ringId),charset,created,extension,id,metaData,mimeType,name,size,updated,url",
          skip: Int? = null,
          top: Int? = null,
        ): List<ArticleAttachmentRead> {
          val response = __client.get("articles/${this._id.encodeURLParameter()}/attachments") {
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
          fields: String? = "${'$'}type,author(${'$'}type,id,login,ringId),charset,created,extension,id,metaData,mimeType,name,size,updated,url",
          body: ArticleAttachmentWrite? = null,
        ): ArticleAttachmentRead {
          val response = __client.post("articles/${this._id.encodeURLParameter()}/attachments") {
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

      public class ArticleAttachmentIdPath internal constructor(
        private val __client: HttpClient,
        private val _id: String,
        private val _articleAttachmentId: String,
      ) {
        public val delete: Delete = Delete(__client, _id, _articleAttachmentId)

        public val `get`: Get = Get(__client, _id, _articleAttachmentId)

        public val post: Post = Post(__client, _id, _articleAttachmentId)

        public class Delete internal constructor(
          private val __client: HttpClient,
          private val _id: String,
          private val _articleAttachmentId: String,
        ) {
          public suspend operator fun invoke() {
            val response = __client.delete("articles/${this._id.encodeURLParameter()}/attachments/${this._articleAttachmentId.encodeURLParameter()}")
            if (response.status.value != 200) throw ResponseException(response, "")
          }
        }

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _id: String,
          private val _articleAttachmentId: String,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,author(${'$'}type,id,login,ringId),charset,created,extension,id,metaData,mimeType,name,size,updated,url"): ArticleAttachmentRead {
            val response = __client.get("articles/${this._id.encodeURLParameter()}/attachments/${this._articleAttachmentId.encodeURLParameter()}") {
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
          private val _articleAttachmentId: String,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,author(${'$'}type,id,login,ringId),charset,created,extension,id,metaData,mimeType,name,size,updated,url", body: ArticleAttachmentWrite? = null): ArticleAttachmentRead {
            val response = __client.post("articles/${this._id.encodeURLParameter()}/attachments/${this._articleAttachmentId.encodeURLParameter()}") {
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

    public class ChildArticles internal constructor(
      private val __client: HttpClient,
      private val _id: String,
    ) {
      public val `get`: Get = Get(__client, _id)

      public val post: Post = Post(__client, _id)

      public fun articleId(articleId: String): ArticleIdPath = ArticleIdPath(__client, _id, articleId)

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _id: String,
      ) {
        public suspend operator fun invoke(
          fields: String? = "${'$'}type,content,created,id,idReadable,parentArticle(${'$'}type,id,idReadable),project(${'$'}type,id,name,shortName),summary,updated,updatedBy(${'$'}type,id,login,ringId)",
          skip: Int? = null,
          top: Int? = null,
        ): List<BaseArticleRead.Article> {
          val response = __client.get("articles/${this._id.encodeURLParameter()}/childArticles") {
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
          fields: String? = "${'$'}type,content,created,id,idReadable,parentArticle(${'$'}type,id,idReadable),project(${'$'}type,id,name,shortName),summary,updated,updatedBy(${'$'}type,id,login,ringId)",
          body: BaseArticleWrite.Article? = null,
        ): BaseArticleRead.Article {
          val response = __client.post("articles/${this._id.encodeURLParameter()}/childArticles") {
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

      public class ArticleIdPath internal constructor(
        private val __client: HttpClient,
        private val _id: String,
        private val _articleId: String,
      ) {
        public val delete: Delete = Delete(__client, _id, _articleId)

        public val `get`: Get = Get(__client, _id, _articleId)

        public val post: Post = Post(__client, _id, _articleId)

        public class Delete internal constructor(
          private val __client: HttpClient,
          private val _id: String,
          private val _articleId: String,
        ) {
          public suspend operator fun invoke() {
            val response = __client.delete("articles/${this._id.encodeURLParameter()}/childArticles/${this._articleId.encodeURLParameter()}")
            if (response.status.value != 200) throw ResponseException(response, "")
          }
        }

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _id: String,
          private val _articleId: String,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,content,created,id,idReadable,parentArticle(${'$'}type,id,idReadable),project(${'$'}type,id,name,shortName),summary,updated,updatedBy(${'$'}type,id,login,ringId)"): BaseArticleRead.Article {
            val response = __client.get("articles/${this._id.encodeURLParameter()}/childArticles/${this._articleId.encodeURLParameter()}") {
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
          private val _articleId: String,
        ) {
          public suspend operator fun invoke(
            muteUpdateNotifications: Boolean? = null,
            fields: String? = "${'$'}type,content,created,id,idReadable,parentArticle(${'$'}type,id,idReadable),project(${'$'}type,id,name,shortName),summary,updated,updatedBy(${'$'}type,id,login,ringId)",
            body: BaseArticleWrite.Article? = null,
          ): BaseArticleRead.Article {
            val response = __client.post("articles/${this._id.encodeURLParameter()}/childArticles/${this._articleId.encodeURLParameter()}") {
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

    public class Comments internal constructor(
      private val __client: HttpClient,
      private val _id: String,
    ) {
      public val `get`: Get = Get(__client, _id)

      public val post: Post = Post(__client, _id)

      public fun articleCommentId(articleCommentId: String): ArticleCommentIdPath = ArticleCommentIdPath(__client, _id, articleCommentId)

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _id: String,
      ) {
        public suspend operator fun invoke(
          fields: String? = "${'$'}type,attachments(${'$'}type,id),author(${'$'}type,id,login,ringId),created,id,text,updated,visibility(${'$'}type,id,permittedGroups(${'$'}type,id,name,ringId),permittedUsers(${'$'}type,id,login,ringId))",
          skip: Int? = null,
          top: Int? = null,
        ): List<ArticleCommentRead> {
          val response = __client.get("articles/${this._id.encodeURLParameter()}/comments") {
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
          fields: String? = "${'$'}type,attachments(${'$'}type,id),author(${'$'}type,id,login,ringId),created,id,text,updated,visibility(${'$'}type,id,permittedGroups(${'$'}type,id,name,ringId),permittedUsers(${'$'}type,id,login,ringId))",
          body: ArticleCommentWrite? = null,
        ): ArticleCommentRead {
          val response = __client.post("articles/${this._id.encodeURLParameter()}/comments") {
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

      public class ArticleCommentIdPath internal constructor(
        private val __client: HttpClient,
        private val _id: String,
        private val _articleCommentId: String,
      ) {
        public val delete: Delete = Delete(__client, _id, _articleCommentId)

        public val `get`: Get = Get(__client, _id, _articleCommentId)

        public val post: Post = Post(__client, _id, _articleCommentId)

        public val reactions: Reactions = Reactions(__client, _id, _articleCommentId)

        public class Delete internal constructor(
          private val __client: HttpClient,
          private val _id: String,
          private val _articleCommentId: String,
        ) {
          public suspend operator fun invoke() {
            val response = __client.delete("articles/${this._id.encodeURLParameter()}/comments/${this._articleCommentId.encodeURLParameter()}")
            if (response.status.value != 200) throw ResponseException(response, "")
          }
        }

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _id: String,
          private val _articleCommentId: String,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,attachments(${'$'}type,id),author(${'$'}type,id,login,ringId),created,id,text,updated,visibility(${'$'}type,id,permittedGroups(${'$'}type,id,name,ringId),permittedUsers(${'$'}type,id,login,ringId))"): ArticleCommentRead {
            val response = __client.get("articles/${this._id.encodeURLParameter()}/comments/${this._articleCommentId.encodeURLParameter()}") {
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
          private val _articleCommentId: String,
        ) {
          public suspend operator fun invoke(
            muteUpdateNotifications: Boolean? = null,
            fields: String? = "${'$'}type,attachments(${'$'}type,id),author(${'$'}type,id,login,ringId),created,id,text,updated,visibility(${'$'}type,id,permittedGroups(${'$'}type,id,name,ringId),permittedUsers(${'$'}type,id,login,ringId))",
            body: ArticleCommentWrite? = null,
          ): ArticleCommentRead {
            val response = __client.post("articles/${this._id.encodeURLParameter()}/comments/${this._articleCommentId.encodeURLParameter()}") {
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
          private val _articleCommentId: String,
        ) {
          public val `get`: Get = Get(__client, _id, _articleCommentId)

          public val post: Post = Post(__client, _id, _articleCommentId)

          public fun reactionId(reactionId: String): ReactionIdPath = ReactionIdPath(__client, _id, _articleCommentId, reactionId)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _id: String,
            private val _articleCommentId: String,
          ) {
            public suspend operator fun invoke(
              fields: String? = "${'$'}type,id",
              skip: Int? = null,
              top: Int? = null,
            ): List<ReactionRead> {
              val response = __client.get("articles/${this._id.encodeURLParameter()}/comments/${this._articleCommentId.encodeURLParameter()}/reactions") {
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
            private val _articleCommentId: String,
          ) {
            public suspend operator fun invoke(fields: String? = "${'$'}type,id", body: ReactionWrite? = null): ReactionRead {
              val response = __client.post("articles/${this._id.encodeURLParameter()}/comments/${this._articleCommentId.encodeURLParameter()}/reactions") {
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
            private val _articleCommentId: String,
            private val _reactionId: String,
          ) {
            public val delete: Delete = Delete(__client, _id, _articleCommentId, _reactionId)

            public val `get`: Get = Get(__client, _id, _articleCommentId, _reactionId)

            public class Delete internal constructor(
              private val __client: HttpClient,
              private val _id: String,
              private val _articleCommentId: String,
              private val _reactionId: String,
            ) {
              public suspend operator fun invoke() {
                val response = __client.delete("articles/${this._id.encodeURLParameter()}/comments/${this._articleCommentId.encodeURLParameter()}/reactions/${this._reactionId.encodeURLParameter()}")
                if (response.status.value != 200) throw ResponseException(response, "")
              }
            }

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _id: String,
              private val _articleCommentId: String,
              private val _reactionId: String,
            ) {
              public suspend operator fun invoke(fields: String? = "${'$'}type,id"): ReactionRead {
                val response = __client.get("articles/${this._id.encodeURLParameter()}/comments/${this._articleCommentId.encodeURLParameter()}/reactions/${this._reactionId.encodeURLParameter()}") {
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

    public class ParentArticle internal constructor(
      private val __client: HttpClient,
      private val _id: String,
    ) {
      public val `get`: Get = Get(__client, _id)

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _id: String,
      ) {
        public suspend operator fun invoke(fields: String? = "${'$'}type,content,created,id,idReadable,parentArticle(${'$'}type,id,idReadable),project(${'$'}type,id,name,shortName),summary,updated,updatedBy(${'$'}type,id,login,ringId)"): BaseArticleRead.Article {
          val response = __client.get("articles/${this._id.encodeURLParameter()}/parentArticle") {
            if (fields != null) {
              this.url.encodedParameters.append("fields", fields.encodeURLParameter())
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
          val response = __client.get("articles/${this._id.encodeURLParameter()}/tags") {
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
          val response = __client.post("articles/${this._id.encodeURLParameter()}/tags") {
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
            val response = __client.delete("articles/${this._id.encodeURLParameter()}/tags/${this._tagId.encodeURLParameter()}")
            if (response.status.value != 200) throw ResponseException(response, "")
          }
        }

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _id: String,
          private val _tagId: String,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,id,name,owner(${'$'}type,id,login,ringId)"): IssueFolderRead.Tag {
            val response = __client.get("articles/${this._id.encodeURLParameter()}/tags/${this._tagId.encodeURLParameter()}") {
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
