package site.vie10.radio.logging

import kotlin.system.measureTimeMillis

/**
 * @author vie10
 **/
suspend fun suspendWrappedRun(logging: (FunctionLogger.() -> Unit)? = null, block: suspend () -> Unit) {
    val functionLogger = FunctionLogger().apply { logging?.invoke(this) }
    functionLogger.onStart()
    val result: Result<Unit>
    val measuredMillis: Long = measureTimeMillis {
        result = runCatching {
            block()
        }
    }
    result.onSuccess {
        functionLogger.onSuccess(measuredMillis)
    }.onFailure {
        functionLogger.onFailure(it)
    }
}

@Suppress("MemberVisibilityCanBePrivate")
class FunctionLogger {
    var onStart: (() -> Unit)? = null
    var onFailure: ((throwable: Throwable) -> Unit)? = null
    var onSuccess: ((measuredMillis: Long) -> Unit)? = null

    fun onStart() {
        onStart?.invoke()
    }

    fun onFailure(throwable: Throwable) {
        onFailure?.invoke(throwable)
    }

    fun onSuccess(measuredMillis: Long) {
        onSuccess?.invoke(measuredMillis)
    }
}
