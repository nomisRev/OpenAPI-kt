package io.github.nomisrev.openapi.routes

import io.github.nomisrev.openapi.Model

/**
 * The typed representation of an OpenAPI 3.2 sequential media type, i.e. a Media Type Object that
 * declares `itemSchema`. Where `schema` describes the complete content as one document, `itemSchema`
 * describes every individual item of a sequence (a stream), so the content can be consumed item by
 * item (e.g. as a `Flow`).
 *
 * The media type determines how the content is split into items, which is captured by the subtypes.
 */
sealed interface Stream {
    /** Every [Model] this stream refers to, used to collect nested and top-level models. */
    val models: List<Model>

    /** Applies [transform] to every [Model] of this stream, preserving its structure. */
    fun mapModels(transform: (Model) -> Model): Stream

    /**
     * A sequence of JSON texts, where each item is a JSON value described by [item].
     * Produced for `application/jsonl`, `application/x-ndjson`, `application/json-seq` and `+json-seq`.
     */
    data class JsonSequence(val framing: Framing, val item: Model) : Stream {
        override val models: List<Model> get() = listOf(item)
        override fun mapModels(transform: (Model) -> Model): Stream = copy(item = transform(item))

        enum class Framing {
            /**
             * `application/jsonl` and `application/x-ndjson`: one JSON text per line, separated by `\n`
             * (a trailing `\r` is tolerated). Blank lines carry no item.
             */
            NewlineDelimited,

            /**
             * `application/json-seq` and the `+json-seq` structured suffix (RFC 7464, RFC 8091): every JSON
             * text is preceded by a record separator (`0x1E`) and followed by `\n`.
             */
            RecordSeparated,
        }
    }

    /**
     * A `text/event-stream` (Server-Sent Events) stream. Every item is one dispatched event, carrying the
     * `event`, `data`, `id` and `retry` fields as defined by the SSE specification. OpenAPI 3.2 describes such
     * an event as an object with those properties; this splits that schema into the kinds of [events] it
     * permits.
     *
     * A `oneOf`/`anyOf` whose branches pin `event` (using `const`, a single-value `enum` or a
     * `discriminator` on `event`) results in one [Event] per event name, each with its own [Event.data].
     * Without such branches the stream has a single [Event] whose [Event.type] is [EventType.Unconstrained].
     *
     * Per the SSE specification an event without an `event:` field is dispatched with the type `message`,
     * so a kind [EventType.Named] `message` also matches events that omit the field.
     */
    data class ServerSentEvents(val events: List<Event>) : Stream {
        override val models: List<Model>
            get() = events.flatMap { it.models }

        override fun mapModels(transform: (Model) -> Model): Stream =
            copy(events = events.map { it.mapModels(transform) })

        /**
         * `true` when every event kind has a fixed name, so the schema only describes events with one of those
         * names. Events with any other name are not described by the API, and how to treat them (skip or fail)
         * is up to the consumer. `false` when at least one kind accepts any event name.
         */
        val isClosed: Boolean
            get() = events.all { it.type is EventType.Named }

        /**
         * One kind of event in the stream.
         *
         * @property id the schema of the `id` field, or `null` when the item schema does not declare it.
         * Note that per SSE the last event id persists across events, and the transport may send it anyway.
         * @property retry the schema of the `retry` field (reconnection time in milliseconds), or `null` when
         * the item schema does not declare it.
         */
        data class Event(
            val type: EventType,
            val data: EventData,
            val id: Model.Object.Property?,
            val retry: Model.Object.Property?,
        ) {
            val models: List<Model>
                get() = listOfNotNull(
                    (type as? EventType.Unconstrained)?.model,
                    data.model,
                    id?.model,
                    retry?.model,
                )

            fun mapModels(transform: (Model) -> Model): Event = copy(
                type = when (type) {
                    is EventType.Named -> type
                    is EventType.Unconstrained -> type.copy(model = transform(type.model))
                },
                data = when (data) {
                    is EventData.Json -> data.copy(model = transform(data.model))
                    is EventData.Text -> data.copy(model = transform(data.model))
                },
                id = id?.copy(model = transform(id.model)),
                retry = retry?.copy(model = transform(retry.model)),
            )
        }

        /** The `event` field (event type) of an [Event]. */
        sealed interface EventType {
            /** The event name is fixed to [name] (e.g. by `const: message`). */
            data class Named(val name: String) : EventType

            /**
             * Any event name is accepted, described by [model] (typically a string). When the field is absent,
             * SSE dispatches the event as `message`.
             */
            data class Unconstrained(val model: Model, val isRequired: Boolean) : EventType
        }

        /** The `data` field of an [Event]. SSE data is always text; this describes how to interpret it. */
        sealed interface EventData {
            val model: Model
            val isRequired: Boolean

            /**
             * The data is a JSON text, decoded into [model].
             *
             * @property contentMediaType the declared JSON `contentMediaType` (e.g. `application/json`), whose
             * `contentSchema` is [model] (or free-form JSON when no `contentSchema` is given). `null` when the
             * spec did not declare a `contentMediaType` but described `data` with a non-string schema directly,
             * which can only be satisfied by JSON-decoding the data.
             */
            data class Json(
                override val model: Model,
                val contentMediaType: String?,
                override val isRequired: Boolean,
            ) : EventData

            /**
             * The data is used as raw text, described by [model] (a string model, possibly an enum or with
             * constraints).
             *
             * @property contentMediaType a declared non-JSON `contentMediaType` (e.g. `text/plain`), if any.
             */
            data class Text(
                override val model: Model,
                val contentMediaType: String?,
                override val isRequired: Boolean,
            ) : EventData
        }
    }

    /**
     * An `itemSchema` on a media type whose item framing is not (yet) supported, for example `multipart/mixed`.
     * The [item] model is still resolved so no information is lost.
     */
    data class Unsupported(val item: Model) : Stream {
        override val models: List<Model> get() = listOf(item)
        override fun mapModels(transform: (Model) -> Model): Stream = copy(item = transform(item))
    }
}
