package deeper.nesting.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

interface FineTuning {
    val jobs: Jobs

    interface Jobs {
        val events: Events

        interface Events {
            suspend fun listFineTuningEvents(
                fineTuningJobId: String,
            ): String
        }
    }
}

internal class KtorFineTuning(private val client: HttpClient) : FineTuning {
    override val jobs: FineTuning.Jobs = KtorFineTuningJobs(client)
}

internal class KtorFineTuningJobs(private val client: HttpClient) : FineTuning.Jobs {
    override val events: FineTuning.Jobs.Events = KtorFineTuningJobsEvents(client)
}

internal class KtorFineTuningJobsEvents(private val client: HttpClient) : FineTuning.Jobs.Events {
    override suspend fun listFineTuningEvents(fineTuningJobId: String): String =
        client.get("/fine_tuning/jobs/$fineTuningJobId/events").body()
}
