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
          ): List<BundleRead.BuildBundle> = client.get("/admin/customFieldSettings/bundles/build") {
            fields?.let { parameter("fields", it) }
            skip?.let { parameter("${'$'}skip", it) }
            top?.let { parameter("${'$'}top", it) }
          }.body()
        }

        public class Post internal constructor(
          private val client: HttpClient,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,id", body: BundleWrite.BuildBundle? = null): BundleRead.BuildBundle = client.post("/admin/customFieldSettings/bundles/build") {
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

          public val values: Values = Values(client, id)

          public class Delete internal constructor(
            private val client: HttpClient,
            private val id: String,
          ) {
            public suspend operator fun invoke() {
              client.delete("/admin/customFieldSettings/bundles/build/$id")
            }
          }

          public class Get internal constructor(
            private val client: HttpClient,
            private val id: String,
          ) {
            public suspend operator fun invoke(fields: String? = "${'$'}type,id"): BundleRead.BuildBundle = client.get("/admin/customFieldSettings/bundles/build/$id") {
              fields?.let { parameter("fields", it) }
            }.body()
          }

          public class Post internal constructor(
            private val client: HttpClient,
            private val id: String,
          ) {
            public suspend operator fun invoke(fields: String? = "${'$'}type,id", body: BundleWrite.BuildBundle? = null): BundleRead.BuildBundle = client.post("/admin/customFieldSettings/bundles/build/$id") {
              fields?.let { parameter("fields", it) }
              body?.let {
                contentType(ContentType.Application.Json)
                setBody(it)
              }
            }.body()
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
              ): List<BundleElementRead.BuildBundleElement> = client.get("/admin/customFieldSettings/bundles/build/$id/values") {
                fields?.let { parameter("fields", it) }
                skip?.let { parameter("${'$'}skip", it) }
                top?.let { parameter("${'$'}top", it) }
              }.body()
            }

            public class Post internal constructor(
              private val client: HttpClient,
              private val id: String,
            ) {
              public suspend operator fun invoke(fields: String? = "${'$'}type,assembleDate,color(${'$'}type,background,foreground,id),id,name,ordinal", body: BundleElementWrite.BuildBundleElement? = null): BundleElementRead.BuildBundleElement = client.post("/admin/customFieldSettings/bundles/build/$id/values") {
                fields?.let { parameter("fields", it) }
                body?.let {
                  contentType(ContentType.Application.Json)
                  setBody(it)
                }
              }.body()
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
                  client.delete("/admin/customFieldSettings/bundles/build/$id/values/$buildBundleElementId")
                }
              }

              public class Get internal constructor(
                private val client: HttpClient,
                private val id: String,
                private val buildBundleElementId: String,
              ) {
                public suspend operator fun invoke(fields: String? = "${'$'}type,assembleDate,color(${'$'}type,background,foreground,id),id,name,ordinal"): BundleElementRead.BuildBundleElement = client.get("/admin/customFieldSettings/bundles/build/$id/values/$buildBundleElementId") {
                  fields?.let { parameter("fields", it) }
                }.body()
              }

              public class Post internal constructor(
                private val client: HttpClient,
                private val id: String,
                private val buildBundleElementId: String,
              ) {
                public suspend operator fun invoke(fields: String? = "${'$'}type,assembleDate,color(${'$'}type,background,foreground,id),id,name,ordinal", body: BundleElementWrite.BuildBundleElement? = null): BundleElementRead.BuildBundleElement = client.post("/admin/customFieldSettings/bundles/build/$id/values/$buildBundleElementId") {
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
          ): List<BundleRead.EnumBundle> = client.get("/admin/customFieldSettings/bundles/enum") {
            fields?.let { parameter("fields", it) }
            skip?.let { parameter("${'$'}skip", it) }
            top?.let { parameter("${'$'}top", it) }
          }.body()
        }

        public class Post internal constructor(
          private val client: HttpClient,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,id", body: BundleWrite.EnumBundle? = null): BundleRead.EnumBundle = client.post("/admin/customFieldSettings/bundles/enum") {
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

          public val values: Values = Values(client, id)

          public class Delete internal constructor(
            private val client: HttpClient,
            private val id: String,
          ) {
            public suspend operator fun invoke() {
              client.delete("/admin/customFieldSettings/bundles/enum/$id")
            }
          }

          public class Get internal constructor(
            private val client: HttpClient,
            private val id: String,
          ) {
            public suspend operator fun invoke(fields: String? = "${'$'}type,id"): BundleRead.EnumBundle = client.get("/admin/customFieldSettings/bundles/enum/$id") {
              fields?.let { parameter("fields", it) }
            }.body()
          }

          public class Post internal constructor(
            private val client: HttpClient,
            private val id: String,
          ) {
            public suspend operator fun invoke(fields: String? = "${'$'}type,id", body: BundleWrite.EnumBundle? = null): BundleRead.EnumBundle = client.post("/admin/customFieldSettings/bundles/enum/$id") {
              fields?.let { parameter("fields", it) }
              body?.let {
                contentType(ContentType.Application.Json)
                setBody(it)
              }
            }.body()
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
              ): List<BundleElementRead.EnumBundleElement> = client.get("/admin/customFieldSettings/bundles/enum/$id/values") {
                fields?.let { parameter("fields", it) }
                skip?.let { parameter("${'$'}skip", it) }
                top?.let { parameter("${'$'}top", it) }
              }.body()
            }

            public class Post internal constructor(
              private val client: HttpClient,
              private val id: String,
            ) {
              public suspend operator fun invoke(fields: String? = "${'$'}type,color(${'$'}type,background,foreground,id),id,localizedName,name,ordinal", body: BundleElementWrite.EnumBundleElement? = null): BundleElementRead.EnumBundleElement = client.post("/admin/customFieldSettings/bundles/enum/$id/values") {
                fields?.let { parameter("fields", it) }
                body?.let {
                  contentType(ContentType.Application.Json)
                  setBody(it)
                }
              }.body()
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
                  client.delete("/admin/customFieldSettings/bundles/enum/$id/values/$enumBundleElementId")
                }
              }

              public class Get internal constructor(
                private val client: HttpClient,
                private val id: String,
                private val enumBundleElementId: String,
              ) {
                public suspend operator fun invoke(fields: String? = "${'$'}type,color(${'$'}type,background,foreground,id),id,localizedName,name,ordinal"): BundleElementRead.EnumBundleElement = client.get("/admin/customFieldSettings/bundles/enum/$id/values/$enumBundleElementId") {
                  fields?.let { parameter("fields", it) }
                }.body()
              }

              public class Post internal constructor(
                private val client: HttpClient,
                private val id: String,
                private val enumBundleElementId: String,
              ) {
                public suspend operator fun invoke(fields: String? = "${'$'}type,color(${'$'}type,background,foreground,id),id,localizedName,name,ordinal", body: BundleElementWrite.EnumBundleElement? = null): BundleElementRead.EnumBundleElement = client.post("/admin/customFieldSettings/bundles/enum/$id/values/$enumBundleElementId") {
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
          ): List<BundleRead.OwnedBundle> = client.get("/admin/customFieldSettings/bundles/ownedField") {
            fields?.let { parameter("fields", it) }
            skip?.let { parameter("${'$'}skip", it) }
            top?.let { parameter("${'$'}top", it) }
          }.body()
        }

        public class Post internal constructor(
          private val client: HttpClient,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,id", body: BundleWrite.OwnedBundle? = null): BundleRead.OwnedBundle = client.post("/admin/customFieldSettings/bundles/ownedField") {
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

          public val values: Values = Values(client, id)

          public class Delete internal constructor(
            private val client: HttpClient,
            private val id: String,
          ) {
            public suspend operator fun invoke() {
              client.delete("/admin/customFieldSettings/bundles/ownedField/$id")
            }
          }

          public class Get internal constructor(
            private val client: HttpClient,
            private val id: String,
          ) {
            public suspend operator fun invoke(fields: String? = "${'$'}type,id"): BundleRead.OwnedBundle = client.get("/admin/customFieldSettings/bundles/ownedField/$id") {
              fields?.let { parameter("fields", it) }
            }.body()
          }

          public class Post internal constructor(
            private val client: HttpClient,
            private val id: String,
          ) {
            public suspend operator fun invoke(fields: String? = "${'$'}type,id", body: BundleWrite.OwnedBundle? = null): BundleRead.OwnedBundle = client.post("/admin/customFieldSettings/bundles/ownedField/$id") {
              fields?.let { parameter("fields", it) }
              body?.let {
                contentType(ContentType.Application.Json)
                setBody(it)
              }
            }.body()
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
              ): List<BundleElementRead.OwnedBundleElement> = client.get("/admin/customFieldSettings/bundles/ownedField/$id/values") {
                fields?.let { parameter("fields", it) }
                skip?.let { parameter("${'$'}skip", it) }
                top?.let { parameter("${'$'}top", it) }
              }.body()
            }

            public class Post internal constructor(
              private val client: HttpClient,
              private val id: String,
            ) {
              public suspend operator fun invoke(fields: String? = "${'$'}type,color(${'$'}type,background,foreground,id),id,name,ordinal,owner(${'$'}type,id,login,ringId)", body: BundleElementWrite.OwnedBundleElement? = null): BundleElementRead.OwnedBundleElement = client.post("/admin/customFieldSettings/bundles/ownedField/$id/values") {
                fields?.let { parameter("fields", it) }
                body?.let {
                  contentType(ContentType.Application.Json)
                  setBody(it)
                }
              }.body()
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
                  client.delete("/admin/customFieldSettings/bundles/ownedField/$id/values/$ownedBundleElementId")
                }
              }

              public class Get internal constructor(
                private val client: HttpClient,
                private val id: String,
                private val ownedBundleElementId: String,
              ) {
                public suspend operator fun invoke(fields: String? = "${'$'}type,color(${'$'}type,background,foreground,id),id,name,ordinal,owner(${'$'}type,id,login,ringId)"): BundleElementRead.OwnedBundleElement = client.get("/admin/customFieldSettings/bundles/ownedField/$id/values/$ownedBundleElementId") {
                  fields?.let { parameter("fields", it) }
                }.body()
              }

              public class Post internal constructor(
                private val client: HttpClient,
                private val id: String,
                private val ownedBundleElementId: String,
              ) {
                public suspend operator fun invoke(fields: String? = "${'$'}type,color(${'$'}type,background,foreground,id),id,name,ordinal,owner(${'$'}type,id,login,ringId)", body: BundleElementWrite.OwnedBundleElement? = null): BundleElementRead.OwnedBundleElement = client.post("/admin/customFieldSettings/bundles/ownedField/$id/values/$ownedBundleElementId") {
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
          ): List<BundleRead.StateBundle> = client.get("/admin/customFieldSettings/bundles/state") {
            fields?.let { parameter("fields", it) }
            skip?.let { parameter("${'$'}skip", it) }
            top?.let { parameter("${'$'}top", it) }
          }.body()
        }

        public class Post internal constructor(
          private val client: HttpClient,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,id", body: BundleWrite.StateBundle? = null): BundleRead.StateBundle = client.post("/admin/customFieldSettings/bundles/state") {
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

          public val values: Values = Values(client, id)

          public class Delete internal constructor(
            private val client: HttpClient,
            private val id: String,
          ) {
            public suspend operator fun invoke() {
              client.delete("/admin/customFieldSettings/bundles/state/$id")
            }
          }

          public class Get internal constructor(
            private val client: HttpClient,
            private val id: String,
          ) {
            public suspend operator fun invoke(fields: String? = "${'$'}type,id"): BundleRead.StateBundle = client.get("/admin/customFieldSettings/bundles/state/$id") {
              fields?.let { parameter("fields", it) }
            }.body()
          }

          public class Post internal constructor(
            private val client: HttpClient,
            private val id: String,
          ) {
            public suspend operator fun invoke(fields: String? = "${'$'}type,id", body: BundleWrite.StateBundle? = null): BundleRead.StateBundle = client.post("/admin/customFieldSettings/bundles/state/$id") {
              fields?.let { parameter("fields", it) }
              body?.let {
                contentType(ContentType.Application.Json)
                setBody(it)
              }
            }.body()
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
              ): List<BundleElementRead.StateBundleElement> = client.get("/admin/customFieldSettings/bundles/state/$id/values") {
                fields?.let { parameter("fields", it) }
                skip?.let { parameter("${'$'}skip", it) }
                top?.let { parameter("${'$'}top", it) }
              }.body()
            }

            public class Post internal constructor(
              private val client: HttpClient,
              private val id: String,
            ) {
              public suspend operator fun invoke(fields: String? = "${'$'}type,color(${'$'}type,background,foreground,id),id,isResolved,localizedName,name,ordinal", body: BundleElementWrite.StateBundleElement? = null): BundleElementRead.StateBundleElement = client.post("/admin/customFieldSettings/bundles/state/$id/values") {
                fields?.let { parameter("fields", it) }
                body?.let {
                  contentType(ContentType.Application.Json)
                  setBody(it)
                }
              }.body()
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
                  client.delete("/admin/customFieldSettings/bundles/state/$id/values/$stateBundleElementId")
                }
              }

              public class Get internal constructor(
                private val client: HttpClient,
                private val id: String,
                private val stateBundleElementId: String,
              ) {
                public suspend operator fun invoke(fields: String? = "${'$'}type,color(${'$'}type,background,foreground,id),id,isResolved,localizedName,name,ordinal"): BundleElementRead.StateBundleElement = client.get("/admin/customFieldSettings/bundles/state/$id/values/$stateBundleElementId") {
                  fields?.let { parameter("fields", it) }
                }.body()
              }

              public class Post internal constructor(
                private val client: HttpClient,
                private val id: String,
                private val stateBundleElementId: String,
              ) {
                public suspend operator fun invoke(fields: String? = "${'$'}type,color(${'$'}type,background,foreground,id),id,isResolved,localizedName,name,ordinal", body: BundleElementWrite.StateBundleElement? = null): BundleElementRead.StateBundleElement = client.post("/admin/customFieldSettings/bundles/state/$id/values/$stateBundleElementId") {
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
          ): List<BundleRead.UserBundle> = client.get("/admin/customFieldSettings/bundles/user") {
            fields?.let { parameter("fields", it) }
            skip?.let { parameter("${'$'}skip", it) }
            top?.let { parameter("${'$'}top", it) }
          }.body()
        }

        public class Post internal constructor(
          private val client: HttpClient,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,id", body: BundleWrite.UserBundle? = null): BundleRead.UserBundle = client.post("/admin/customFieldSettings/bundles/user") {
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

          public val aggregatedUsers: AggregatedUsers = AggregatedUsers(client, id)

          public val groups: Groups = Groups(client, id)

          public val individuals: Individuals = Individuals(client, id)

          public class Delete internal constructor(
            private val client: HttpClient,
            private val id: String,
          ) {
            public suspend operator fun invoke() {
              client.delete("/admin/customFieldSettings/bundles/user/$id")
            }
          }

          public class Get internal constructor(
            private val client: HttpClient,
            private val id: String,
          ) {
            public suspend operator fun invoke(fields: String? = "${'$'}type,id"): BundleRead.UserBundle = client.get("/admin/customFieldSettings/bundles/user/$id") {
              fields?.let { parameter("fields", it) }
            }.body()
          }

          public class Post internal constructor(
            private val client: HttpClient,
            private val id: String,
          ) {
            public suspend operator fun invoke(fields: String? = "${'$'}type,id", body: BundleWrite.UserBundle? = null): BundleRead.UserBundle = client.post("/admin/customFieldSettings/bundles/user/$id") {
              fields?.let { parameter("fields", it) }
              body?.let {
                contentType(ContentType.Application.Json)
                setBody(it)
              }
            }.body()
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
              ): List<UserRead> = client.get("/admin/customFieldSettings/bundles/user/$id/aggregatedUsers") {
                fields?.let { parameter("fields", it) }
                skip?.let { parameter("${'$'}skip", it) }
                top?.let { parameter("${'$'}top", it) }
              }.body()
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
              ): List<UserGroupRead> = client.get("/admin/customFieldSettings/bundles/user/$id/groups") {
                fields?.let { parameter("fields", it) }
                skip?.let { parameter("${'$'}skip", it) }
                top?.let { parameter("${'$'}top", it) }
              }.body()
            }

            public class Post internal constructor(
              private val client: HttpClient,
              private val id: String,
            ) {
              public suspend operator fun invoke(fields: String? = "${'$'}type,allUsersGroup,id,name,ringId", body: UserGroupWrite? = null): UserGroupRead = client.post("/admin/customFieldSettings/bundles/user/$id/groups") {
                fields?.let { parameter("fields", it) }
                body?.let {
                  contentType(ContentType.Application.Json)
                  setBody(it)
                }
              }.body()
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
                  client.delete("/admin/customFieldSettings/bundles/user/$id/groups/$userGroupId")
                }
              }

              public class Get internal constructor(
                private val client: HttpClient,
                private val id: String,
                private val userGroupId: String,
              ) {
                public suspend operator fun invoke(fields: String? = "${'$'}type,allUsersGroup,id,name,ringId"): UserGroupRead = client.get("/admin/customFieldSettings/bundles/user/$id/groups/$userGroupId") {
                  fields?.let { parameter("fields", it) }
                }.body()
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
              ): List<UserRead> = client.get("/admin/customFieldSettings/bundles/user/$id/individuals") {
                fields?.let { parameter("fields", it) }
                skip?.let { parameter("${'$'}skip", it) }
                top?.let { parameter("${'$'}top", it) }
              }.body()
            }

            public class Post internal constructor(
              private val client: HttpClient,
              private val id: String,
            ) {
              public suspend operator fun invoke(fields: String? = "${'$'}type,banned,email,fullName,guest,id,login,ringId", body: UserWrite? = null): UserRead = client.post("/admin/customFieldSettings/bundles/user/$id/individuals") {
                fields?.let { parameter("fields", it) }
                body?.let {
                  contentType(ContentType.Application.Json)
                  setBody(it)
                }
              }.body()
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
                  client.delete("/admin/customFieldSettings/bundles/user/$id/individuals/$userId")
                }
              }

              public class Get internal constructor(
                private val client: HttpClient,
                private val id: String,
                private val userId: String,
              ) {
                public suspend operator fun invoke(fields: String? = "${'$'}type,banned,email,fullName,guest,id,login,ringId"): UserRead = client.get("/admin/customFieldSettings/bundles/user/$id/individuals/$userId") {
                  fields?.let { parameter("fields", it) }
                }.body()
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
          ): List<BundleRead.VersionBundle> = client.get("/admin/customFieldSettings/bundles/version") {
            fields?.let { parameter("fields", it) }
            skip?.let { parameter("${'$'}skip", it) }
            top?.let { parameter("${'$'}top", it) }
          }.body()
        }

        public class Post internal constructor(
          private val client: HttpClient,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,id", body: BundleWrite.VersionBundle? = null): BundleRead.VersionBundle = client.post("/admin/customFieldSettings/bundles/version") {
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

          public val values: Values = Values(client, id)

          public class Delete internal constructor(
            private val client: HttpClient,
            private val id: String,
          ) {
            public suspend operator fun invoke() {
              client.delete("/admin/customFieldSettings/bundles/version/$id")
            }
          }

          public class Get internal constructor(
            private val client: HttpClient,
            private val id: String,
          ) {
            public suspend operator fun invoke(fields: String? = "${'$'}type,id"): BundleRead.VersionBundle = client.get("/admin/customFieldSettings/bundles/version/$id") {
              fields?.let { parameter("fields", it) }
            }.body()
          }

          public class Post internal constructor(
            private val client: HttpClient,
            private val id: String,
          ) {
            public suspend operator fun invoke(fields: String? = "${'$'}type,id", body: BundleWrite.VersionBundle? = null): BundleRead.VersionBundle = client.post("/admin/customFieldSettings/bundles/version/$id") {
              fields?.let { parameter("fields", it) }
              body?.let {
                contentType(ContentType.Application.Json)
                setBody(it)
              }
            }.body()
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
              ): List<BundleElementRead.VersionBundleElement> = client.get("/admin/customFieldSettings/bundles/version/$id/values") {
                fields?.let { parameter("fields", it) }
                skip?.let { parameter("${'$'}skip", it) }
                top?.let { parameter("${'$'}top", it) }
              }.body()
            }

            public class Post internal constructor(
              private val client: HttpClient,
              private val id: String,
            ) {
              public suspend operator fun invoke(fields: String? = "${'$'}type,archived,color(${'$'}type,background,foreground,id),id,name,ordinal,releaseDate,released", body: BundleElementWrite.VersionBundleElement? = null): BundleElementRead.VersionBundleElement = client.post("/admin/customFieldSettings/bundles/version/$id/values") {
                fields?.let { parameter("fields", it) }
                body?.let {
                  contentType(ContentType.Application.Json)
                  setBody(it)
                }
              }.body()
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
                  client.delete("/admin/customFieldSettings/bundles/version/$id/values/$versionBundleElementId")
                }
              }

              public class Get internal constructor(
                private val client: HttpClient,
                private val id: String,
                private val versionBundleElementId: String,
              ) {
                public suspend operator fun invoke(fields: String? = "${'$'}type,archived,color(${'$'}type,background,foreground,id),id,name,ordinal,releaseDate,released"): BundleElementRead.VersionBundleElement = client.get("/admin/customFieldSettings/bundles/version/$id/values/$versionBundleElementId") {
                  fields?.let { parameter("fields", it) }
                }.body()
              }

              public class Post internal constructor(
                private val client: HttpClient,
                private val id: String,
                private val versionBundleElementId: String,
              ) {
                public suspend operator fun invoke(fields: String? = "${'$'}type,archived,color(${'$'}type,background,foreground,id),id,name,ordinal,releaseDate,released", body: BundleElementWrite.VersionBundleElement? = null): BundleElementRead.VersionBundleElement = client.post("/admin/customFieldSettings/bundles/version/$id/values/$versionBundleElementId") {
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
        ): List<CustomFieldRead> = client.get("/admin/customFieldSettings/customFields") {
          fields?.let { parameter("fields", it) }
          skip?.let { parameter("${'$'}skip", it) }
          top?.let { parameter("${'$'}top", it) }
        }.body()
      }

      public class Post internal constructor(
        private val client: HttpClient,
      ) {
        public suspend operator fun invoke(fields: String? = "${'$'}type,fieldType(${'$'}type,id),id,isAutoAttached,isUpdateable,localizedName,name,ordinal", body: CustomFieldWrite? = null): CustomFieldRead = client.post("/admin/customFieldSettings/customFields") {
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

        public val fieldDefaults: FieldDefaults = FieldDefaults(client, id)

        public val instances: Instances = Instances(client, id)

        public class Delete internal constructor(
          private val client: HttpClient,
          private val id: String,
        ) {
          public suspend operator fun invoke() {
            client.delete("/admin/customFieldSettings/customFields/$id")
          }
        }

        public class Get internal constructor(
          private val client: HttpClient,
          private val id: String,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,fieldType(${'$'}type,id),id,isAutoAttached,isUpdateable,localizedName,name,ordinal"): CustomFieldRead = client.get("/admin/customFieldSettings/customFields/$id") {
            fields?.let { parameter("fields", it) }
          }.body()
        }

        public class Post internal constructor(
          private val client: HttpClient,
          private val id: String,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,fieldType(${'$'}type,id),id,isAutoAttached,isUpdateable,localizedName,name,ordinal", body: CustomFieldWrite? = null): CustomFieldRead = client.post("/admin/customFieldSettings/customFields/$id") {
            fields?.let { parameter("fields", it) }
            body?.let {
              contentType(ContentType.Application.Json)
              setBody(it)
            }
          }.body()
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
            public suspend operator fun invoke(fields: String? = "${'$'}type,bundle(${'$'}type,id),canBeEmpty,defaultValues(${'$'}type,id,name),emptyFieldText,id,isPublic"): CustomFieldDefaultsRead = client.get("/admin/customFieldSettings/customFields/$id/fieldDefaults") {
              fields?.let { parameter("fields", it) }
            }.body()
          }

          public class Post internal constructor(
            private val client: HttpClient,
            private val id: String,
          ) {
            public suspend operator fun invoke(fields: String? = "${'$'}type,bundle(${'$'}type,id),canBeEmpty,defaultValues(${'$'}type,id,name),emptyFieldText,id,isPublic", body: CustomFieldDefaultsWrite? = null): CustomFieldDefaultsRead = client.post("/admin/customFieldSettings/customFields/$id/fieldDefaults") {
              fields?.let { parameter("fields", it) }
              body?.let {
                contentType(ContentType.Application.Json)
                setBody(it)
              }
            }.body()
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
            ): List<ProjectCustomFieldRead> = client.get("/admin/customFieldSettings/customFields/$id/instances") {
              fields?.let { parameter("fields", it) }
              skip?.let { parameter("${'$'}skip", it) }
              top?.let { parameter("${'$'}top", it) }
            }.body()
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
        ): List<FieldTypeRead> = client.get("/admin/customFieldSettings/types") {
          fields?.let { parameter("fields", it) }
          skip?.let { parameter("${'$'}skip", it) }
          top?.let { parameter("${'$'}top", it) }
        }.body()
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
        ): List<BackupFile> = client.get("/admin/databaseBackup/backups") {
          fields?.let { parameter("fields", it) }
          skip?.let { parameter("${'$'}skip", it) }
          top?.let { parameter("${'$'}top", it) }
        }.body()
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
          public suspend operator fun invoke(fields: String? = "${'$'}type,creationDate,id,link,name,size"): BackupFile = client.get("/admin/databaseBackup/backups/$id") {
            fields?.let { parameter("fields", it) }
          }.body()
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
        public suspend operator fun invoke(fields: String? = "${'$'}type,availableDiskSpace,backupStatus(${'$'}type,backupCancelled,backupError(${'$'}type,date,errorMessage,id),backupInProgress,id),id,isOn,location"): DatabaseBackupSettingsRead = client.get("/admin/databaseBackup/settings") {
          fields?.let { parameter("fields", it) }
        }.body()
      }

      public class Post internal constructor(
        private val client: HttpClient,
      ) {
        public suspend operator fun invoke(fields: String? = "${'$'}type,availableDiskSpace,backupStatus(${'$'}type,backupCancelled,backupError(${'$'}type,date,errorMessage,id),backupInProgress,id),id,isOn,location", body: DatabaseBackupSettingsWrite? = null): DatabaseBackupSettingsRead = client.post("/admin/databaseBackup/settings") {
          fields?.let { parameter("fields", it) }
          body?.let {
            contentType(ContentType.Application.Json)
            setBody(it)
          }
        }.body()
      }

      public class BackupStatus internal constructor(
        private val client: HttpClient,
      ) {
        public val `get`: Get = Get(client)

        public class Get internal constructor(
          private val client: HttpClient,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,backupCancelled,backupError(${'$'}type,date,errorMessage,id),backupInProgress,id"): io.youtrack.model.BackupStatus = client.get("/admin/databaseBackup/settings/backupStatus") {
            fields?.let { parameter("fields", it) }
          }.body()
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
      public suspend operator fun invoke(fields: String? = "${'$'}type,appearanceSettings(${'$'}type,dateFieldFormat(${'$'}type,datePattern,id,pattern,presentation),id,timeZone(${'$'}type,id,offset,presentation)),id,license(${'$'}type,error,id,license,username),localeSettings(${'$'}type,id,isRTL,locale(${'$'}type,community,id,language,locale,name)),notificationSettings(${'$'}type,emailSettings(${'$'}type,id,isEnabled),id),restSettings(${'$'}type,allowAllOrigins,allowedOrigins,id),systemSettings(${'$'}type,baseUrl,id,isApplicationReadOnly,maxExportItems,maxUploadFileSize)"): GlobalSettingsRead = client.get("/admin/globalSettings") {
        fields?.let { parameter("fields", it) }
      }.body()
    }

    public class Post internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(fields: String? = "${'$'}type,appearanceSettings(${'$'}type,dateFieldFormat(${'$'}type,datePattern,id,pattern,presentation),id,timeZone(${'$'}type,id,offset,presentation)),id,license(${'$'}type,error,id,license,username),localeSettings(${'$'}type,id,isRTL,locale(${'$'}type,community,id,language,locale,name)),notificationSettings(${'$'}type,emailSettings(${'$'}type,id,isEnabled),id),restSettings(${'$'}type,allowAllOrigins,allowedOrigins,id),systemSettings(${'$'}type,baseUrl,id,isApplicationReadOnly,maxExportItems,maxUploadFileSize)", body: GlobalSettingsWrite? = null): GlobalSettingsRead = client.post("/admin/globalSettings") {
        fields?.let { parameter("fields", it) }
        body?.let {
          contentType(ContentType.Application.Json)
          setBody(it)
        }
      }.body()
    }

    public class AppearanceSettings internal constructor(
      private val client: HttpClient,
    ) {
      public val `get`: Get = Get(client)

      public val post: Post = Post(client)

      public class Get internal constructor(
        private val client: HttpClient,
      ) {
        public suspend operator fun invoke(fields: String? = "${'$'}type,dateFieldFormat(${'$'}type,datePattern,id,pattern,presentation),id,timeZone(${'$'}type,id,offset,presentation)"): AppearanceSettingsRead = client.get("/admin/globalSettings/appearanceSettings") {
          fields?.let { parameter("fields", it) }
        }.body()
      }

      public class Post internal constructor(
        private val client: HttpClient,
      ) {
        public suspend operator fun invoke(fields: String? = "${'$'}type,dateFieldFormat(${'$'}type,datePattern,id,pattern,presentation),id,timeZone(${'$'}type,id,offset,presentation)", body: AppearanceSettingsWrite? = null): AppearanceSettingsRead = client.post("/admin/globalSettings/appearanceSettings") {
          fields?.let { parameter("fields", it) }
          body?.let {
            contentType(ContentType.Application.Json)
            setBody(it)
          }
        }.body()
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
        public suspend operator fun invoke(fields: String? = "${'$'}type,error,id,license,username"): LicenseRead = client.get("/admin/globalSettings/license") {
          fields?.let { parameter("fields", it) }
        }.body()
      }

      public class Post internal constructor(
        private val client: HttpClient,
      ) {
        public suspend operator fun invoke(fields: String? = "${'$'}type,error,id,license,username", body: LicenseWrite? = null): LicenseRead = client.post("/admin/globalSettings/license") {
          fields?.let { parameter("fields", it) }
          body?.let {
            contentType(ContentType.Application.Json)
            setBody(it)
          }
        }.body()
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
        public suspend operator fun invoke(fields: String? = "${'$'}type,id,isRTL,locale(${'$'}type,community,id,language,locale,name)"): LocaleSettingsRead = client.get("/admin/globalSettings/localeSettings") {
          fields?.let { parameter("fields", it) }
        }.body()
      }

      public class Post internal constructor(
        private val client: HttpClient,
      ) {
        public suspend operator fun invoke(fields: String? = "${'$'}type,id,isRTL,locale(${'$'}type,community,id,language,locale,name)", body: LocaleSettingsWrite? = null): LocaleSettingsRead = client.post("/admin/globalSettings/localeSettings") {
          fields?.let { parameter("fields", it) }
          body?.let {
            contentType(ContentType.Application.Json)
            setBody(it)
          }
        }.body()
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
        public suspend operator fun invoke(fields: String? = "${'$'}type,emailSettings(${'$'}type,id,isEnabled),id"): NotificationSettingsRead = client.get("/admin/globalSettings/notificationSettings") {
          fields?.let { parameter("fields", it) }
        }.body()
      }

      public class Post internal constructor(
        private val client: HttpClient,
      ) {
        public suspend operator fun invoke(fields: String? = "${'$'}type,emailSettings(${'$'}type,id,isEnabled),id", body: NotificationSettingsWrite? = null): NotificationSettingsRead = client.post("/admin/globalSettings/notificationSettings") {
          fields?.let { parameter("fields", it) }
          body?.let {
            contentType(ContentType.Application.Json)
            setBody(it)
          }
        }.body()
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
        public suspend operator fun invoke(fields: String? = "${'$'}type,allowAllOrigins,allowedOrigins,id"): RestCorsSettingsRead = client.get("/admin/globalSettings/restSettings") {
          fields?.let { parameter("fields", it) }
        }.body()
      }

      public class Post internal constructor(
        private val client: HttpClient,
      ) {
        public suspend operator fun invoke(fields: String? = "${'$'}type,allowAllOrigins,allowedOrigins,id", body: RestCorsSettingsWrite? = null): RestCorsSettingsRead = client.post("/admin/globalSettings/restSettings") {
          fields?.let { parameter("fields", it) }
          body?.let {
            contentType(ContentType.Application.Json)
            setBody(it)
          }
        }.body()
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
        public suspend operator fun invoke(fields: String? = "${'$'}type,baseUrl,id,isApplicationReadOnly,maxExportItems,maxUploadFileSize"): SystemSettingsRead = client.get("/admin/globalSettings/systemSettings") {
          fields?.let { parameter("fields", it) }
        }.body()
      }

      public class Post internal constructor(
        private val client: HttpClient,
      ) {
        public suspend operator fun invoke(fields: String? = "${'$'}type,baseUrl,id,isApplicationReadOnly,maxExportItems,maxUploadFileSize", body: SystemSettingsWrite? = null): SystemSettingsRead = client.post("/admin/globalSettings/systemSettings") {
          fields?.let { parameter("fields", it) }
          body?.let {
            contentType(ContentType.Application.Json)
            setBody(it)
          }
        }.body()
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
      ): List<IssueFolderRead.Project> = client.get("/admin/projects") {
        fields?.let { parameter("fields", it) }
        skip?.let { parameter("${'$'}skip", it) }
        top?.let { parameter("${'$'}top", it) }
      }.body()
    }

    public class Post internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(
        template: String? = null,
        fields: String? = "${'$'}type,archived,customFields,id,leader(${'$'}type,id,login,ringId),name,shortName",
        body: IssueFolderWrite.Project? = null,
      ): IssueFolderRead.Project = client.post("/admin/projects") {
        template?.let { parameter("template", it) }
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

      public val articles: Articles = Articles(client, id)

      public val customFields: CustomFields = CustomFields(client, id)

      public val issues: Issues = Issues(client, id)

      public val timeTrackingSettings: TimeTrackingSettings = TimeTrackingSettings(client, id)

      public class Delete internal constructor(
        private val client: HttpClient,
        private val id: String,
      ) {
        public suspend operator fun invoke() {
          client.delete("/admin/projects/$id")
        }
      }

      public class Get internal constructor(
        private val client: HttpClient,
        private val id: String,
      ) {
        public suspend operator fun invoke(fields: String? = "${'$'}type,archived,customFields,id,leader(${'$'}type,id,login,ringId),name,shortName"): IssueFolderRead.Project = client.get("/admin/projects/$id") {
          fields?.let { parameter("fields", it) }
        }.body()
      }

      public class Post internal constructor(
        private val client: HttpClient,
        private val id: String,
      ) {
        public suspend operator fun invoke(fields: String? = "${'$'}type,archived,customFields,id,leader(${'$'}type,id,login,ringId),name,shortName", body: IssueFolderWrite.Project? = null): IssueFolderRead.Project = client.post("/admin/projects/$id") {
          fields?.let { parameter("fields", it) }
          body?.let {
            contentType(ContentType.Application.Json)
            setBody(it)
          }
        }.body()
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
          ): List<BaseArticleRead.Article> = client.get("/admin/projects/$id/articles") {
            fields?.let { parameter("fields", it) }
            skip?.let { parameter("${'$'}skip", it) }
            top?.let { parameter("${'$'}top", it) }
          }.body()
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
          ): List<ProjectCustomFieldRead> = client.get("/admin/projects/$id/customFields") {
            fields?.let { parameter("fields", it) }
            skip?.let { parameter("${'$'}skip", it) }
            top?.let { parameter("${'$'}top", it) }
          }.body()
        }

        public class Post internal constructor(
          private val client: HttpClient,
          private val id: String,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,bundle(${'$'}type,id),canBeEmpty,defaultValues(${'$'}type,id,name),emptyFieldText,field(${'$'}type,fieldType(${'$'}type,id),id,localizedName,name),id,isPublic,ordinal", body: ProjectCustomFieldWrite? = null): ProjectCustomFieldRead = client.post("/admin/projects/$id/customFields") {
            fields?.let { parameter("fields", it) }
            body?.let {
              contentType(ContentType.Application.Json)
              setBody(it)
            }
          }.body()
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
              client.delete("/admin/projects/$id/customFields/$projectCustomFieldId")
            }
          }

          public class Get internal constructor(
            private val client: HttpClient,
            private val id: String,
            private val projectCustomFieldId: String,
          ) {
            public suspend operator fun invoke(fields: String? = "${'$'}type,bundle(${'$'}type,id),canBeEmpty,defaultValues(${'$'}type,id,name),emptyFieldText,field(${'$'}type,fieldType(${'$'}type,id),id,localizedName,name),id,isPublic,ordinal"): ProjectCustomFieldRead = client.get("/admin/projects/$id/customFields/$projectCustomFieldId") {
              fields?.let { parameter("fields", it) }
            }.body()
          }

          public class Post internal constructor(
            private val client: HttpClient,
            private val id: String,
            private val projectCustomFieldId: String,
          ) {
            public suspend operator fun invoke(fields: String? = "${'$'}type,bundle(${'$'}type,id),canBeEmpty,defaultValues(${'$'}type,id,name),emptyFieldText,field(${'$'}type,fieldType(${'$'}type,id),id,localizedName,name),id,isPublic,ordinal", body: ProjectCustomFieldWrite? = null): ProjectCustomFieldRead = client.post("/admin/projects/$id/customFields/$projectCustomFieldId") {
              fields?.let { parameter("fields", it) }
              body?.let {
                contentType(ContentType.Application.Json)
                setBody(it)
              }
            }.body()
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
          ): List<IssueRead> = client.get("/admin/projects/$id/issues") {
            customFields?.let { parameter("customFields", it) }
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
            fields: String? = "${'$'}type,created,customFields(${'$'}type,id,name,value(${'$'}type,id,name)),description,id,idReadable,links(${'$'}type,direction,id,linkType(${'$'}type,id,localizedName,name)),numberInProject,project(${'$'}type,id,name,shortName),reporter(${'$'}type,id,login,ringId),resolved,summary,updated,updater(${'$'}type,id,login,ringId),visibility(${'$'}type,id,permittedGroups(${'$'}type,id,name,ringId),permittedUsers(${'$'}type,id,login,ringId))",
            body: IssueWrite? = null,
          ): IssueRead = client.post("/admin/projects/$id/issues") {
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
              client.delete("/admin/projects/$id/issues/$issueId")
            }
          }

          public class Get internal constructor(
            private val client: HttpClient,
            private val id: String,
            private val issueId: String,
          ) {
            public suspend operator fun invoke(fields: String? = "${'$'}type,created,customFields(${'$'}type,id,name,value(${'$'}type,id,name)),description,id,idReadable,links(${'$'}type,direction,id,linkType(${'$'}type,id,localizedName,name)),numberInProject,project(${'$'}type,id,name,shortName),reporter(${'$'}type,id,login,ringId),resolved,summary,updated,updater(${'$'}type,id,login,ringId),visibility(${'$'}type,id,permittedGroups(${'$'}type,id,name,ringId),permittedUsers(${'$'}type,id,login,ringId))"): IssueRead = client.get("/admin/projects/$id/issues/$issueId") {
              fields?.let { parameter("fields", it) }
            }.body()
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
            ): IssueRead = client.post("/admin/projects/$id/issues/$issueId") {
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
          public suspend operator fun invoke(fields: String? = "${'$'}type,enabled,estimate(${'$'}type,field(${'$'}type,fieldType(${'$'}type,id),id,localizedName,name),id),id,timeSpent(${'$'}type,field(${'$'}type,fieldType(${'$'}type,id),id,localizedName,name),id)"): ProjectTimeTrackingSettingsRead = client.get("/admin/projects/$id/timeTrackingSettings") {
            fields?.let { parameter("fields", it) }
          }.body()
        }

        public class Post internal constructor(
          private val client: HttpClient,
          private val id: String,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,enabled,estimate(${'$'}type,field(${'$'}type,fieldType(${'$'}type,id),id,localizedName,name),id),id,timeSpent(${'$'}type,field(${'$'}type,fieldType(${'$'}type,id),id,localizedName,name),id)", body: ProjectTimeTrackingSettingsWrite? = null): ProjectTimeTrackingSettingsRead = client.post("/admin/projects/$id/timeTrackingSettings") {
            fields?.let { parameter("fields", it) }
            body?.let {
              contentType(ContentType.Application.Json)
              setBody(it)
            }
          }.body()
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
            ): List<WorkItemTypeRead> = client.get("/admin/projects/$id/timeTrackingSettings/workItemTypes") {
              fields?.let { parameter("fields", it) }
              skip?.let { parameter("${'$'}skip", it) }
              top?.let { parameter("${'$'}top", it) }
            }.body()
          }

          public class Post internal constructor(
            private val client: HttpClient,
            private val id: String,
          ) {
            public suspend operator fun invoke(fields: String? = "${'$'}type,autoAttached,id,name", body: WorkItemTypeWrite? = null): WorkItemTypeRead = client.post("/admin/projects/$id/timeTrackingSettings/workItemTypes") {
              fields?.let { parameter("fields", it) }
              body?.let {
                contentType(ContentType.Application.Json)
                setBody(it)
              }
            }.body()
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
                client.delete("/admin/projects/$id/timeTrackingSettings/workItemTypes/$workItemTypeId")
              }
            }

            public class Get internal constructor(
              private val client: HttpClient,
              private val id: String,
              private val workItemTypeId: String,
            ) {
              public suspend operator fun invoke(fields: String? = "${'$'}type,autoAttached,id,name"): WorkItemTypeRead = client.get("/admin/projects/$id/timeTrackingSettings/workItemTypes/$workItemTypeId") {
                fields?.let { parameter("fields", it) }
              }.body()
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
      public suspend operator fun invoke(fields: String? = "${'$'}type,allocatedMemory,availableMemory,availableProcessors,blobStringsCacheHitRate,cachedResultsCountInDBQueriesCache,databaseBackgroundThreads,databaseLocation,databaseQueriesCacheHitRate,databaseSize,fullDatabaseSize,id,installationFolder,logsLocation,notificationAnalyzerThreads,onlineUsers(${'$'}type,id,users),pendingAsyncJobs,reportCalculatorThreads,requestsPerSecond,startedTime,textIndexSize,totalTransactions,transactionsPerSecond,uptime,usedMemory"): io.youtrack.model.Telemetry = client.get("/admin/telemetry") {
        fields?.let { parameter("fields", it) }
      }.body()
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
      public suspend operator fun invoke(fields: String? = "${'$'}type,id,workItemTypes(${'$'}type,id,name),workTimeSettings(${'$'}type,daysAWeek,firstDayOfWeek,id,minutesADay,workDays)"): GlobalTimeTrackingSettings = client.get("/admin/timeTrackingSettings") {
        fields?.let { parameter("fields", it) }
      }.body()
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
        ): List<WorkItemTypeRead> = client.get("/admin/timeTrackingSettings/workItemTypes") {
          fields?.let { parameter("fields", it) }
          skip?.let { parameter("${'$'}skip", it) }
          top?.let { parameter("${'$'}top", it) }
        }.body()
      }

      public class Post internal constructor(
        private val client: HttpClient,
      ) {
        public suspend operator fun invoke(fields: String? = "${'$'}type,autoAttached,id,name", body: WorkItemTypeWrite? = null): WorkItemTypeRead = client.post("/admin/timeTrackingSettings/workItemTypes") {
          fields?.let { parameter("fields", it) }
          body?.let {
            contentType(ContentType.Application.Json)
            setBody(it)
          }
        }.body()
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
            client.delete("/admin/timeTrackingSettings/workItemTypes/$workItemTypeId")
          }
        }

        public class Get internal constructor(
          private val client: HttpClient,
          private val workItemTypeId: String,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,autoAttached,id,name"): WorkItemTypeRead = client.get("/admin/timeTrackingSettings/workItemTypes/$workItemTypeId") {
            fields?.let { parameter("fields", it) }
          }.body()
        }

        public class Post internal constructor(
          private val client: HttpClient,
          private val workItemTypeId: String,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,autoAttached,id,name", body: WorkItemTypeWrite? = null): WorkItemTypeRead = client.post("/admin/timeTrackingSettings/workItemTypes/$workItemTypeId") {
            fields?.let { parameter("fields", it) }
            body?.let {
              contentType(ContentType.Application.Json)
              setBody(it)
            }
          }.body()
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
        public suspend operator fun invoke(fields: String? = "${'$'}type,daysAWeek,firstDayOfWeek,id,minutesADay,workDays"): WorkTimeSettingsRead = client.get("/admin/timeTrackingSettings/workTimeSettings") {
          fields?.let { parameter("fields", it) }
        }.body()
      }

      public class Post internal constructor(
        private val client: HttpClient,
      ) {
        public suspend operator fun invoke(fields: String? = "${'$'}type,daysAWeek,firstDayOfWeek,id,minutesADay,workDays", body: WorkTimeSettingsWrite? = null): WorkTimeSettingsRead = client.post("/admin/timeTrackingSettings/workTimeSettings") {
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
