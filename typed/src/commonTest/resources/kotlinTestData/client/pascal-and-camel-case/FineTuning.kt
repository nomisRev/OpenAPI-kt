package pascal.and.camel.case.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post

interface FineTuning {
    val jobs: Jobs

    interface Jobs {
        suspend fun createFineTuningJob(): String
    }
}

internal class KtorFineTuning(private val client: HttpClient) : FineTuning {
    override val jobs: FineTuning.Jobs = KtorFineTuningJobs(client)
}

internal class KtorFineTuningJobs(private val client: HttpClient) : FineTuning.Jobs {
    override suspend fun createFineTuningJob(): String =
        client.post("/fine_tuning/jobs").body()
}
