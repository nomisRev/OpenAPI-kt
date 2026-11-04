package io.github.nomisrev.render.test.client.`inline`.oneof.request.body.collection.`inline`.items

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.encodeURLParameter
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmName
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonPrimitive

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
              public suspend operator fun invoke(): List<Name> {
                val response = client.put("repos/${owner.encodeURLParameter()}/${repo.encodeURLParameter()}/issues/$issueNumber/labels")
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }

              public suspend operator fun invoke(body: LabelsStrings): List<Name> {
                val response = client.put("repos/${owner.encodeURLParameter()}/${repo.encodeURLParameter()}/issues/$issueNumber/labels") {
                  contentType(ContentType.Application.Json)
                  setBody(body)
                }
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }

              @JvmName("StringList")
              public suspend operator fun invoke(body: List<String>): List<Name> {
                val response = client.put("repos/${owner.encodeURLParameter()}/${repo.encodeURLParameter()}/issues/$issueNumber/labels") {
                  contentType(ContentType.Application.Json)
                  setBody(body)
                }
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }

              public suspend operator fun invoke(body: LabelsNames): List<Name> {
                val response = client.put("repos/${owner.encodeURLParameter()}/${repo.encodeURLParameter()}/issues/$issueNumber/labels") {
                  contentType(ContentType.Application.Json)
                  setBody(body)
                }
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }

              @JvmName("NameList")
              public suspend operator fun invoke(body: List<Name>): List<Name> {
                val response = client.put("repos/${owner.encodeURLParameter()}/${repo.encodeURLParameter()}/issues/$issueNumber/labels") {
                  contentType(ContentType.Application.Json)
                  setBody(body)
                }
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }

              public suspend operator fun invoke(body: String): List<Name> {
                val response = client.put("repos/${owner.encodeURLParameter()}/${repo.encodeURLParameter()}/issues/$issueNumber/labels") {
                  contentType(ContentType.Application.Json)
                  setBody(JsonPrimitive(body))
                }
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }

              @OptIn(ExperimentalSerializationApi::class)
              @Serializable
              public data class LabelsStrings(
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val labels: List<String>? = null,
              )

              @OptIn(ExperimentalSerializationApi::class)
              @Serializable
              public data class LabelsNames(
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val labels: List<Name>? = null,
              )

              @Serializable
              public data class Name(
                public val name: String,
              )
            }
          }
        }
      }
    }
  }
}
