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
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public val post: Post = Post(client)

  public fun id(id: String): IdPath = IdPath(client, id)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(
      fields: String? = "${'$'}type,content,created,id,idReadable,parentArticle(${'$'}type,id,idReadable),project(${'$'}type,id,name,shortName),summary,updated,updatedBy(${'$'}type,id,login,ringId)",
      skip: Int? = null,
      top: Int? = null,
    ): List<BaseArticleRead.Article> = client.get("/articles") {
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
      fields: String? = "${'$'}type,content,created,id,idReadable,parentArticle(${'$'}type,id,idReadable),project(${'$'}type,id,name,shortName),summary,updated,updatedBy(${'$'}type,id,login,ringId)",
      body: BaseArticleWrite.Article? = null,
    ): BaseArticleRead.Article = client.post("/articles") {
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

    public val attachments: Attachments = Attachments(client, id)

    public val childArticles: ChildArticles = ChildArticles(client, id)

    public val comments: Comments = Comments(client, id)

    public val parentArticle: ParentArticle = ParentArticle(client, id)

    public val tags: Tags = Tags(client, id)

    public class Delete internal constructor(
      private val client: HttpClient,
      private val id: String,
    ) {
      public suspend operator fun invoke() {
        client.delete("/articles/$id")
      }
    }

    public class Get internal constructor(
      private val client: HttpClient,
      private val id: String,
    ) {
      public suspend operator fun invoke(fields: String? = "${'$'}type,content,created,id,idReadable,parentArticle(${'$'}type,id,idReadable),project(${'$'}type,id,name,shortName),summary,updated,updatedBy(${'$'}type,id,login,ringId)"): BaseArticleRead.Article = client.get("/articles/$id") {
        fields?.let { parameter("fields", it) }
      }.body()
    }

    public class Post internal constructor(
      private val client: HttpClient,
      private val id: String,
    ) {
      public suspend operator fun invoke(
        muteUpdateNotifications: Boolean? = null,
        fields: String? = "${'$'}type,content,created,id,idReadable,parentArticle(${'$'}type,id,idReadable),project(${'$'}type,id,name,shortName),summary,updated,updatedBy(${'$'}type,id,login,ringId)",
        body: BaseArticleWrite.Article? = null,
      ): BaseArticleRead.Article = client.post("/articles/$id") {
        muteUpdateNotifications?.let { parameter("muteUpdateNotifications", it) }
        fields?.let { parameter("fields", it) }
        body?.let {
          contentType(ContentType.Application.Json)
          setBody(it)
        }
      }.body()
    }

    public class Attachments internal constructor(
      private val client: HttpClient,
      private val id: String,
    ) {
      public val `get`: Get = Get(client, id)

      public val post: Post = Post(client, id)

      public fun articleAttachmentId(articleAttachmentId: String): ArticleAttachmentIdPath = ArticleAttachmentIdPath(client, id, articleAttachmentId)

      public class Get internal constructor(
        private val client: HttpClient,
        private val id: String,
      ) {
        public suspend operator fun invoke(
          fields: String? = "${'$'}type,author(${'$'}type,id,login,ringId),charset,created,extension,id,metaData,mimeType,name,size,updated,url",
          skip: Int? = null,
          top: Int? = null,
        ): List<ArticleAttachmentRead> = client.get("/articles/$id/attachments") {
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
          body: ArticleAttachmentWrite? = null,
        ): ArticleAttachmentRead = client.post("/articles/$id/attachments") {
          muteUpdateNotifications?.let { parameter("muteUpdateNotifications", it) }
          fields?.let { parameter("fields", it) }
          body?.let {
            contentType(ContentType.Application.Json)
            setBody(it)
          }
        }.body()
      }

      public class ArticleAttachmentIdPath internal constructor(
        private val client: HttpClient,
        private val id: String,
        private val articleAttachmentId: String,
      ) {
        public val delete: Delete = Delete(client, id, articleAttachmentId)

        public val `get`: Get = Get(client, id, articleAttachmentId)

        public val post: Post = Post(client, id, articleAttachmentId)

        public class Delete internal constructor(
          private val client: HttpClient,
          private val id: String,
          private val articleAttachmentId: String,
        ) {
          public suspend operator fun invoke() {
            client.delete("/articles/$id/attachments/$articleAttachmentId")
          }
        }

        public class Get internal constructor(
          private val client: HttpClient,
          private val id: String,
          private val articleAttachmentId: String,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,author(${'$'}type,id,login,ringId),charset,created,extension,id,metaData,mimeType,name,size,updated,url"): ArticleAttachmentRead = client.get("/articles/$id/attachments/$articleAttachmentId") {
            fields?.let { parameter("fields", it) }
          }.body()
        }

        public class Post internal constructor(
          private val client: HttpClient,
          private val id: String,
          private val articleAttachmentId: String,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,author(${'$'}type,id,login,ringId),charset,created,extension,id,metaData,mimeType,name,size,updated,url", body: ArticleAttachmentWrite? = null): ArticleAttachmentRead = client.post("/articles/$id/attachments/$articleAttachmentId") {
            fields?.let { parameter("fields", it) }
            body?.let {
              contentType(ContentType.Application.Json)
              setBody(it)
            }
          }.body()
        }
      }
    }

    public class ChildArticles internal constructor(
      private val client: HttpClient,
      private val id: String,
    ) {
      public val `get`: Get = Get(client, id)

      public val post: Post = Post(client, id)

      public fun articleId(articleId: String): ArticleIdPath = ArticleIdPath(client, id, articleId)

      public class Get internal constructor(
        private val client: HttpClient,
        private val id: String,
      ) {
        public suspend operator fun invoke(
          fields: String? = "${'$'}type,content,created,id,idReadable,parentArticle(${'$'}type,id,idReadable),project(${'$'}type,id,name,shortName),summary,updated,updatedBy(${'$'}type,id,login,ringId)",
          skip: Int? = null,
          top: Int? = null,
        ): List<BaseArticleRead.Article> = client.get("/articles/$id/childArticles") {
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
          fields: String? = "${'$'}type,content,created,id,idReadable,parentArticle(${'$'}type,id,idReadable),project(${'$'}type,id,name,shortName),summary,updated,updatedBy(${'$'}type,id,login,ringId)",
          body: BaseArticleWrite.Article? = null,
        ): BaseArticleRead.Article = client.post("/articles/$id/childArticles") {
          muteUpdateNotifications?.let { parameter("muteUpdateNotifications", it) }
          fields?.let { parameter("fields", it) }
          body?.let {
            contentType(ContentType.Application.Json)
            setBody(it)
          }
        }.body()
      }

      public class ArticleIdPath internal constructor(
        private val client: HttpClient,
        private val id: String,
        private val articleId: String,
      ) {
        public val delete: Delete = Delete(client, id, articleId)

        public val `get`: Get = Get(client, id, articleId)

        public val post: Post = Post(client, id, articleId)

        public class Delete internal constructor(
          private val client: HttpClient,
          private val id: String,
          private val articleId: String,
        ) {
          public suspend operator fun invoke() {
            client.delete("/articles/$id/childArticles/$articleId")
          }
        }

        public class Get internal constructor(
          private val client: HttpClient,
          private val id: String,
          private val articleId: String,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,content,created,id,idReadable,parentArticle(${'$'}type,id,idReadable),project(${'$'}type,id,name,shortName),summary,updated,updatedBy(${'$'}type,id,login,ringId)"): BaseArticleRead.Article = client.get("/articles/$id/childArticles/$articleId") {
            fields?.let { parameter("fields", it) }
          }.body()
        }

        public class Post internal constructor(
          private val client: HttpClient,
          private val id: String,
          private val articleId: String,
        ) {
          public suspend operator fun invoke(
            muteUpdateNotifications: Boolean? = null,
            fields: String? = "${'$'}type,content,created,id,idReadable,parentArticle(${'$'}type,id,idReadable),project(${'$'}type,id,name,shortName),summary,updated,updatedBy(${'$'}type,id,login,ringId)",
            body: BaseArticleWrite.Article? = null,
          ): BaseArticleRead.Article = client.post("/articles/$id/childArticles/$articleId") {
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

    public class Comments internal constructor(
      private val client: HttpClient,
      private val id: String,
    ) {
      public val `get`: Get = Get(client, id)

      public val post: Post = Post(client, id)

      public fun articleCommentId(articleCommentId: String): ArticleCommentIdPath = ArticleCommentIdPath(client, id, articleCommentId)

      public class Get internal constructor(
        private val client: HttpClient,
        private val id: String,
      ) {
        public suspend operator fun invoke(
          fields: String? = "${'$'}type,attachments(${'$'}type,id),author(${'$'}type,id,login,ringId),created,id,text,updated,visibility(${'$'}type,id,permittedGroups(${'$'}type,id,name,ringId),permittedUsers(${'$'}type,id,login,ringId))",
          skip: Int? = null,
          top: Int? = null,
        ): List<ArticleCommentRead> = client.get("/articles/$id/comments") {
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
          fields: String? = "${'$'}type,attachments(${'$'}type,id),author(${'$'}type,id,login,ringId),created,id,text,updated,visibility(${'$'}type,id,permittedGroups(${'$'}type,id,name,ringId),permittedUsers(${'$'}type,id,login,ringId))",
          body: ArticleCommentWrite? = null,
        ): ArticleCommentRead = client.post("/articles/$id/comments") {
          draftId?.let { parameter("draftId", it) }
          muteUpdateNotifications?.let { parameter("muteUpdateNotifications", it) }
          fields?.let { parameter("fields", it) }
          body?.let {
            contentType(ContentType.Application.Json)
            setBody(it)
          }
        }.body()
      }

      public class ArticleCommentIdPath internal constructor(
        private val client: HttpClient,
        private val id: String,
        private val articleCommentId: String,
      ) {
        public val delete: Delete = Delete(client, id, articleCommentId)

        public val `get`: Get = Get(client, id, articleCommentId)

        public val post: Post = Post(client, id, articleCommentId)

        public val reactions: Reactions = Reactions(client, id, articleCommentId)

        public class Delete internal constructor(
          private val client: HttpClient,
          private val id: String,
          private val articleCommentId: String,
        ) {
          public suspend operator fun invoke() {
            client.delete("/articles/$id/comments/$articleCommentId")
          }
        }

        public class Get internal constructor(
          private val client: HttpClient,
          private val id: String,
          private val articleCommentId: String,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,attachments(${'$'}type,id),author(${'$'}type,id,login,ringId),created,id,text,updated,visibility(${'$'}type,id,permittedGroups(${'$'}type,id,name,ringId),permittedUsers(${'$'}type,id,login,ringId))"): ArticleCommentRead = client.get("/articles/$id/comments/$articleCommentId") {
            fields?.let { parameter("fields", it) }
          }.body()
        }

        public class Post internal constructor(
          private val client: HttpClient,
          private val id: String,
          private val articleCommentId: String,
        ) {
          public suspend operator fun invoke(
            muteUpdateNotifications: Boolean? = null,
            fields: String? = "${'$'}type,attachments(${'$'}type,id),author(${'$'}type,id,login,ringId),created,id,text,updated,visibility(${'$'}type,id,permittedGroups(${'$'}type,id,name,ringId),permittedUsers(${'$'}type,id,login,ringId))",
            body: ArticleCommentWrite? = null,
          ): ArticleCommentRead = client.post("/articles/$id/comments/$articleCommentId") {
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
          private val articleCommentId: String,
        ) {
          public val `get`: Get = Get(client, id, articleCommentId)

          public val post: Post = Post(client, id, articleCommentId)

          public fun reactionId(reactionId: String): ReactionIdPath = ReactionIdPath(client, id, articleCommentId, reactionId)

          public class Get internal constructor(
            private val client: HttpClient,
            private val id: String,
            private val articleCommentId: String,
          ) {
            public suspend operator fun invoke(
              fields: String? = "${'$'}type,id",
              skip: Int? = null,
              top: Int? = null,
            ): List<ReactionRead> = client.get("/articles/$id/comments/$articleCommentId/reactions") {
              fields?.let { parameter("fields", it) }
              skip?.let { parameter("${'$'}skip", it) }
              top?.let { parameter("${'$'}top", it) }
            }.body()
          }

          public class Post internal constructor(
            private val client: HttpClient,
            private val id: String,
            private val articleCommentId: String,
          ) {
            public suspend operator fun invoke(fields: String? = "${'$'}type,id", body: ReactionWrite? = null): ReactionRead = client.post("/articles/$id/comments/$articleCommentId/reactions") {
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
            private val articleCommentId: String,
            private val reactionId: String,
          ) {
            public val delete: Delete = Delete(client, id, articleCommentId, reactionId)

            public val `get`: Get = Get(client, id, articleCommentId, reactionId)

            public class Delete internal constructor(
              private val client: HttpClient,
              private val id: String,
              private val articleCommentId: String,
              private val reactionId: String,
            ) {
              public suspend operator fun invoke() {
                client.delete("/articles/$id/comments/$articleCommentId/reactions/$reactionId")
              }
            }

            public class Get internal constructor(
              private val client: HttpClient,
              private val id: String,
              private val articleCommentId: String,
              private val reactionId: String,
            ) {
              public suspend operator fun invoke(fields: String? = "${'$'}type,id"): ReactionRead = client.get("/articles/$id/comments/$articleCommentId/reactions/$reactionId") {
                fields?.let { parameter("fields", it) }
              }.body()
            }
          }
        }
      }
    }

    public class ParentArticle internal constructor(
      private val client: HttpClient,
      private val id: String,
    ) {
      public val `get`: Get = Get(client, id)

      public class Get internal constructor(
        private val client: HttpClient,
        private val id: String,
      ) {
        public suspend operator fun invoke(fields: String? = "${'$'}type,content,created,id,idReadable,parentArticle(${'$'}type,id,idReadable),project(${'$'}type,id,name,shortName),summary,updated,updatedBy(${'$'}type,id,login,ringId)"): BaseArticleRead.Article = client.get("/articles/$id/parentArticle") {
          fields?.let { parameter("fields", it) }
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
        ): List<IssueFolderRead.Tag> = client.get("/articles/$id/tags") {
          fields?.let { parameter("fields", it) }
          skip?.let { parameter("${'$'}skip", it) }
          top?.let { parameter("${'$'}top", it) }
        }.body()
      }

      public class Post internal constructor(
        private val client: HttpClient,
        private val id: String,
      ) {
        public suspend operator fun invoke(fields: String? = "${'$'}type,id,name,owner(${'$'}type,id,login,ringId)", body: IssueFolderWrite.Tag? = null): IssueFolderRead.Tag = client.post("/articles/$id/tags") {
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
            client.delete("/articles/$id/tags/$tagId")
          }
        }

        public class Get internal constructor(
          private val client: HttpClient,
          private val id: String,
          private val tagId: String,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,id,name,owner(${'$'}type,id,login,ringId)"): IssueFolderRead.Tag = client.get("/articles/$id/tags/$tagId") {
            fields?.let { parameter("fields", it) }
          }.body()
        }
      }
    }
  }
}
