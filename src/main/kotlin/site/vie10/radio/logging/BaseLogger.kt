package site.vie10.radio.logging

/**
 * @author vie10
 **/
abstract class BaseLogger : Logger {

    final override fun info(block: () -> Any) {
        info(block().toString())
    }

    final override fun warn(throwable: Throwable) {
        warn(throwable) { "No message provided. Looks like unexpected exception!" }
    }

    final override fun warn(throwable: Throwable?, block: () -> Any) {
        warn(block().toString(), throwable)
    }

    final override fun exception(throwable: Throwable, block: () -> Any) {
        exception(block().toString(), throwable)
    }

    protected abstract fun info(message: String)

    protected abstract fun warn(message: String, throwable: Throwable?)

    protected abstract fun exception(message: String, throwable: Throwable)
}
