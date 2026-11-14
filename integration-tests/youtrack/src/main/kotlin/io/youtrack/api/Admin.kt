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
import io.youtrack.model.AppearanceSettingsRead
import io.youtrack.model.AppearanceSettingsWrite
import io.youtrack.model.BackupFile
import io.youtrack.model.BaseArticleRead
import io.youtrack.model.BundleElementRead
import io.youtrack.model.BundleElementWrite
import io.youtrack.model.BundleRead
import io.youtrack.model.BundleWrite
import io.youtrack.model.CustomFieldDefaultsRead
import io.youtrack.model.CustomFieldDefaultsWrite
import io.youtrack.model.CustomFieldRead
import io.youtrack.model.CustomFieldWrite
import io.youtrack.model.DatabaseBackupSettingsRead
import io.youtrack.model.DatabaseBackupSettingsWrite
import io.youtrack.model.FieldTypeRead
import io.youtrack.model.GlobalSettingsRead
import io.youtrack.model.GlobalSettingsWrite
import io.youtrack.model.GlobalTimeTrackingSettings
import io.youtrack.model.IssueFolderRead
import io.youtrack.model.IssueFolderWrite
import io.youtrack.model.IssueRead
import io.youtrack.model.IssueWrite
import io.youtrack.model.LicenseRead
import io.youtrack.model.LicenseWrite
import io.youtrack.model.LocaleSettingsRead
import io.youtrack.model.LocaleSettingsWrite
import io.youtrack.model.NotificationSettingsRead
import io.youtrack.model.NotificationSettingsWrite
import io.youtrack.model.ProjectCustomFieldRead
import io.youtrack.model.ProjectCustomFieldWrite
import io.youtrack.model.ProjectTimeTrackingSettingsRead
import io.youtrack.model.ProjectTimeTrackingSettingsWrite
import io.youtrack.model.RestCorsSettingsRead
import io.youtrack.model.RestCorsSettingsWrite
import io.youtrack.model.SystemSettingsRead
import io.youtrack.model.SystemSettingsWrite
import io.youtrack.model.UserGroupRead
import io.youtrack.model.UserGroupWrite
import io.youtrack.model.UserRead
import io.youtrack.model.UserWrite
import io.youtrack.model.WorkItemTypeRead
import io.youtrack.model.WorkItemTypeWrite
import io.youtrack.model.WorkTimeSettingsRead
import io.youtrack.model.WorkTimeSettingsWrite
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.collections.List

public class Admin internal constructor(
  private val client: HttpClient,
) {
  public val customFieldSettings: CustomFieldSettings = CustomFieldSettings(client)

  public val databaseBackup: DatabaseBackup = DatabaseBackup(client)

  public val globalSettings: GlobalSettings = GlobalSettings(client)

  public val projects: Projects = Projects(client)

  public val telemetry: Telemetry = Telemetry(client)

  public val timeTrackingSettings: TimeTrackingSettings = TimeTrackingSettings(client)

  public class CustomFieldSettings internal constructor(
    private val client: HttpClient,
  ) {
    public val bundles: Bundles = Bundles(client)

    public val customFields: CustomFields = CustomFields(client)

    public val types: Types = Types(client)

    public class Bundles internal constructor(
      private val client: HttpClient,
    ) {
      public val build: Build = Build(client)

      public val `enum`: Enum = Enum(client)

      public val ownedField: OwnedField = OwnedField(client)

      public val state: State = State(client)

      public val user: User = User(client)

      public val version: Version = Version(client)

      public class Build internal constructor(
        private val client: HttpClient,
      ) {
        public val `get`: Get = Get(client)

        public val post: Post = Post(client)

        public fun id(id: String): IdPath = IdPath(client, id)

        public class Get internal constructor(
          private val client: HttpClient,
        ) {
          public suspend operator fun invoke(
            fields: String? = "${'$'}type,id",
            skip: Int? = null,
            top: Int? = null,
          ): List<BundleRead.BuildBundle> {
            val response = client.get("admin/customFieldSettings/bundles/build") {
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

        public class Post internal constructor(
          private val client: HttpClient,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,id", body: BundleWrite.BuildBundle? = null): BundleRead.BuildBundle {
            val response = client.post("admin/customFieldSettings/bundles/build") {
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

        public class IdPath internal constructor(
          private val client: HttpClient,
          private val id: String,
        ) {
          public val delete: Delete = Delete(client, id)

          public val `get`: Get = Get(client, id)

          public val post: Post = Post(client, id)

          public val values: Values = Values(client, id)

          public class Delete internal constructor(
            private val client: HttpClient,
            private val id: String,
          ) {
            public suspend operator fun invoke() {
              val response = client.delete("admin/customFieldSettings/bundles/build/${id.encodeURLParameter()}")
              if (response.status.value != 200) throw ResponseException(response, "")
            }
          }

          public class Get internal constructor(
            private val client: HttpClient,
            private val id: String,
          ) {
            public suspend operator fun invoke(fields: String? = "${'$'}type,id"): BundleRead.BuildBundle {
              val response = client.get("admin/customFieldSettings/bundles/build/${id.encodeURLParameter()}") {
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
            public suspend operator fun invoke(fields: String? = "${'$'}type,id", body: BundleWrite.BuildBundle? = null): BundleRead.BuildBundle {
              val response = client.post("admin/customFieldSettings/bundles/build/${id.encodeURLParameter()}") {
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

          public class Values internal constructor(
            private val client: HttpClient,
            private val id: String,
          ) {
            public val `get`: Get = Get(client, id)

            public val post: Post = Post(client, id)

            public fun buildBundleElementId(buildBundleElementId: String): BuildBundleElementIdPath = BuildBundleElementIdPath(client, id, buildBundleElementId)

            public class Get internal constructor(
              private val client: HttpClient,
              private val id: String,
            ) {
              public suspend operator fun invoke(
                fields: String? = "${'$'}type,assembleDate,color(${'$'}type,background,foreground,id),id,name,ordinal",
                skip: Int? = null,
                top: Int? = null,
              ): List<BundleElementRead.BuildBundleElement> {
                val response = client.get("admin/customFieldSettings/bundles/build/${id.encodeURLParameter()}/values") {
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

            public class Post internal constructor(
              private val client: HttpClient,
              private val id: String,
            ) {
              public suspend operator fun invoke(fields: String? = "${'$'}type,assembleDate,color(${'$'}type,background,foreground,id),id,name,ordinal", body: BundleElementWrite.BuildBundleElement? = null): BundleElementRead.BuildBundleElement {
                val response = client.post("admin/customFieldSettings/bundles/build/${id.encodeURLParameter()}/values") {
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

            public class BuildBundleElementIdPath internal constructor(
              private val client: HttpClient,
              private val id: String,
              private val buildBundleElementId: String,
            ) {
              public val delete: Delete = Delete(client, id, buildBundleElementId)

              public val `get`: Get = Get(client, id, buildBundleElementId)

              public val post: Post = Post(client, id, buildBundleElementId)

              public class Delete internal constructor(
                private val client: HttpClient,
                private val id: String,
                private val buildBundleElementId: String,
              ) {
                public suspend operator fun invoke() {
                  val response = client.delete("admin/customFieldSettings/bundles/build/${id.encodeURLParameter()}/values/${buildBundleElementId.encodeURLParameter()}")
                  if (response.status.value != 200) throw ResponseException(response, "")
                }
              }

              public class Get internal constructor(
                private val client: HttpClient,
                private val id: String,
                private val buildBundleElementId: String,
              ) {
                public suspend operator fun invoke(fields: String? = "${'$'}type,assembleDate,color(${'$'}type,background,foreground,id),id,name,ordinal"): BundleElementRead.BuildBundleElement {
                  val response = client.get("admin/customFieldSettings/bundles/build/${id.encodeURLParameter()}/values/${buildBundleElementId.encodeURLParameter()}") {
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
                private val buildBundleElementId: String,
              ) {
                public suspend operator fun invoke(fields: String? = "${'$'}type,assembleDate,color(${'$'}type,background,foreground,id),id,name,ordinal", body: BundleElementWrite.BuildBundleElement? = null): BundleElementRead.BuildBundleElement {
                  val response = client.post("admin/customFieldSettings/bundles/build/${id.encodeURLParameter()}/values/${buildBundleElementId.encodeURLParameter()}") {
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
        }
      }

      public class Enum internal constructor(
        private val client: HttpClient,
      ) {
        public val `get`: Get = Get(client)

        public val post: Post = Post(client)

        public fun id(id: String): IdPath = IdPath(client, id)

        public class Get internal constructor(
          private val client: HttpClient,
        ) {
          public suspend operator fun invoke(
            fields: String? = "${'$'}type,id",
            skip: Int? = null,
            top: Int? = null,
          ): List<BundleRead.EnumBundle> {
            val response = client.get("admin/customFieldSettings/bundles/enum") {
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

        public class Post internal constructor(
          private val client: HttpClient,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,id", body: BundleWrite.EnumBundle? = null): BundleRead.EnumBundle {
            val response = client.post("admin/customFieldSettings/bundles/enum") {
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

        public class IdPath internal constructor(
          private val client: HttpClient,
          private val id: String,
        ) {
          public val delete: Delete = Delete(client, id)

          public val `get`: Get = Get(client, id)

          public val post: Post = Post(client, id)

          public val values: Values = Values(client, id)

          public class Delete internal constructor(
            private val client: HttpClient,
            private val id: String,
          ) {
            public suspend operator fun invoke() {
              val response = client.delete("admin/customFieldSettings/bundles/enum/${id.encodeURLParameter()}")
              if (response.status.value != 200) throw ResponseException(response, "")
            }
          }

          public class Get internal constructor(
            private val client: HttpClient,
            private val id: String,
          ) {
            public suspend operator fun invoke(fields: String? = "${'$'}type,id"): BundleRead.EnumBundle {
              val response = client.get("admin/customFieldSettings/bundles/enum/${id.encodeURLParameter()}") {
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
            public suspend operator fun invoke(fields: String? = "${'$'}type,id", body: BundleWrite.EnumBundle? = null): BundleRead.EnumBundle {
              val response = client.post("admin/customFieldSettings/bundles/enum/${id.encodeURLParameter()}") {
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

          public class Values internal constructor(
            private val client: HttpClient,
            private val id: String,
          ) {
            public val `get`: Get = Get(client, id)

            public val post: Post = Post(client, id)

            public fun enumBundleElementId(enumBundleElementId: String): EnumBundleElementIdPath = EnumBundleElementIdPath(client, id, enumBundleElementId)

            public class Get internal constructor(
              private val client: HttpClient,
              private val id: String,
            ) {
              public suspend operator fun invoke(
                fields: String? = "${'$'}type,color(${'$'}type,background,foreground,id),id,localizedName,name,ordinal",
                skip: Int? = null,
                top: Int? = null,
              ): List<BundleElementRead.EnumBundleElement> {
                val response = client.get("admin/customFieldSettings/bundles/enum/${id.encodeURLParameter()}/values") {
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

            public class Post internal constructor(
              private val client: HttpClient,
              private val id: String,
            ) {
              public suspend operator fun invoke(fields: String? = "${'$'}type,color(${'$'}type,background,foreground,id),id,localizedName,name,ordinal", body: BundleElementWrite.EnumBundleElement? = null): BundleElementRead.EnumBundleElement {
                val response = client.post("admin/customFieldSettings/bundles/enum/${id.encodeURLParameter()}/values") {
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

            public class EnumBundleElementIdPath internal constructor(
              private val client: HttpClient,
              private val id: String,
              private val enumBundleElementId: String,
            ) {
              public val delete: Delete = Delete(client, id, enumBundleElementId)

              public val `get`: Get = Get(client, id, enumBundleElementId)

              public val post: Post = Post(client, id, enumBundleElementId)

              public class Delete internal constructor(
                private val client: HttpClient,
                private val id: String,
                private val enumBundleElementId: String,
              ) {
                public suspend operator fun invoke() {
                  val response = client.delete("admin/customFieldSettings/bundles/enum/${id.encodeURLParameter()}/values/${enumBundleElementId.encodeURLParameter()}")
                  if (response.status.value != 200) throw ResponseException(response, "")
                }
              }

              public class Get internal constructor(
                private val client: HttpClient,
                private val id: String,
                private val enumBundleElementId: String,
              ) {
                public suspend operator fun invoke(fields: String? = "${'$'}type,color(${'$'}type,background,foreground,id),id,localizedName,name,ordinal"): BundleElementRead.EnumBundleElement {
                  val response = client.get("admin/customFieldSettings/bundles/enum/${id.encodeURLParameter()}/values/${enumBundleElementId.encodeURLParameter()}") {
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
                private val enumBundleElementId: String,
              ) {
                public suspend operator fun invoke(fields: String? = "${'$'}type,color(${'$'}type,background,foreground,id),id,localizedName,name,ordinal", body: BundleElementWrite.EnumBundleElement? = null): BundleElementRead.EnumBundleElement {
                  val response = client.post("admin/customFieldSettings/bundles/enum/${id.encodeURLParameter()}/values/${enumBundleElementId.encodeURLParameter()}") {
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
        }
      }

      public class OwnedField internal constructor(
        private val client: HttpClient,
      ) {
        public val `get`: Get = Get(client)

        public val post: Post = Post(client)

        public fun id(id: String): IdPath = IdPath(client, id)

        public class Get internal constructor(
          private val client: HttpClient,
        ) {
          public suspend operator fun invoke(
            fields: String? = "${'$'}type,id",
            skip: Int? = null,
            top: Int? = null,
          ): List<BundleRead.OwnedBundle> {
            val response = client.get("admin/customFieldSettings/bundles/ownedField") {
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

        public class Post internal constructor(
          private val client: HttpClient,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,id", body: BundleWrite.OwnedBundle? = null): BundleRead.OwnedBundle {
            val response = client.post("admin/customFieldSettings/bundles/ownedField") {
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

        public class IdPath internal constructor(
          private val client: HttpClient,
          private val id: String,
        ) {
          public val delete: Delete = Delete(client, id)

          public val `get`: Get = Get(client, id)

          public val post: Post = Post(client, id)

          public val values: Values = Values(client, id)

          public class Delete internal constructor(
            private val client: HttpClient,
            private val id: String,
          ) {
            public suspend operator fun invoke() {
              val response = client.delete("admin/customFieldSettings/bundles/ownedField/${id.encodeURLParameter()}")
              if (response.status.value != 200) throw ResponseException(response, "")
            }
          }

          public class Get internal constructor(
            private val client: HttpClient,
            private val id: String,
          ) {
            public suspend operator fun invoke(fields: String? = "${'$'}type,id"): BundleRead.OwnedBundle {
              val response = client.get("admin/customFieldSettings/bundles/ownedField/${id.encodeURLParameter()}") {
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
            public suspend operator fun invoke(fields: String? = "${'$'}type,id", body: BundleWrite.OwnedBundle? = null): BundleRead.OwnedBundle {
              val response = client.post("admin/customFieldSettings/bundles/ownedField/${id.encodeURLParameter()}") {
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

          public class Values internal constructor(
            private val client: HttpClient,
            private val id: String,
          ) {
            public val `get`: Get = Get(client, id)

            public val post: Post = Post(client, id)

            public fun ownedBundleElementId(ownedBundleElementId: String): OwnedBundleElementIdPath = OwnedBundleElementIdPath(client, id, ownedBundleElementId)

            public class Get internal constructor(
              private val client: HttpClient,
              private val id: String,
            ) {
              public suspend operator fun invoke(
                fields: String? = "${'$'}type,color(${'$'}type,background,foreground,id),id,name,ordinal,owner(${'$'}type,id,login,ringId)",
                skip: Int? = null,
                top: Int? = null,
              ): List<BundleElementRead.OwnedBundleElement> {
                val response = client.get("admin/customFieldSettings/bundles/ownedField/${id.encodeURLParameter()}/values") {
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

            public class Post internal constructor(
              private val client: HttpClient,
              private val id: String,
            ) {
              public suspend operator fun invoke(fields: String? = "${'$'}type,color(${'$'}type,background,foreground,id),id,name,ordinal,owner(${'$'}type,id,login,ringId)", body: BundleElementWrite.OwnedBundleElement? = null): BundleElementRead.OwnedBundleElement {
                val response = client.post("admin/customFieldSettings/bundles/ownedField/${id.encodeURLParameter()}/values") {
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

            public class OwnedBundleElementIdPath internal constructor(
              private val client: HttpClient,
              private val id: String,
              private val ownedBundleElementId: String,
            ) {
              public val delete: Delete = Delete(client, id, ownedBundleElementId)

              public val `get`: Get = Get(client, id, ownedBundleElementId)

              public val post: Post = Post(client, id, ownedBundleElementId)

              public class Delete internal constructor(
                private val client: HttpClient,
                private val id: String,
                private val ownedBundleElementId: String,
              ) {
                public suspend operator fun invoke() {
                  val response = client.delete("admin/customFieldSettings/bundles/ownedField/${id.encodeURLParameter()}/values/${ownedBundleElementId.encodeURLParameter()}")
                  if (response.status.value != 200) throw ResponseException(response, "")
                }
              }

              public class Get internal constructor(
                private val client: HttpClient,
                private val id: String,
                private val ownedBundleElementId: String,
              ) {
                public suspend operator fun invoke(fields: String? = "${'$'}type,color(${'$'}type,background,foreground,id),id,name,ordinal,owner(${'$'}type,id,login,ringId)"): BundleElementRead.OwnedBundleElement {
                  val response = client.get("admin/customFieldSettings/bundles/ownedField/${id.encodeURLParameter()}/values/${ownedBundleElementId.encodeURLParameter()}") {
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
                private val ownedBundleElementId: String,
              ) {
                public suspend operator fun invoke(fields: String? = "${'$'}type,color(${'$'}type,background,foreground,id),id,name,ordinal,owner(${'$'}type,id,login,ringId)", body: BundleElementWrite.OwnedBundleElement? = null): BundleElementRead.OwnedBundleElement {
                  val response = client.post("admin/customFieldSettings/bundles/ownedField/${id.encodeURLParameter()}/values/${ownedBundleElementId.encodeURLParameter()}") {
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
        }
      }

      public class State internal constructor(
        private val client: HttpClient,
      ) {
        public val `get`: Get = Get(client)

        public val post: Post = Post(client)

        public fun id(id: String): IdPath = IdPath(client, id)

        public class Get internal constructor(
          private val client: HttpClient,
        ) {
          public suspend operator fun invoke(
            fields: String? = "${'$'}type,id",
            skip: Int? = null,
            top: Int? = null,
          ): List<BundleRead.StateBundle> {
            val response = client.get("admin/customFieldSettings/bundles/state") {
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

        public class Post internal constructor(
          private val client: HttpClient,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,id", body: BundleWrite.StateBundle? = null): BundleRead.StateBundle {
            val response = client.post("admin/customFieldSettings/bundles/state") {
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

        public class IdPath internal constructor(
          private val client: HttpClient,
          private val id: String,
        ) {
          public val delete: Delete = Delete(client, id)

          public val `get`: Get = Get(client, id)

          public val post: Post = Post(client, id)

          public val values: Values = Values(client, id)

          public class Delete internal constructor(
            private val client: HttpClient,
            private val id: String,
          ) {
            public suspend operator fun invoke() {
              val response = client.delete("admin/customFieldSettings/bundles/state/${id.encodeURLParameter()}")
              if (response.status.value != 200) throw ResponseException(response, "")
            }
          }

          public class Get internal constructor(
            private val client: HttpClient,
            private val id: String,
          ) {
            public suspend operator fun invoke(fields: String? = "${'$'}type,id"): BundleRead.StateBundle {
              val response = client.get("admin/customFieldSettings/bundles/state/${id.encodeURLParameter()}") {
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
            public suspend operator fun invoke(fields: String? = "${'$'}type,id", body: BundleWrite.StateBundle? = null): BundleRead.StateBundle {
              val response = client.post("admin/customFieldSettings/bundles/state/${id.encodeURLParameter()}") {
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

          public class Values internal constructor(
            private val client: HttpClient,
            private val id: String,
          ) {
            public val `get`: Get = Get(client, id)

            public val post: Post = Post(client, id)

            public fun stateBundleElementId(stateBundleElementId: String): StateBundleElementIdPath = StateBundleElementIdPath(client, id, stateBundleElementId)

            public class Get internal constructor(
              private val client: HttpClient,
              private val id: String,
            ) {
              public suspend operator fun invoke(
                fields: String? = "${'$'}type,color(${'$'}type,background,foreground,id),id,isResolved,localizedName,name,ordinal",
                skip: Int? = null,
                top: Int? = null,
              ): List<BundleElementRead.StateBundleElement> {
                val response = client.get("admin/customFieldSettings/bundles/state/${id.encodeURLParameter()}/values") {
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

            public class Post internal constructor(
              private val client: HttpClient,
              private val id: String,
            ) {
              public suspend operator fun invoke(fields: String? = "${'$'}type,color(${'$'}type,background,foreground,id),id,isResolved,localizedName,name,ordinal", body: BundleElementWrite.StateBundleElement? = null): BundleElementRead.StateBundleElement {
                val response = client.post("admin/customFieldSettings/bundles/state/${id.encodeURLParameter()}/values") {
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

            public class StateBundleElementIdPath internal constructor(
              private val client: HttpClient,
              private val id: String,
              private val stateBundleElementId: String,
            ) {
              public val delete: Delete = Delete(client, id, stateBundleElementId)

              public val `get`: Get = Get(client, id, stateBundleElementId)

              public val post: Post = Post(client, id, stateBundleElementId)

              public class Delete internal constructor(
                private val client: HttpClient,
                private val id: String,
                private val stateBundleElementId: String,
              ) {
                public suspend operator fun invoke() {
                  val response = client.delete("admin/customFieldSettings/bundles/state/${id.encodeURLParameter()}/values/${stateBundleElementId.encodeURLParameter()}")
                  if (response.status.value != 200) throw ResponseException(response, "")
                }
              }

              public class Get internal constructor(
                private val client: HttpClient,
                private val id: String,
                private val stateBundleElementId: String,
              ) {
                public suspend operator fun invoke(fields: String? = "${'$'}type,color(${'$'}type,background,foreground,id),id,isResolved,localizedName,name,ordinal"): BundleElementRead.StateBundleElement {
                  val response = client.get("admin/customFieldSettings/bundles/state/${id.encodeURLParameter()}/values/${stateBundleElementId.encodeURLParameter()}") {
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
                private val stateBundleElementId: String,
              ) {
                public suspend operator fun invoke(fields: String? = "${'$'}type,color(${'$'}type,background,foreground,id),id,isResolved,localizedName,name,ordinal", body: BundleElementWrite.StateBundleElement? = null): BundleElementRead.StateBundleElement {
                  val response = client.post("admin/customFieldSettings/bundles/state/${id.encodeURLParameter()}/values/${stateBundleElementId.encodeURLParameter()}") {
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
        }
      }

      public class User internal constructor(
        private val client: HttpClient,
      ) {
        public val `get`: Get = Get(client)

        public val post: Post = Post(client)

        public fun id(id: String): IdPath = IdPath(client, id)

        public class Get internal constructor(
          private val client: HttpClient,
        ) {
          public suspend operator fun invoke(
            fields: String? = "${'$'}type,id",
            skip: Int? = null,
            top: Int? = null,
          ): List<BundleRead.UserBundle> {
            val response = client.get("admin/customFieldSettings/bundles/user") {
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

        public class Post internal constructor(
          private val client: HttpClient,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,id", body: BundleWrite.UserBundle? = null): BundleRead.UserBundle {
            val response = client.post("admin/customFieldSettings/bundles/user") {
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

        public class IdPath internal constructor(
          private val client: HttpClient,
          private val id: String,
        ) {
          public val delete: Delete = Delete(client, id)

          public val `get`: Get = Get(client, id)

          public val post: Post = Post(client, id)

          public val aggregatedUsers: AggregatedUsers = AggregatedUsers(client, id)

          public val groups: Groups = Groups(client, id)

          public val individuals: Individuals = Individuals(client, id)

          public class Delete internal constructor(
            private val client: HttpClient,
            private val id: String,
          ) {
            public suspend operator fun invoke() {
              val response = client.delete("admin/customFieldSettings/bundles/user/${id.encodeURLParameter()}")
              if (response.status.value != 200) throw ResponseException(response, "")
            }
          }

          public class Get internal constructor(
            private val client: HttpClient,
            private val id: String,
          ) {
            public suspend operator fun invoke(fields: String? = "${'$'}type,id"): BundleRead.UserBundle {
              val response = client.get("admin/customFieldSettings/bundles/user/${id.encodeURLParameter()}") {
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
            public suspend operator fun invoke(fields: String? = "${'$'}type,id", body: BundleWrite.UserBundle? = null): BundleRead.UserBundle {
              val response = client.post("admin/customFieldSettings/bundles/user/${id.encodeURLParameter()}") {
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

          public class AggregatedUsers internal constructor(
            private val client: HttpClient,
            private val id: String,
          ) {
            public val `get`: Get = Get(client, id)

            public class Get internal constructor(
              private val client: HttpClient,
              private val id: String,
            ) {
              public suspend operator fun invoke(
                fields: String? = "${'$'}type,banned,email,fullName,guest,id,login,ringId",
                skip: Int? = null,
                top: Int? = null,
              ): List<UserRead> {
                val response = client.get("admin/customFieldSettings/bundles/user/${id.encodeURLParameter()}/aggregatedUsers") {
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

          public class Groups internal constructor(
            private val client: HttpClient,
            private val id: String,
          ) {
            public val `get`: Get = Get(client, id)

            public val post: Post = Post(client, id)

            public fun userGroupId(userGroupId: String): UserGroupIdPath = UserGroupIdPath(client, id, userGroupId)

            public class Get internal constructor(
              private val client: HttpClient,
              private val id: String,
            ) {
              public suspend operator fun invoke(
                fields: String? = "${'$'}type,allUsersGroup,id,name,ringId",
                skip: Int? = null,
                top: Int? = null,
              ): List<UserGroupRead> {
                val response = client.get("admin/customFieldSettings/bundles/user/${id.encodeURLParameter()}/groups") {
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

            public class Post internal constructor(
              private val client: HttpClient,
              private val id: String,
            ) {
              public suspend operator fun invoke(fields: String? = "${'$'}type,allUsersGroup,id,name,ringId", body: UserGroupWrite? = null): UserGroupRead {
                val response = client.post("admin/customFieldSettings/bundles/user/${id.encodeURLParameter()}/groups") {
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

            public class UserGroupIdPath internal constructor(
              private val client: HttpClient,
              private val id: String,
              private val userGroupId: String,
            ) {
              public val delete: Delete = Delete(client, id, userGroupId)

              public val `get`: Get = Get(client, id, userGroupId)

              public class Delete internal constructor(
                private val client: HttpClient,
                private val id: String,
                private val userGroupId: String,
              ) {
                public suspend operator fun invoke() {
                  val response = client.delete("admin/customFieldSettings/bundles/user/${id.encodeURLParameter()}/groups/${userGroupId.encodeURLParameter()}")
                  if (response.status.value != 200) throw ResponseException(response, "")
                }
              }

              public class Get internal constructor(
                private val client: HttpClient,
                private val id: String,
                private val userGroupId: String,
              ) {
                public suspend operator fun invoke(fields: String? = "${'$'}type,allUsersGroup,id,name,ringId"): UserGroupRead {
                  val response = client.get("admin/customFieldSettings/bundles/user/${id.encodeURLParameter()}/groups/${userGroupId.encodeURLParameter()}") {
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

          public class Individuals internal constructor(
            private val client: HttpClient,
            private val id: String,
          ) {
            public val `get`: Get = Get(client, id)

            public val post: Post = Post(client, id)

            public fun userId(userId: String): UserIdPath = UserIdPath(client, id, userId)

            public class Get internal constructor(
              private val client: HttpClient,
              private val id: String,
            ) {
              public suspend operator fun invoke(
                fields: String? = "${'$'}type,banned,email,fullName,guest,id,login,ringId",
                skip: Int? = null,
                top: Int? = null,
              ): List<UserRead> {
                val response = client.get("admin/customFieldSettings/bundles/user/${id.encodeURLParameter()}/individuals") {
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

            public class Post internal constructor(
              private val client: HttpClient,
              private val id: String,
            ) {
              public suspend operator fun invoke(fields: String? = "${'$'}type,banned,email,fullName,guest,id,login,ringId", body: UserWrite? = null): UserRead {
                val response = client.post("admin/customFieldSettings/bundles/user/${id.encodeURLParameter()}/individuals") {
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

            public class UserIdPath internal constructor(
              private val client: HttpClient,
              private val id: String,
              private val userId: String,
            ) {
              public val delete: Delete = Delete(client, id, userId)

              public val `get`: Get = Get(client, id, userId)

              public class Delete internal constructor(
                private val client: HttpClient,
                private val id: String,
                private val userId: String,
              ) {
                public suspend operator fun invoke() {
                  val response = client.delete("admin/customFieldSettings/bundles/user/${id.encodeURLParameter()}/individuals/${userId.encodeURLParameter()}")
                  if (response.status.value != 200) throw ResponseException(response, "")
                }
              }

              public class Get internal constructor(
                private val client: HttpClient,
                private val id: String,
                private val userId: String,
              ) {
                public suspend operator fun invoke(fields: String? = "${'$'}type,banned,email,fullName,guest,id,login,ringId"): UserRead {
                  val response = client.get("admin/customFieldSettings/bundles/user/${id.encodeURLParameter()}/individuals/${userId.encodeURLParameter()}") {
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
        }
      }

      public class Version internal constructor(
        private val client: HttpClient,
      ) {
        public val `get`: Get = Get(client)

        public val post: Post = Post(client)

        public fun id(id: String): IdPath = IdPath(client, id)

        public class Get internal constructor(
          private val client: HttpClient,
        ) {
          public suspend operator fun invoke(
            fields: String? = "${'$'}type,id",
            skip: Int? = null,
            top: Int? = null,
          ): List<BundleRead.VersionBundle> {
            val response = client.get("admin/customFieldSettings/bundles/version") {
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

        public class Post internal constructor(
          private val client: HttpClient,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,id", body: BundleWrite.VersionBundle? = null): BundleRead.VersionBundle {
            val response = client.post("admin/customFieldSettings/bundles/version") {
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

        public class IdPath internal constructor(
          private val client: HttpClient,
          private val id: String,
        ) {
          public val delete: Delete = Delete(client, id)

          public val `get`: Get = Get(client, id)

          public val post: Post = Post(client, id)

          public val values: Values = Values(client, id)

          public class Delete internal constructor(
            private val client: HttpClient,
            private val id: String,
          ) {
            public suspend operator fun invoke() {
              val response = client.delete("admin/customFieldSettings/bundles/version/${id.encodeURLParameter()}")
              if (response.status.value != 200) throw ResponseException(response, "")
            }
          }

          public class Get internal constructor(
            private val client: HttpClient,
            private val id: String,
          ) {
            public suspend operator fun invoke(fields: String? = "${'$'}type,id"): BundleRead.VersionBundle {
              val response = client.get("admin/customFieldSettings/bundles/version/${id.encodeURLParameter()}") {
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
            public suspend operator fun invoke(fields: String? = "${'$'}type,id", body: BundleWrite.VersionBundle? = null): BundleRead.VersionBundle {
              val response = client.post("admin/customFieldSettings/bundles/version/${id.encodeURLParameter()}") {
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

          public class Values internal constructor(
            private val client: HttpClient,
            private val id: String,
          ) {
            public val `get`: Get = Get(client, id)

            public val post: Post = Post(client, id)

            public fun versionBundleElementId(versionBundleElementId: String): VersionBundleElementIdPath = VersionBundleElementIdPath(client, id, versionBundleElementId)

            public class Get internal constructor(
              private val client: HttpClient,
              private val id: String,
            ) {
              public suspend operator fun invoke(
                fields: String? = "${'$'}type,archived,color(${'$'}type,background,foreground,id),id,name,ordinal,releaseDate,released",
                skip: Int? = null,
                top: Int? = null,
              ): List<BundleElementRead.VersionBundleElement> {
                val response = client.get("admin/customFieldSettings/bundles/version/${id.encodeURLParameter()}/values") {
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

            public class Post internal constructor(
              private val client: HttpClient,
              private val id: String,
            ) {
              public suspend operator fun invoke(fields: String? = "${'$'}type,archived,color(${'$'}type,background,foreground,id),id,name,ordinal,releaseDate,released", body: BundleElementWrite.VersionBundleElement? = null): BundleElementRead.VersionBundleElement {
                val response = client.post("admin/customFieldSettings/bundles/version/${id.encodeURLParameter()}/values") {
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

            public class VersionBundleElementIdPath internal constructor(
              private val client: HttpClient,
              private val id: String,
              private val versionBundleElementId: String,
            ) {
              public val delete: Delete = Delete(client, id, versionBundleElementId)

              public val `get`: Get = Get(client, id, versionBundleElementId)

              public val post: Post = Post(client, id, versionBundleElementId)

              public class Delete internal constructor(
                private val client: HttpClient,
                private val id: String,
                private val versionBundleElementId: String,
              ) {
                public suspend operator fun invoke() {
                  val response = client.delete("admin/customFieldSettings/bundles/version/${id.encodeURLParameter()}/values/${versionBundleElementId.encodeURLParameter()}")
                  if (response.status.value != 200) throw ResponseException(response, "")
                }
              }

              public class Get internal constructor(
                private val client: HttpClient,
                private val id: String,
                private val versionBundleElementId: String,
              ) {
                public suspend operator fun invoke(fields: String? = "${'$'}type,archived,color(${'$'}type,background,foreground,id),id,name,ordinal,releaseDate,released"): BundleElementRead.VersionBundleElement {
                  val response = client.get("admin/customFieldSettings/bundles/version/${id.encodeURLParameter()}/values/${versionBundleElementId.encodeURLParameter()}") {
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
                private val versionBundleElementId: String,
              ) {
                public suspend operator fun invoke(fields: String? = "${'$'}type,archived,color(${'$'}type,background,foreground,id),id,name,ordinal,releaseDate,released", body: BundleElementWrite.VersionBundleElement? = null): BundleElementRead.VersionBundleElement {
                  val response = client.post("admin/customFieldSettings/bundles/version/${id.encodeURLParameter()}/values/${versionBundleElementId.encodeURLParameter()}") {
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
        }
      }
    }

    public class CustomFields internal constructor(
      private val client: HttpClient,
    ) {
      public val `get`: Get = Get(client)

      public val post: Post = Post(client)

      public fun id(id: String): IdPath = IdPath(client, id)

      public class Get internal constructor(
        private val client: HttpClient,
      ) {
        public suspend operator fun invoke(
          fields: String? = "${'$'}type,fieldType(${'$'}type,id),id,isAutoAttached,isUpdateable,localizedName,name,ordinal",
          skip: Int? = null,
          top: Int? = null,
        ): List<CustomFieldRead> {
          val response = client.get("admin/customFieldSettings/customFields") {
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

      public class Post internal constructor(
        private val client: HttpClient,
      ) {
        public suspend operator fun invoke(fields: String? = "${'$'}type,fieldType(${'$'}type,id),id,isAutoAttached,isUpdateable,localizedName,name,ordinal", body: CustomFieldWrite? = null): CustomFieldRead {
          val response = client.post("admin/customFieldSettings/customFields") {
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

      public class IdPath internal constructor(
        private val client: HttpClient,
        private val id: String,
      ) {
        public val delete: Delete = Delete(client, id)

        public val `get`: Get = Get(client, id)

        public val post: Post = Post(client, id)

        public val fieldDefaults: FieldDefaults = FieldDefaults(client, id)

        public val instances: Instances = Instances(client, id)

        public class Delete internal constructor(
          private val client: HttpClient,
          private val id: String,
        ) {
          public suspend operator fun invoke() {
            val response = client.delete("admin/customFieldSettings/customFields/${id.encodeURLParameter()}")
            if (response.status.value != 200) throw ResponseException(response, "")
          }
        }

        public class Get internal constructor(
          private val client: HttpClient,
          private val id: String,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,fieldType(${'$'}type,id),id,isAutoAttached,isUpdateable,localizedName,name,ordinal"): CustomFieldRead {
            val response = client.get("admin/customFieldSettings/customFields/${id.encodeURLParameter()}") {
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
          public suspend operator fun invoke(fields: String? = "${'$'}type,fieldType(${'$'}type,id),id,isAutoAttached,isUpdateable,localizedName,name,ordinal", body: CustomFieldWrite? = null): CustomFieldRead {
            val response = client.post("admin/customFieldSettings/customFields/${id.encodeURLParameter()}") {
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

        public class FieldDefaults internal constructor(
          private val client: HttpClient,
          private val id: String,
        ) {
          public val `get`: Get = Get(client, id)

          public val post: Post = Post(client, id)

          public class Get internal constructor(
            private val client: HttpClient,
            private val id: String,
          ) {
            public suspend operator fun invoke(fields: String? = "${'$'}type,bundle(${'$'}type,id),canBeEmpty,defaultValues(${'$'}type,id,name),emptyFieldText,id,isPublic"): CustomFieldDefaultsRead {
              val response = client.get("admin/customFieldSettings/customFields/${id.encodeURLParameter()}/fieldDefaults") {
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
            public suspend operator fun invoke(fields: String? = "${'$'}type,bundle(${'$'}type,id),canBeEmpty,defaultValues(${'$'}type,id,name),emptyFieldText,id,isPublic", body: CustomFieldDefaultsWrite? = null): CustomFieldDefaultsRead {
              val response = client.post("admin/customFieldSettings/customFields/${id.encodeURLParameter()}/fieldDefaults") {
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

        public class Instances internal constructor(
          private val client: HttpClient,
          private val id: String,
        ) {
          public val `get`: Get = Get(client, id)

          public class Get internal constructor(
            private val client: HttpClient,
            private val id: String,
          ) {
            public suspend operator fun invoke(
              fields: String? = "${'$'}type,bundle(${'$'}type,id),canBeEmpty,defaultValues(${'$'}type,id,name),emptyFieldText,field(${'$'}type,fieldType(${'$'}type,id),id,localizedName,name),id,isPublic,ordinal",
              skip: Int? = null,
              top: Int? = null,
            ): List<ProjectCustomFieldRead> {
              val response = client.get("admin/customFieldSettings/customFields/${id.encodeURLParameter()}/instances") {
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
    }

    public class Types internal constructor(
      private val client: HttpClient,
    ) {
      public val `get`: Get = Get(client)

      public class Get internal constructor(
        private val client: HttpClient,
      ) {
        public suspend operator fun invoke(
          fields: String? = "${'$'}type,id",
          skip: Int? = null,
          top: Int? = null,
        ): List<FieldTypeRead> {
          val response = client.get("admin/customFieldSettings/types") {
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

  public class DatabaseBackup internal constructor(
    private val client: HttpClient,
  ) {
    public val backups: Backups = Backups(client)

    public val settings: Settings = Settings(client)

    public class Backups internal constructor(
      private val client: HttpClient,
    ) {
      public val `get`: Get = Get(client)

      public fun id(id: String): IdPath = IdPath(client, id)

      public class Get internal constructor(
        private val client: HttpClient,
      ) {
        public suspend operator fun invoke(
          fields: String? = "${'$'}type,creationDate,id,link,name,size",
          skip: Int? = null,
          top: Int? = null,
        ): List<BackupFile> {
          val response = client.get("admin/databaseBackup/backups") {
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

        public class Get internal constructor(
          private val client: HttpClient,
          private val id: String,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,creationDate,id,link,name,size"): BackupFile {
            val response = client.get("admin/databaseBackup/backups/${id.encodeURLParameter()}") {
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

    public class Settings internal constructor(
      private val client: HttpClient,
    ) {
      public val `get`: Get = Get(client)

      public val post: Post = Post(client)

      public val backupStatus: BackupStatus = BackupStatus(client)

      public class Get internal constructor(
        private val client: HttpClient,
      ) {
        public suspend operator fun invoke(fields: String? = "${'$'}type,availableDiskSpace,backupStatus(${'$'}type,backupCancelled,backupError(${'$'}type,date,errorMessage,id),backupInProgress,id),id,isOn,location"): DatabaseBackupSettingsRead {
          val response = client.get("admin/databaseBackup/settings") {
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
      ) {
        public suspend operator fun invoke(fields: String? = "${'$'}type,availableDiskSpace,backupStatus(${'$'}type,backupCancelled,backupError(${'$'}type,date,errorMessage,id),backupInProgress,id),id,isOn,location", body: DatabaseBackupSettingsWrite? = null): DatabaseBackupSettingsRead {
          val response = client.post("admin/databaseBackup/settings") {
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

      public class BackupStatus internal constructor(
        private val client: HttpClient,
      ) {
        public val `get`: Get = Get(client)

        public class Get internal constructor(
          private val client: HttpClient,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,backupCancelled,backupError(${'$'}type,date,errorMessage,id),backupInProgress,id"): io.youtrack.model.BackupStatus {
            val response = client.get("admin/databaseBackup/settings/backupStatus") {
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
  }

  public class GlobalSettings internal constructor(
    private val client: HttpClient,
  ) {
    public val `get`: Get = Get(client)

    public val post: Post = Post(client)

    public val appearanceSettings: AppearanceSettings = AppearanceSettings(client)

    public val license: License = License(client)

    public val localeSettings: LocaleSettings = LocaleSettings(client)

    public val notificationSettings: NotificationSettings = NotificationSettings(client)

    public val restSettings: RestSettings = RestSettings(client)

    public val systemSettings: SystemSettings = SystemSettings(client)

    public class Get internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(fields: String? = "${'$'}type,appearanceSettings(${'$'}type,dateFieldFormat(${'$'}type,datePattern,id,pattern,presentation),id,timeZone(${'$'}type,id,offset,presentation)),id,license(${'$'}type,error,id,license,username),localeSettings(${'$'}type,id,isRTL,locale(${'$'}type,community,id,language,locale,name)),notificationSettings(${'$'}type,emailSettings(${'$'}type,id,isEnabled),id),restSettings(${'$'}type,allowAllOrigins,allowedOrigins,id),systemSettings(${'$'}type,baseUrl,id,isApplicationReadOnly,maxExportItems,maxUploadFileSize)"): GlobalSettingsRead {
        val response = client.get("admin/globalSettings") {
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
    ) {
      public suspend operator fun invoke(fields: String? = "${'$'}type,appearanceSettings(${'$'}type,dateFieldFormat(${'$'}type,datePattern,id,pattern,presentation),id,timeZone(${'$'}type,id,offset,presentation)),id,license(${'$'}type,error,id,license,username),localeSettings(${'$'}type,id,isRTL,locale(${'$'}type,community,id,language,locale,name)),notificationSettings(${'$'}type,emailSettings(${'$'}type,id,isEnabled),id),restSettings(${'$'}type,allowAllOrigins,allowedOrigins,id),systemSettings(${'$'}type,baseUrl,id,isApplicationReadOnly,maxExportItems,maxUploadFileSize)", body: GlobalSettingsWrite? = null): GlobalSettingsRead {
        val response = client.post("admin/globalSettings") {
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

    public class AppearanceSettings internal constructor(
      private val client: HttpClient,
    ) {
      public val `get`: Get = Get(client)

      public val post: Post = Post(client)

      public class Get internal constructor(
        private val client: HttpClient,
      ) {
        public suspend operator fun invoke(fields: String? = "${'$'}type,dateFieldFormat(${'$'}type,datePattern,id,pattern,presentation),id,timeZone(${'$'}type,id,offset,presentation)"): AppearanceSettingsRead {
          val response = client.get("admin/globalSettings/appearanceSettings") {
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
      ) {
        public suspend operator fun invoke(fields: String? = "${'$'}type,dateFieldFormat(${'$'}type,datePattern,id,pattern,presentation),id,timeZone(${'$'}type,id,offset,presentation)", body: AppearanceSettingsWrite? = null): AppearanceSettingsRead {
          val response = client.post("admin/globalSettings/appearanceSettings") {
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

    public class License internal constructor(
      private val client: HttpClient,
    ) {
      public val `get`: Get = Get(client)

      public val post: Post = Post(client)

      public class Get internal constructor(
        private val client: HttpClient,
      ) {
        public suspend operator fun invoke(fields: String? = "${'$'}type,error,id,license,username"): LicenseRead {
          val response = client.get("admin/globalSettings/license") {
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
      ) {
        public suspend operator fun invoke(fields: String? = "${'$'}type,error,id,license,username", body: LicenseWrite? = null): LicenseRead {
          val response = client.post("admin/globalSettings/license") {
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

    public class LocaleSettings internal constructor(
      private val client: HttpClient,
    ) {
      public val `get`: Get = Get(client)

      public val post: Post = Post(client)

      public class Get internal constructor(
        private val client: HttpClient,
      ) {
        public suspend operator fun invoke(fields: String? = "${'$'}type,id,isRTL,locale(${'$'}type,community,id,language,locale,name)"): LocaleSettingsRead {
          val response = client.get("admin/globalSettings/localeSettings") {
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
      ) {
        public suspend operator fun invoke(fields: String? = "${'$'}type,id,isRTL,locale(${'$'}type,community,id,language,locale,name)", body: LocaleSettingsWrite? = null): LocaleSettingsRead {
          val response = client.post("admin/globalSettings/localeSettings") {
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

    public class NotificationSettings internal constructor(
      private val client: HttpClient,
    ) {
      public val `get`: Get = Get(client)

      public val post: Post = Post(client)

      public class Get internal constructor(
        private val client: HttpClient,
      ) {
        public suspend operator fun invoke(fields: String? = "${'$'}type,emailSettings(${'$'}type,id,isEnabled),id"): NotificationSettingsRead {
          val response = client.get("admin/globalSettings/notificationSettings") {
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
      ) {
        public suspend operator fun invoke(fields: String? = "${'$'}type,emailSettings(${'$'}type,id,isEnabled),id", body: NotificationSettingsWrite? = null): NotificationSettingsRead {
          val response = client.post("admin/globalSettings/notificationSettings") {
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

    public class RestSettings internal constructor(
      private val client: HttpClient,
    ) {
      public val `get`: Get = Get(client)

      public val post: Post = Post(client)

      public class Get internal constructor(
        private val client: HttpClient,
      ) {
        public suspend operator fun invoke(fields: String? = "${'$'}type,allowAllOrigins,allowedOrigins,id"): RestCorsSettingsRead {
          val response = client.get("admin/globalSettings/restSettings") {
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
      ) {
        public suspend operator fun invoke(fields: String? = "${'$'}type,allowAllOrigins,allowedOrigins,id", body: RestCorsSettingsWrite? = null): RestCorsSettingsRead {
          val response = client.post("admin/globalSettings/restSettings") {
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

    public class SystemSettings internal constructor(
      private val client: HttpClient,
    ) {
      public val `get`: Get = Get(client)

      public val post: Post = Post(client)

      public class Get internal constructor(
        private val client: HttpClient,
      ) {
        public suspend operator fun invoke(fields: String? = "${'$'}type,baseUrl,id,isApplicationReadOnly,maxExportItems,maxUploadFileSize"): SystemSettingsRead {
          val response = client.get("admin/globalSettings/systemSettings") {
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
      ) {
        public suspend operator fun invoke(fields: String? = "${'$'}type,baseUrl,id,isApplicationReadOnly,maxExportItems,maxUploadFileSize", body: SystemSettingsWrite? = null): SystemSettingsRead {
          val response = client.post("admin/globalSettings/systemSettings") {
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

  public class Projects internal constructor(
    private val client: HttpClient,
  ) {
    public val `get`: Get = Get(client)

    public val post: Post = Post(client)

    public fun id(id: String): IdPath = IdPath(client, id)

    public class Get internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(
        fields: String? = "${'$'}type,archived,customFields,id,leader(${'$'}type,id,login,ringId),name,shortName",
        skip: Int? = null,
        top: Int? = null,
      ): List<IssueFolderRead.Project> {
        val response = client.get("admin/projects") {
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

    public class Post internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(
        template: String? = null,
        fields: String? = "${'$'}type,archived,customFields,id,leader(${'$'}type,id,login,ringId),name,shortName",
        body: IssueFolderWrite.Project? = null,
      ): IssueFolderRead.Project {
        val response = client.post("admin/projects") {
          if (template != null) {
            url.encodedParameters.append("template", template.encodeURLParameter())
          }
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

    public class IdPath internal constructor(
      private val client: HttpClient,
      private val id: String,
    ) {
      public val delete: Delete = Delete(client, id)

      public val `get`: Get = Get(client, id)

      public val post: Post = Post(client, id)

      public val articles: Articles = Articles(client, id)

      public val customFields: CustomFields = CustomFields(client, id)

      public val issues: Issues = Issues(client, id)

      public val timeTrackingSettings: TimeTrackingSettings = TimeTrackingSettings(client, id)

      public class Delete internal constructor(
        private val client: HttpClient,
        private val id: String,
      ) {
        public suspend operator fun invoke() {
          val response = client.delete("admin/projects/${id.encodeURLParameter()}")
          if (response.status.value != 200) throw ResponseException(response, "")
        }
      }

      public class Get internal constructor(
        private val client: HttpClient,
        private val id: String,
      ) {
        public suspend operator fun invoke(fields: String? = "${'$'}type,archived,customFields,id,leader(${'$'}type,id,login,ringId),name,shortName"): IssueFolderRead.Project {
          val response = client.get("admin/projects/${id.encodeURLParameter()}") {
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
        public suspend operator fun invoke(fields: String? = "${'$'}type,archived,customFields,id,leader(${'$'}type,id,login,ringId),name,shortName", body: IssueFolderWrite.Project? = null): IssueFolderRead.Project {
          val response = client.post("admin/projects/${id.encodeURLParameter()}") {
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

      public class Articles internal constructor(
        private val client: HttpClient,
        private val id: String,
      ) {
        public val `get`: Get = Get(client, id)

        public class Get internal constructor(
          private val client: HttpClient,
          private val id: String,
        ) {
          public suspend operator fun invoke(
            fields: String? = "${'$'}type,content,created,id,idReadable,parentArticle(${'$'}type,id,idReadable),project(${'$'}type,id,name,shortName),summary,updated,updatedBy(${'$'}type,id,login,ringId)",
            skip: Int? = null,
            top: Int? = null,
          ): List<BaseArticleRead.Article> {
            val response = client.get("admin/projects/${id.encodeURLParameter()}/articles") {
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

      public class CustomFields internal constructor(
        private val client: HttpClient,
        private val id: String,
      ) {
        public val `get`: Get = Get(client, id)

        public val post: Post = Post(client, id)

        public fun projectCustomFieldId(projectCustomFieldId: String): ProjectCustomFieldIdPath = ProjectCustomFieldIdPath(client, id, projectCustomFieldId)

        public class Get internal constructor(
          private val client: HttpClient,
          private val id: String,
        ) {
          public suspend operator fun invoke(
            fields: String? = "${'$'}type,bundle(${'$'}type,id),canBeEmpty,defaultValues(${'$'}type,id,name),emptyFieldText,field(${'$'}type,fieldType(${'$'}type,id),id,localizedName,name),id,isPublic,ordinal",
            skip: Int? = null,
            top: Int? = null,
          ): List<ProjectCustomFieldRead> {
            val response = client.get("admin/projects/${id.encodeURLParameter()}/customFields") {
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

        public class Post internal constructor(
          private val client: HttpClient,
          private val id: String,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,bundle(${'$'}type,id),canBeEmpty,defaultValues(${'$'}type,id,name),emptyFieldText,field(${'$'}type,fieldType(${'$'}type,id),id,localizedName,name),id,isPublic,ordinal", body: ProjectCustomFieldWrite? = null): ProjectCustomFieldRead {
            val response = client.post("admin/projects/${id.encodeURLParameter()}/customFields") {
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

        public class ProjectCustomFieldIdPath internal constructor(
          private val client: HttpClient,
          private val id: String,
          private val projectCustomFieldId: String,
        ) {
          public val delete: Delete = Delete(client, id, projectCustomFieldId)

          public val `get`: Get = Get(client, id, projectCustomFieldId)

          public val post: Post = Post(client, id, projectCustomFieldId)

          public class Delete internal constructor(
            private val client: HttpClient,
            private val id: String,
            private val projectCustomFieldId: String,
          ) {
            public suspend operator fun invoke() {
              val response = client.delete("admin/projects/${id.encodeURLParameter()}/customFields/${projectCustomFieldId.encodeURLParameter()}")
              if (response.status.value != 200) throw ResponseException(response, "")
            }
          }

          public class Get internal constructor(
            private val client: HttpClient,
            private val id: String,
            private val projectCustomFieldId: String,
          ) {
            public suspend operator fun invoke(fields: String? = "${'$'}type,bundle(${'$'}type,id),canBeEmpty,defaultValues(${'$'}type,id,name),emptyFieldText,field(${'$'}type,fieldType(${'$'}type,id),id,localizedName,name),id,isPublic,ordinal"): ProjectCustomFieldRead {
              val response = client.get("admin/projects/${id.encodeURLParameter()}/customFields/${projectCustomFieldId.encodeURLParameter()}") {
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
            private val projectCustomFieldId: String,
          ) {
            public suspend operator fun invoke(fields: String? = "${'$'}type,bundle(${'$'}type,id),canBeEmpty,defaultValues(${'$'}type,id,name),emptyFieldText,field(${'$'}type,fieldType(${'$'}type,id),id,localizedName,name),id,isPublic,ordinal", body: ProjectCustomFieldWrite? = null): ProjectCustomFieldRead {
              val response = client.post("admin/projects/${id.encodeURLParameter()}/customFields/${projectCustomFieldId.encodeURLParameter()}") {
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

      public class Issues internal constructor(
        private val client: HttpClient,
        private val id: String,
      ) {
        public val `get`: Get = Get(client, id)

        public val post: Post = Post(client, id)

        public fun issueId(issueId: String): IssueIdPath = IssueIdPath(client, id, issueId)

        public class Get internal constructor(
          private val client: HttpClient,
          private val id: String,
        ) {
          public suspend operator fun invoke(
            customFields: String? = null,
            fields: String? = "${'$'}type,created,customFields(${'$'}type,id,name,value(${'$'}type,id,name)),description,id,idReadable,links(${'$'}type,direction,id,linkType(${'$'}type,id,localizedName,name)),numberInProject,project(${'$'}type,id,name,shortName),reporter(${'$'}type,id,login,ringId),resolved,summary,updated,updater(${'$'}type,id,login,ringId),visibility(${'$'}type,id,permittedGroups(${'$'}type,id,name,ringId),permittedUsers(${'$'}type,id,login,ringId))",
            skip: Int? = null,
            top: Int? = null,
          ): List<IssueRead> {
            val response = client.get("admin/projects/${id.encodeURLParameter()}/issues") {
              if (customFields != null) {
                url.encodedParameters.append("customFields", customFields.encodeURLParameter())
              }
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

        public class Post internal constructor(
          private val client: HttpClient,
          private val id: String,
        ) {
          public suspend operator fun invoke(
            muteUpdateNotifications: Boolean? = null,
            fields: String? = "${'$'}type,created,customFields(${'$'}type,id,name,value(${'$'}type,id,name)),description,id,idReadable,links(${'$'}type,direction,id,linkType(${'$'}type,id,localizedName,name)),numberInProject,project(${'$'}type,id,name,shortName),reporter(${'$'}type,id,login,ringId),resolved,summary,updated,updater(${'$'}type,id,login,ringId),visibility(${'$'}type,id,permittedGroups(${'$'}type,id,name,ringId),permittedUsers(${'$'}type,id,login,ringId))",
            body: IssueWrite? = null,
          ): IssueRead {
            val response = client.post("admin/projects/${id.encodeURLParameter()}/issues") {
              if (muteUpdateNotifications != null) {
                url.encodedParameters.append("muteUpdateNotifications", muteUpdateNotifications.toString())
              }
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

        public class IssueIdPath internal constructor(
          private val client: HttpClient,
          private val id: String,
          private val issueId: String,
        ) {
          public val delete: Delete = Delete(client, id, issueId)

          public val `get`: Get = Get(client, id, issueId)

          public val post: Post = Post(client, id, issueId)

          public class Delete internal constructor(
            private val client: HttpClient,
            private val id: String,
            private val issueId: String,
          ) {
            public suspend operator fun invoke() {
              val response = client.delete("admin/projects/${id.encodeURLParameter()}/issues/${issueId.encodeURLParameter()}")
              if (response.status.value != 200) throw ResponseException(response, "")
            }
          }

          public class Get internal constructor(
            private val client: HttpClient,
            private val id: String,
            private val issueId: String,
          ) {
            public suspend operator fun invoke(fields: String? = "${'$'}type,created,customFields(${'$'}type,id,name,value(${'$'}type,id,name)),description,id,idReadable,links(${'$'}type,direction,id,linkType(${'$'}type,id,localizedName,name)),numberInProject,project(${'$'}type,id,name,shortName),reporter(${'$'}type,id,login,ringId),resolved,summary,updated,updater(${'$'}type,id,login,ringId),visibility(${'$'}type,id,permittedGroups(${'$'}type,id,name,ringId),permittedUsers(${'$'}type,id,login,ringId))"): IssueRead {
              val response = client.get("admin/projects/${id.encodeURLParameter()}/issues/${issueId.encodeURLParameter()}") {
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
            private val issueId: String,
          ) {
            public suspend operator fun invoke(
              muteUpdateNotifications: Boolean? = null,
              fields: String? = "${'$'}type,created,customFields(${'$'}type,id,name,value(${'$'}type,id,name)),description,id,idReadable,links(${'$'}type,direction,id,linkType(${'$'}type,id,localizedName,name)),numberInProject,project(${'$'}type,id,name,shortName),reporter(${'$'}type,id,login,ringId),resolved,summary,updated,updater(${'$'}type,id,login,ringId),visibility(${'$'}type,id,permittedGroups(${'$'}type,id,name,ringId),permittedUsers(${'$'}type,id,login,ringId))",
              body: IssueWrite? = null,
            ): IssueRead {
              val response = client.post("admin/projects/${id.encodeURLParameter()}/issues/${issueId.encodeURLParameter()}") {
                if (muteUpdateNotifications != null) {
                  url.encodedParameters.append("muteUpdateNotifications", muteUpdateNotifications.toString())
                }
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

      public class TimeTrackingSettings internal constructor(
        private val client: HttpClient,
        private val id: String,
      ) {
        public val `get`: Get = Get(client, id)

        public val post: Post = Post(client, id)

        public val workItemTypes: WorkItemTypes = WorkItemTypes(client, id)

        public class Get internal constructor(
          private val client: HttpClient,
          private val id: String,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,enabled,estimate(${'$'}type,field(${'$'}type,fieldType(${'$'}type,id),id,localizedName,name),id),id,timeSpent(${'$'}type,field(${'$'}type,fieldType(${'$'}type,id),id,localizedName,name),id)"): ProjectTimeTrackingSettingsRead {
            val response = client.get("admin/projects/${id.encodeURLParameter()}/timeTrackingSettings") {
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
          public suspend operator fun invoke(fields: String? = "${'$'}type,enabled,estimate(${'$'}type,field(${'$'}type,fieldType(${'$'}type,id),id,localizedName,name),id),id,timeSpent(${'$'}type,field(${'$'}type,fieldType(${'$'}type,id),id,localizedName,name),id)", body: ProjectTimeTrackingSettingsWrite? = null): ProjectTimeTrackingSettingsRead {
            val response = client.post("admin/projects/${id.encodeURLParameter()}/timeTrackingSettings") {
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

        public class WorkItemTypes internal constructor(
          private val client: HttpClient,
          private val id: String,
        ) {
          public val `get`: Get = Get(client, id)

          public val post: Post = Post(client, id)

          public fun workItemTypeId(workItemTypeId: String): WorkItemTypeIdPath = WorkItemTypeIdPath(client, id, workItemTypeId)

          public class Get internal constructor(
            private val client: HttpClient,
            private val id: String,
          ) {
            public suspend operator fun invoke(
              fields: String? = "${'$'}type,autoAttached,id,name",
              skip: Int? = null,
              top: Int? = null,
            ): List<WorkItemTypeRead> {
              val response = client.get("admin/projects/${id.encodeURLParameter()}/timeTrackingSettings/workItemTypes") {
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

          public class Post internal constructor(
            private val client: HttpClient,
            private val id: String,
          ) {
            public suspend operator fun invoke(fields: String? = "${'$'}type,autoAttached,id,name", body: WorkItemTypeWrite? = null): WorkItemTypeRead {
              val response = client.post("admin/projects/${id.encodeURLParameter()}/timeTrackingSettings/workItemTypes") {
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

          public class WorkItemTypeIdPath internal constructor(
            private val client: HttpClient,
            private val id: String,
            private val workItemTypeId: String,
          ) {
            public val delete: Delete = Delete(client, id, workItemTypeId)

            public val `get`: Get = Get(client, id, workItemTypeId)

            public class Delete internal constructor(
              private val client: HttpClient,
              private val id: String,
              private val workItemTypeId: String,
            ) {
              public suspend operator fun invoke() {
                val response = client.delete("admin/projects/${id.encodeURLParameter()}/timeTrackingSettings/workItemTypes/${workItemTypeId.encodeURLParameter()}")
                if (response.status.value != 200) throw ResponseException(response, "")
              }
            }

            public class Get internal constructor(
              private val client: HttpClient,
              private val id: String,
              private val workItemTypeId: String,
            ) {
              public suspend operator fun invoke(fields: String? = "${'$'}type,autoAttached,id,name"): WorkItemTypeRead {
                val response = client.get("admin/projects/${id.encodeURLParameter()}/timeTrackingSettings/workItemTypes/${workItemTypeId.encodeURLParameter()}") {
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
      }
    }
  }

  public class Telemetry internal constructor(
    private val client: HttpClient,
  ) {
    public val `get`: Get = Get(client)

    public class Get internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(fields: String? = "${'$'}type,allocatedMemory,availableMemory,availableProcessors,blobStringsCacheHitRate,cachedResultsCountInDBQueriesCache,databaseBackgroundThreads,databaseLocation,databaseQueriesCacheHitRate,databaseSize,fullDatabaseSize,id,installationFolder,logsLocation,notificationAnalyzerThreads,onlineUsers(${'$'}type,id,users),pendingAsyncJobs,reportCalculatorThreads,requestsPerSecond,startedTime,textIndexSize,totalTransactions,transactionsPerSecond,uptime,usedMemory"): io.youtrack.model.Telemetry {
        val response = client.get("admin/telemetry") {
          if (fields != null) {
            url.encodedParameters.append("fields", fields.encodeURLParameter())
          }
        }
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }
  }

  public class TimeTrackingSettings internal constructor(
    private val client: HttpClient,
  ) {
    public val `get`: Get = Get(client)

    public val workItemTypes: WorkItemTypes = WorkItemTypes(client)

    public val workTimeSettings: WorkTimeSettings = WorkTimeSettings(client)

    public class Get internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(fields: String? = "${'$'}type,id,workItemTypes(${'$'}type,id,name),workTimeSettings(${'$'}type,daysAWeek,firstDayOfWeek,id,minutesADay,workDays)"): GlobalTimeTrackingSettings {
        val response = client.get("admin/timeTrackingSettings") {
          if (fields != null) {
            url.encodedParameters.append("fields", fields.encodeURLParameter())
          }
        }
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }

    public class WorkItemTypes internal constructor(
      private val client: HttpClient,
    ) {
      public val `get`: Get = Get(client)

      public val post: Post = Post(client)

      public fun workItemTypeId(workItemTypeId: String): WorkItemTypeIdPath = WorkItemTypeIdPath(client, workItemTypeId)

      public class Get internal constructor(
        private val client: HttpClient,
      ) {
        public suspend operator fun invoke(
          fields: String? = "${'$'}type,autoAttached,id,name",
          skip: Int? = null,
          top: Int? = null,
        ): List<WorkItemTypeRead> {
          val response = client.get("admin/timeTrackingSettings/workItemTypes") {
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

      public class Post internal constructor(
        private val client: HttpClient,
      ) {
        public suspend operator fun invoke(fields: String? = "${'$'}type,autoAttached,id,name", body: WorkItemTypeWrite? = null): WorkItemTypeRead {
          val response = client.post("admin/timeTrackingSettings/workItemTypes") {
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

      public class WorkItemTypeIdPath internal constructor(
        private val client: HttpClient,
        private val workItemTypeId: String,
      ) {
        public val delete: Delete = Delete(client, workItemTypeId)

        public val `get`: Get = Get(client, workItemTypeId)

        public val post: Post = Post(client, workItemTypeId)

        public class Delete internal constructor(
          private val client: HttpClient,
          private val workItemTypeId: String,
        ) {
          public suspend operator fun invoke() {
            val response = client.delete("admin/timeTrackingSettings/workItemTypes/${workItemTypeId.encodeURLParameter()}")
            if (response.status.value != 200) throw ResponseException(response, "")
          }
        }

        public class Get internal constructor(
          private val client: HttpClient,
          private val workItemTypeId: String,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,autoAttached,id,name"): WorkItemTypeRead {
            val response = client.get("admin/timeTrackingSettings/workItemTypes/${workItemTypeId.encodeURLParameter()}") {
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
          private val workItemTypeId: String,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,autoAttached,id,name", body: WorkItemTypeWrite? = null): WorkItemTypeRead {
            val response = client.post("admin/timeTrackingSettings/workItemTypes/${workItemTypeId.encodeURLParameter()}") {
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

    public class WorkTimeSettings internal constructor(
      private val client: HttpClient,
    ) {
      public val `get`: Get = Get(client)

      public val post: Post = Post(client)

      public class Get internal constructor(
        private val client: HttpClient,
      ) {
        public suspend operator fun invoke(fields: String? = "${'$'}type,daysAWeek,firstDayOfWeek,id,minutesADay,workDays"): WorkTimeSettingsRead {
          val response = client.get("admin/timeTrackingSettings/workTimeSettings") {
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
      ) {
        public suspend operator fun invoke(fields: String? = "${'$'}type,daysAWeek,firstDayOfWeek,id,minutesADay,workDays", body: WorkTimeSettingsWrite? = null): WorkTimeSettingsRead {
          val response = client.post("admin/timeTrackingSettings/workTimeSettings") {
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
}
