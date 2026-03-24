package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.ClassName
import io.github.nomisrev.openapi.parser.Server

/**
 * Describes the server configuration strategy derived from the server list:
 * - [NoServers]: no servers declared -> use a `baseUrl: String` parameter.
 * - [SingleFixed]: exactly one server with no variables -> no server type is generated.
 * - [SingleVariable]: exactly one server with variables -> produce a standalone data class (no
 *   sealed interface; the data class exposes a `url` property).
 * - [Multiple]: more than one server -> produce a sealed interface hierarchy.
 */
internal sealed interface ServerStrategy {
    data object NoServers : ServerStrategy
    data object SingleFixed : ServerStrategy
    data class SingleVariable(val server: Server, val serverClassName: ClassName) : ServerStrategy
    data class Multiple(val servers: List<Server>, val serverClassName: ClassName) : ServerStrategy
}

/** Determine the [ServerStrategy] for this [ApiTree]. */
internal fun ApiTree.serverStrategy(config: RenderConfig): ServerStrategy {
    val rootName = name.toPascalCase()
    val serverClassName = ClassName(config.apiPackage, "${rootName}Server")

    return when {
        servers.isEmpty() -> ServerStrategy.NoServers
        servers.size == 1 && servers.first().variables.isNullOrEmpty() ->
            ServerStrategy.SingleFixed

        servers.size == 1 ->
            ServerStrategy.SingleVariable(
                server = servers.first(),
                serverClassName = serverClassName,
            )

        else ->
            ServerStrategy.Multiple(
                servers = servers,
                serverClassName = serverClassName,
            )
    }
}
