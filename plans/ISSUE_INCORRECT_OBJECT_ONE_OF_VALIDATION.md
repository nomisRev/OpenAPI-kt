# Issue Incorrect Object OneOf Model Generation

```json
              "schema": {
                "type": "object",
                "properties": {
                  "name": {
                    "type": "string",
                    "description": "The name of the check. For example, \"code-coverage\"."
                  },
                  "head_sha": {
                    "type": "string",
                    "description": "The SHA of the commit."
                  },
                  "details_url": {
                    "type": "string",
                    "description": "The URL of the integrator's site that has the full details of the check. If the integrator does not provide this, then the homepage of the GitHub app is used."
                  },
                  "external_id": {
                    "type": "string",
                    "description": "A reference for the run on the integrator's system."
                  },
                  "status": {
                    "type": "string",
                    "description": "The current status of the check run. Only GitHub Actions can set a status of `waiting`, `pending`, or `requested`.",
                    "enum": [
                      "queued",
                      "in_progress",
                      "completed",
                      "waiting",
                      "requested",
                      "pending"
                    ],
                    "default": "queued"
                  },
                  "started_at": {
                    "type": "string",
                    "format": "date-time",
                    "description": "The time that the check run began. This is a timestamp in [ISO 8601](https://en.wikipedia.org/wiki/ISO_8601) format: `YYYY-MM-DDTHH:MM:SSZ`."
                  },
                  "conclusion": {
                    "type": "string",
                    "description": "**Required if you provide `completed_at` or a `status` of `completed`**. The final conclusion of the check. \n**Note:** Providing `conclusion` will automatically set the `status` parameter to `completed`. You cannot change a check run conclusion to `stale`, only GitHub can set this.",
                    "enum": [
                      "action_required",
                      "cancelled",
                      "failure",
                      "neutral",
                      "success",
                      "skipped",
                      "stale",
                      "timed_out"
                    ]
                  },
                  "completed_at": {
                    "type": "string",
                    "format": "date-time",
                    "description": "The time the check completed. This is a timestamp in [ISO 8601](https://en.wikipedia.org/wiki/ISO_8601) format: `YYYY-MM-DDTHH:MM:SSZ`."
                  },
                  "output": {
                    "type": "object",
                    "description": "Check runs can accept a variety of data in the `output` object, including a `title` and `summary` and can optionally provide descriptive details about the run.",
                    "properties": {
                      "title": {
                        "type": "string",
                        "description": "The title of the check run."
                      },
                      "summary": {
                        "type": "string",
                        "maxLength": 65535,
                        "description": "The summary of the check run. This parameter supports Markdown. **Maximum length**: 65535 characters."
                      },
                      "text": {
                        "type": "string",
                        "maxLength": 65535,
                        "description": "The details of the check run. This parameter supports Markdown. **Maximum length**: 65535 characters."
                      },
                      "annotations": {
                        "type": "array",
                        "description": "Adds information from your analysis to specific lines of code. Annotations are visible on GitHub in the **Checks** and **Files changed** tab of the pull request. The Checks API limits the number of annotations to a maximum of 50 per API request. To create more than 50 annotations, you have to make multiple requests to the [Update a check run](https://docs.github.com/rest/checks/runs#update-a-check-run) endpoint. Each time you update the check run, annotations are appended to the list of annotations that already exist for the check run. GitHub Actions are limited to 10 warning annotations and 10 error annotations per step. For details about how you can view annotations on GitHub, see \"[About status checks](https://docs.github.com/articles/about-status-checks#checks)\".",
                        "maxItems": 50,
                        "items": {
                          "type": "object",
                          "properties": {
                            "path": {
                              "type": "string",
                              "description": "The path of the file to add an annotation to. For example, `assets/css/main.css`."
                            },
                            "start_line": {
                              "type": "integer",
                              "description": "The start line of the annotation. Line numbers start at 1."
                            },
                            "end_line": {
                              "type": "integer",
                              "description": "The end line of the annotation."
                            },
                            "start_column": {
                              "type": "integer",
                              "description": "The start column of the annotation. Annotations only support `start_column` and `end_column` on the same line. Omit this parameter if `start_line` and `end_line` have different values. Column numbers start at 1."
                            },
                            "end_column": {
                              "type": "integer",
                              "description": "The end column of the annotation. Annotations only support `start_column` and `end_column` on the same line. Omit this parameter if `start_line` and `end_line` have different values."
                            },
                            "annotation_level": {
                              "type": "string",
                              "description": "The level of the annotation.",
                              "enum": [
                                "notice",
                                "warning",
                                "failure"
                              ]
                            },
                            "message": {
                              "type": "string",
                              "description": "A short description of the feedback for these lines of code. The maximum size is 64 KB."
                            },
                            "title": {
                              "type": "string",
                              "description": "The title that represents the annotation. The maximum size is 255 characters."
                            },
                            "raw_details": {
                              "type": "string",
                              "description": "Details about this annotation. The maximum size is 64 KB."
                            }
                          },
                          "required": [
                            "path",
                            "start_line",
                            "end_line",
                            "annotation_level",
                            "message"
                          ]
                        }
                      },
                      "images": {
                        "type": "array",
                        "description": "Adds images to the output displayed in the GitHub pull request UI.",
                        "items": {
                          "type": "object",
                          "properties": {
                            "alt": {
                              "type": "string",
                              "description": "The alternative text for the image."
                            },
                            "image_url": {
                              "type": "string",
                              "description": "The full URL of the image."
                            },
                            "caption": {
                              "type": "string",
                              "description": "A short image description."
                            }
                          },
                          "required": [
                            "alt",
                            "image_url"
                          ]
                        }
                      }
                    },
                    "required": [
                      "title",
                      "summary"
                    ]
                  },
                  "actions": {
                    "type": "array",
                    "description": "Displays a button on GitHub that can be clicked to alert your app to do additional tasks. For example, a code linting app can display a button that automatically fixes detected errors. The button created in this object is displayed after the check run completes. When a user clicks the button, GitHub sends the [`check_run.requested_action` webhook](https://docs.github.com/webhooks/event-payloads/#check_run) to your app. Each action includes a `label`, `identifier` and `description`. A maximum of three actions are accepted. To learn more about check runs and requested actions, see \"[Check runs and requested actions](https://docs.github.com/rest/guides/using-the-rest-api-to-interact-with-checks#check-runs-and-requested-actions).\"",
                    "maxItems": 3,
                    "items": {
                      "type": "object",
                      "properties": {
                        "label": {
                          "type": "string",
                          "maxLength": 20,
                          "description": "The text to be displayed on a button in the web UI. The maximum size is 20 characters."
                        },
                        "description": {
                          "type": "string",
                          "maxLength": 40,
                          "description": "A short explanation of what this action would do. The maximum size is 40 characters."
                        },
                        "identifier": {
                          "type": "string",
                          "maxLength": 20,
                          "description": "A reference for the action on the integrator's system. The maximum size is 20 characters."
                        }
                      },
                      "required": [
                        "label",
                        "description",
                        "identifier"
                      ]
                    }
                  }
                },
                "required": [
                  "name",
                  "head_sha"
                ],
                "discriminator": {
                  "propertyName": "status"
                },
                "oneOf": [
                  {
                    "properties": {
                      "status": {
                        "enum": [
                          "completed"
                        ]
                      }
                    },
                    "required": [
                      "status",
                      "conclusion"
                    ],
                    "additionalProperties": true
                  },
                  {
                    "properties": {
                      "status": {
                        "enum": [
                          "queued",
                          "in_progress"
                        ]
                      }
                    },
                    "additionalProperties": true
                  }
                ]
              }
```

currently

```kotlin
        public class Post internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(body: Body): CheckRun = client.post("/repos/$owner/$repo/check-runs") {
            contentType(ContentType.Application.Json)
            setBody(body)
          }.body()

          @Serializable(with = Body.Serializer::class)
          public sealed interface Body {
            @OptIn(ExperimentalSerializationApi::class)
            @KeepGeneratedSerializer
            @Serializable(with = Completed.Serializer::class)
            public data class Completed(
              public val status: Status,
              public val additional: JsonObject? = null,
            ) : Body {
              @Serializable
              public enum class Status(
                public val `value`: String,
              ) {
                @SerialName("completed")
                Completed("completed"),
                ;
              }

              public object Serializer : KSerializer<Completed> {
                override val descriptor: SerialDescriptor = generatedSerializer().descriptor

                override fun serialize(encoder: Encoder, `value`: Completed) {
                  val json = (encoder as JsonEncoder).json
                  val known = json.encodeToJsonElement(generatedSerializer(), value.copy(additional = null)) as JsonObject
                  val content = mutableMapOf<String, JsonElement>()
                  known.forEach { (key, jsonElement) ->
                    if (key != "additional") {
                      content[key] = jsonElement
                    }
                  }
                  value.additional?.forEach { (key, jsonElement) ->
                    content[key] = jsonElement
                  }
                  encoder.encodeSerializableValue(JsonObject.serializer(), JsonObject(content))
                }

                override fun deserialize(decoder: Decoder): Completed {
                  val json = (decoder as JsonDecoder).json
                  val element = decoder.decodeSerializableValue(JsonObject.serializer())
                  val knownNames = setOf("status")
                  val known = json.decodeFromJsonElement(generatedSerializer(), JsonObject(element.filterKeys { it in knownNames }))
                  val additional = JsonObject(element - knownNames).ifEmpty { null }
                  return known.copy(additional = additional)
                }
              }
            }

            @OptIn(ExperimentalSerializationApi::class)
            @KeepGeneratedSerializer
            @Serializable(with = Two.Serializer::class)
            public data class Two(
              public val status: Status? = null,
              public val additional: JsonObject? = null,
            ) : Body {
              @Serializable
              public enum class Status(
                public val `value`: String,
              ) {
                @SerialName("queued")
                Queued("queued"),
                @SerialName("in_progress")
                InProgress("in_progress"),
                ;
              }

              public object Serializer : KSerializer<Two> {
                override val descriptor: SerialDescriptor = generatedSerializer().descriptor

                override fun serialize(encoder: Encoder, `value`: Two) {
                  val json = (encoder as JsonEncoder).json
                  val known = json.encodeToJsonElement(generatedSerializer(), value.copy(additional = null)) as JsonObject
                  val content = mutableMapOf<String, JsonElement>()
                  known.forEach { (key, jsonElement) ->
                    if (key != "additional") {
                      content[key] = jsonElement
                    }
                  }
                  value.additional?.forEach { (key, jsonElement) ->
                    content[key] = jsonElement
                  }
                  encoder.encodeSerializableValue(JsonObject.serializer(), JsonObject(content))
                }

                override fun deserialize(decoder: Decoder): Two {
                  val json = (decoder as JsonDecoder).json
                  val element = decoder.decodeSerializableValue(JsonObject.serializer())
                  val knownNames = setOf("status")
                  val known = json.decodeFromJsonElement(generatedSerializer(), JsonObject(element.filterKeys { it in knownNames }))
                  val additional = JsonObject(element - knownNames).ifEmpty { null }
                  return known.copy(additional = additional)
                }
              }
            }

            public object Serializer : KSerializer<Body> {
              @OptIn(
                InternalSerializationApi::class,
                ExperimentalSerializationApi::class,
              )
              override val descriptor: SerialDescriptor =
                  buildSerialDescriptor("io.github.api.Repos.OwnerPath.RepoPath.CheckRuns.Post.Body", PolymorphicKind.SEALED) {
                element("Completed", Completed.serializer().descriptor)
                element("Two", Two.serializer().descriptor)
              }

              override fun deserialize(decoder: Decoder): Body {
                val value = decoder.decodeSerializableValue(JsonElement.serializer())
                val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                val obj = value as? JsonObject
                val tag = (obj?.get("status") as? JsonPrimitive)?.content
                when(tag) {
                  "completed" -> return json.decodeFromJsonElement(Completed.serializer(), value)
                  "queued", "in_progress" -> return json.decodeFromJsonElement(Two.serializer(), value)
                  else -> throw SerializationException("Unknown tag: " + tag + " for io.github.api.Repos.OwnerPath.RepoPath.CheckRuns.Post.Body")
                }
              }

              override fun serialize(encoder: Encoder, `value`: Body) {
                when(value) {
                  is Completed -> encoder.encodeSerializableValue(Completed.serializer(), value)
                  is Two -> encoder.encodeSerializableValue(Two.serializer(), value)
                }
              }
            }
          }
        }
```

Instead, should probably just be a regular data class which performs the validation inside `init { }` with `require`.

I.e.
```kotlin
init {
    require(status != Status.Completed || (status != null && conclusion != null)) { "When status == Completed " }
}
```

And in this case we need to flatten `additionalProperties` into the Schema.
We need to implement a proper "merge"/validation upon oneOf within object.
