package site.vie10.radio.logging

import kotlin.system.measureTimeMillis

/**
 * @author vie10
 **/
suspend fun suspendWrappedRun(logging: (FunctionLogger<Unit>.() -> Unit)? = null, block: suspend () -> Unit) {
    val functionLogger = FunctionLogger<Unit>().apply { logging?.invoke(this) }
    functionLogger.onStart()
    val result: Result<Unit>
    val measuredMillis: Long = measureTimeMillis {
        result = runCatching {
            block()
        }
    }
    result.onSuccess {
        functionLogger.onSuccess(measuredMillis, it)
    }.onFailure {
        functionLogger.onFailure(it)
    }
}

@Suppress("MemberVisibilityCanBePrivate")
class FunctionLogger<T> {
    var onStart: (() -> Unit)? = null
    var onFailure: ((throwable: Throwable) -> Unit)? = null
    var onSuccess: ((measuredMillis: Long, result: T) -> Unit)? = null

    fun onStart() {
        onStart?.invoke()
    }

    fun onFailure(throwable: Throwable) {
        onFailure?.invoke(throwable)
    }

    fun onSuccess(measuredMillis: Long, result: T) {
        onSuccess?.invoke(measuredMillis, result)
    }
}
