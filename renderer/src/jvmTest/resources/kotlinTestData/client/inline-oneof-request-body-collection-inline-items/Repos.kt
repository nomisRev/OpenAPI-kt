package io.github.nomisrev.render.test.client.`inline`.oneof.request.body.collection.`inline`.items

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlin.jvm.JvmName
import kotlinx.serialization.Serializable

public class Repos internal constructor(
  private val client: HttpClient,
) {
  public fun owner(owner: String): OwnerPath = OwnerPath(client, owner)

  public class OwnerPath internal constructor(
    private val client: HttpClient,
    private val owner: String,
  ) {
    public fun repo(repo: String): RepoPath = RepoPath(client, owner, repo)

    public class RepoPath internal constructor(
      private val client: HttpClient,
      private val owner: String,
      private val repo: String,
    ) {
      public val issues: Issues = Issues(client, owner, repo)

      public class Issues internal constructor(
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public fun issueNumber(issueNumber: Long): IssueNumberPath = IssueNumberPath(client, owner, repo, issueNumber)

        public class IssueNumberPath internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
          private val issueNumber: Long,
        ) {
          public val labels: Labels = Labels(client, owner, repo, issueNumber)

          public class Labels internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val issueNumber: Long,
          ) {
            public val put: Put = Put(client, owner, repo, issueNumber)

            public class Put internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val issueNumber: Long,
            ) {
              public suspend operator fun invoke(): List<Name> = client.put("/repos/$owner/$repo/issues/$issueNumber/labels").body()

              public suspend operator fun invoke(body: LabelsStrings): List<Name> = client.put("/repos/$owner/$repo/issues/$issueNumber/labels") {
                contentType(ContentType.Application.Json)
                setBody(body)
              }.body()

              @JvmName("StringList")
              public suspend operator fun invoke(body: List<String>): List<Name> = client.put("/repos/$owner/$repo/issues/$issueNumber/labels") {
                contentType(ContentType.Application.Json)
                setBody(body)
              }.body()

              public suspend operator fun invoke(body: LabelsNames): List<Name> = client.put("/repos/$owner/$repo/issues/$issueNumber/labels") {
                contentType(ContentType.Application.Json)
                setBody(body)
              }.body()

              @JvmName("NameList")
              public suspend operator fun invoke(body: List<Name>): List<Name> = client.put("/repos/$owner/$repo/issues/$issueNumber/labels") {
                contentType(ContentType.Application.Json)
                setBody(body)
              }.body()

              public suspend operator fun invoke(body: String): List<Name> = client.put("/repos/$owner/$repo/issues/$issueNumber/labels") {
                contentType(ContentType.Application.Json)
                setBody(body)
              }.body()

              @JvmInline
              @Serializable
              public value class LabelsStrings(
                public val labels: List<String>? = null,
              )

              @JvmInline
              @Serializable
              public value class LabelsNames(
                public val labels: List<Name>? = null,
              )

              @JvmInline
              @Serializable
              public value class Name(
                public val name: String,
              )
            }
          }
        }
      }
    }
  }
}
